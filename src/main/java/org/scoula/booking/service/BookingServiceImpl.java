package org.scoula.booking.service;

import java.nio.file.AccessDeniedException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.scoula.booking.domain.BookingVo;
import org.scoula.booking.dto.BookingCheckDetailDto;
import org.scoula.booking.dto.BookingCheckResponseDto;
import org.scoula.booking.dto.BookingCreateRequestDto;
import org.scoula.booking.dto.BookingCreateResponseDto;
import org.scoula.booking.dto.BookingDetailResponseDto;
import org.scoula.booking.dto.BookingDto;
import org.scoula.booking.dto.BookingPatchRequestDto;
import org.scoula.booking.dto.DocInfoDto;
import org.scoula.booking.dto.ReservedSlotsResponseDto;
import org.scoula.booking.mapper.BookingMapper;
import org.scoula.branch.service.BranchService;
import org.scoula.exception.DuplicateBookingException;
import org.scoula.exception.InvalidBookingDateException;
import org.scoula.product.service.ProductService;
import org.scoula.product.service.ProductsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.f4b6a3.ulid.UlidCreator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
	private final BookingMapper bookingMapper;
	private final ProductsService productsService;
	private final ProductService productService;
	private final BranchService branchService;

	@Override
	public List<BookingDto> getBookingsByEmail(String email) {
		return bookingMapper.getBookingsByEmail(email).stream()
			.map(BookingDto::of)
			.collect(Collectors.toList());
	}

	@Override
	public BookingCreateResponseDto addBooking(String email, BookingCreateRequestDto requestDto) {
		BookingVo bookingVo = requestDto.toVo();

		// 0. 예약 날짜 유효성 검사 로직
		validateBookingDate(bookingVo.getDate());

		// 1. 중복 예약 확인 로직
		int existingBookings = bookingMapper.countByBranchDateTime(
			bookingVo.getBranchId(),
			bookingVo.getDate(),
			bookingVo.getTime()
		);

		// 2. 이미 예약이 있다면 예외 발생
		if (existingBookings > 0) {
			throw new DuplicateBookingException("해당 지점의 해당 시간에는 이미 예약이 존재합니다.");
		}

		DocInfoDto initialDocInfo = generateInitialDocInfo(requestDto.getFinPrdtCode());
		String bookingId = UlidCreator.getUlid().toString();

		// Vo에 모든 정보 설정
		bookingVo.setEmail(email);
		bookingVo.setDocInfo(initialDocInfo);
		bookingVo.setBookingId(bookingId);

		// DB에 저장
		bookingMapper.insertBooking(bookingVo);

		// 올바른 인자들로 of 메서드 호출
		return BookingCreateResponseDto.of(bookingVo);
	}

	@Transactional
	@Override // 인터페이스를 구현하므로 @Override 추가
	public BookingDetailResponseDto patchBooking(String bookingId, String email,
		BookingPatchRequestDto patchDto) throws AccessDeniedException {

		// 1. 기존 예약 데이터를 DB에서 조회
		BookingVo existingBooking = bookingMapper.findById(bookingId);

		// 2. 예약 존재 여부 확인
		if (existingBooking == null) {
			throw new NoSuchElementException("해당 예약을 찾을 수 없습니다: " + bookingId);
		}

		// 3. 수정 권한 확인 (매우 중요!)
		if (!existingBooking.getEmail().equals(email)) {
			throw new AccessDeniedException("예약을 수정할 권한이 없습니다.");
		}

		// 4. 변경 요청된 필드가 있는지 확인하고 값 업데이트
		boolean isTimeSlotChanged = false;
		// 4-1. 날짜 변경 요청이 있는 경우
		if (patchDto.getDate() != null && !patchDto.getDate().isEmpty()) {
			Date newDate = parseDate(patchDto.getDate()); // 헬퍼 메소드로 날짜 파싱
			validateBookingDate(newDate);                 // 헬퍼 메소드로 날짜 유효성 검사
			existingBooking.setDate(newDate);
			isTimeSlotChanged = true;
		}
		// 4-2. 시간 변경 요청이 있는 경우
		if (patchDto.getTime() != null && !patchDto.getTime().isEmpty()) {
			existingBooking.setTime(patchDto.getTime());
			isTimeSlotChanged = true;
		}

		// 5. 날짜 또는 시간이 변경되었다면, 해당 시간대가 비어있는지 중복 확인
		if (isTimeSlotChanged) {
			int existingCount = bookingMapper.countByBranchDateTime(
				existingBooking.getBranchId(),
				existingBooking.getDate(),
				existingBooking.getTime()
			);
			if (existingCount > 0) {
				throw new DuplicateBookingException("변경하려는 시간에는 이미 다른 예약이 존재합니다.");
			}
		}

		// 6. 수정된 내용이 반영된 Vo 객체로 DB 업데이트
		bookingMapper.updateBooking(existingBooking);

		// 7. 수정된 최종 정보를 DTO로 만들어 반환
		String prdtName = productService.getProductNameByCode(existingBooking.getFinPrdtCode());
		String branchName = branchService.getBranchNameById(existingBooking.getBranchId());

		return BookingDetailResponseDto.of(existingBooking, prdtName, branchName);
	}

	/**
	 * 헬퍼 메서드: 날짜를 파싱합니다.
	 * @param dateString String 타입으로 된 date
	 */
	private Date parseDate(String dateString) {
		try {
			return new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
		} catch (ParseException e) {
			throw new IllegalArgumentException("Invalid date format. Please use yyyy-MM-dd.", e);
		}
	}

	/**
	 * 헬퍼 메서드: 상품 코드에 따라 필요한 서류 목록을 포함한 초기 DocInfoDto를 생성합니다.
	 * @param prdtCode 상품 코드 (예: "LN1001...", "DP1001...")
	 * @return 생성된 DocInfoDto
	 */
	private DocInfoDto generateInitialDocInfo(String prdtCode) {
		DocInfoDto docInfo = new DocInfoDto();
		List<String> requiredDocs = new ArrayList<>();

		// prdtCode가 null이거나 비어있는 경우를 대비한 방어 코드
		if (prdtCode == null || prdtCode.isEmpty()) {
			requiredDocs.add("신분증 (필수)");
			docInfo.setRequiredDocuments(requiredDocs);
			return docInfo;
		}

		// 주택담보대출("LN") 상품일 경우
		if (prdtCode.startsWith("LN")) {
			requiredDocs.add("신분증");
			requiredDocs.add("주민등록등본");
			requiredDocs.add("가족관계증명서");
			requiredDocs.add("인감증명서 및 인감도장");
			requiredDocs.add("소득증빙서류 (재직/사업자 유형에 따라 상이)");
			requiredDocs.add("부동산 등기권리증 또는 매매계약서");
			requiredDocs.add("건축물대장");
		} else if (prdtCode.startsWith("DP")) { // 예금/적금("DP") 상품일 경우
			requiredDocs.add("신분증");
		} else { // 그 외 다른 상품일 경우 (기본 서류)
			requiredDocs.add("신분증");
			requiredDocs.add("상품가입 관련 추가서류 (필요시)");
		}

		docInfo.setRequiredDocuments(requiredDocs);
		return docInfo;
	}

	/**
	 * 헬퍼 메서드: 예약 날짜가 유효한지 (오늘부터 한 달 이내) 검사합니다.
	 * @param bookingDate 검사할 예약 날짜
	 */
	private void validateBookingDate(Date bookingDate) {
		if (bookingDate == null) {
			throw new InvalidBookingDateException("예약 날짜를 입력해주세요.");
		}

		// java.util.Date를 최신 LocalDate로 변환하여 비교
		LocalDate today = LocalDate.now();
		LocalDate requestedDate = bookingDate.toInstant()
			.atZone(ZoneId.systemDefault())
			.toLocalDate();
		// 오늘로부터 한 달 뒤의 날짜 계산
		LocalDate oneMonthLater = today.plusMonths(1);

		// 과거 날짜이거나, 한 달 이후의 날짜인지 확인
		if (requestedDate.isBefore(today) || requestedDate.isAfter(oneMonthLater)) {
			throw new InvalidBookingDateException("예약은 오늘부터 한 달 이내의 날짜만 가능합니다.");
		}
	}

	@Transactional
	@Override
	public void deleteBooking(String bookingId, String currentUserEmail) throws AccessDeniedException {
		// 1. 삭제할 예약 정보를 DB에서 조회합니다.
		BookingVo bookingVo = bookingMapper.findById(bookingId);

		// 2. 예약이 존재하는지 확인합니다.
		if (bookingVo == null) {
			// 예약이 없어도 굳이 에러를 발생시키지 않고 정상 처리할 수도 있습니다.
			// 여기서는 일단 없다고 알려주는 방식으로 처리합니다.
			throw new NoSuchElementException("해당 예약을 찾을 수 없습니다: " + bookingId);
		}

		// 3. 삭제 권한이 있는지 확인합니다 (매우 중요!).
		if (!bookingVo.getEmail().equals(currentUserEmail)) {
			throw new AccessDeniedException("예약을 삭제할 권한이 없습니다.");
		}

		// 4. 권한이 확인되면 예약을 삭제합니다.
		bookingMapper.deleteBooking(bookingId);
	}

	@Override
	public BookingDetailResponseDto getBookingById(String bookingId) {
		// 1. ULID로 DB에서 예약 정보(Vo)를 조회
		BookingVo bookingVo = bookingMapper.findById(bookingId);

		if (bookingVo == null) {
			throw new NoSuchElementException("Booking not found with ulid: " + bookingId);
		}

		// [추가] DB에서 가져온 시간 값의 초 단위 제거
		String time = bookingVo.getTime();
		if (time != null && time.length() > 5) {
			bookingVo.setTime(time.substring(0, 5));
		}

		// 2. ProductService에서 상품명을 조회
		String prdtName = productService.getProductNameByCode(bookingVo.getFinPrdtCode());
		String branchName = branchService.getBranchNameById(bookingVo.getBranchId());

		// 3. 수정된 bookingVo를 사용하여 최종 DTO 생성
		return BookingDetailResponseDto.of(bookingVo, prdtName, branchName);
	}

	/**
	 * DB에서 가져온 예약 목록(List<BookingVo>)을
	 * 우리가 원하는 최종 형태(Map<String, List<String>>)로 가공하는 핵심 로직
	 * @param branchId 지점번호
	 * */
	@Override
	public ReservedSlotsResponseDto getReservedSlotsByBranch(int branchId) {
		// 1. 조회 시작 날짜를 '오늘'로 설정
		LocalDate today = LocalDate.now();
		Date startDate = Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant());

		// 2. DB에서 오늘 현재 시간 이후의 모든 예약 목록을 가져옴
		Date currentDate = new Date(); // 오늘 날짜
		String currentTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
		List<BookingVo> futureBookings = bookingMapper.findFutureByBranch(branchId, currentDate, currentTime);

		// 3. 날짜 포맷터 정의
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		// 4. Stream의 groupingBy를 사용하여 List를 Map으로 변환
		Map<String, List<String>> reservedSlotsMap = futureBookings.stream()
			.collect(Collectors.groupingBy(
				// 그룹화의 기준(Key): 날짜를 "yyyy-MM-dd" 형식의 문자열로 변환
				booking -> booking.getDate().toInstant()
					.atZone(ZoneId.systemDefault())
					.toLocalDate()
					.format(dateFormatter),
				// 그룹화된 요소(Value): 시간(time)만 모아서 리스트로 만듦
				Collectors.mapping(
					booking -> {
						String time = booking.getTime();
						// 시간이 "10:00:00" 형태일 경우, 앞에서 5글자("10:00")만 잘라냄
						// null이거나 형식이 다른 경우를 대비하여 방어 코드 추가
						return (time != null && time.length() >= 5) ? time.substring(0, 5) : time;
					},
					Collectors.toList()
				)
			));

		// 5. 최종 DTO 로 감싸서 반환
		return new ReservedSlotsResponseDto(reservedSlotsMap);
	}

	/**
	 * 사용자가 해당 상품에 대한 예약 여부 조회
	 * */
	public BookingCheckResponseDto checkBookingExists(String email, String finPrdtCode) {
		// 1. DB에서 해당 사용자의 특정 상품 예약을 조회 (BookingVo 또는 null을 받음)
		BookingVo existingBooking = bookingMapper.findByEmailAndFinPrdtCode(email, finPrdtCode);

		// 2. ofNullable을 사용하여 결과를 Optional로 직접 감싸줌
		Optional<BookingVo> existingBookingOpt = Optional.ofNullable(existingBooking);

		// 3. Optional의 isPresent()로 존재 여부 확인
		if (existingBookingOpt.isPresent()) {
			// 4. 예약이 존재하면, Vo를 DTO로 변환
			BookingVo booking = existingBookingOpt.get();
			BookingCheckDetailDto detailDto = BookingCheckDetailDto.from(booking);
			return new BookingCheckResponseDto(true, detailDto);
		} else {
			// 4. 예약이 존재하지 않으면, details는 null로 설정
			return new BookingCheckResponseDto(false, null);
		}
	}
}
