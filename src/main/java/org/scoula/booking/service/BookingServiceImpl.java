package org.scoula.booking.service;

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
import org.scoula.booking.dto.DocInfoDto;
import org.scoula.booking.dto.ReservedSlotsResponseDto;
import org.scoula.booking.mapper.BookingMapper;
import org.scoula.branch.service.BranchService;
import org.scoula.exception.DuplicateBookingException;
import org.scoula.exception.InvalidBookingDateException;
import org.scoula.product.service.ProductsService;
import org.springframework.stereotype.Service;

import com.github.f4b6a3.ulid.UlidCreator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
	private final BookingMapper bookingMapper;
	private final ProductsService productsService;
	private final BranchService branchService;

	@Override
	public List<BookingDto> getBookingsByEmail(String email) {
		return bookingMapper.getBookingsByEmail(email).stream()
			.map(BookingDto::of)
			.collect(Collectors.toList());
	}

	@Override
	public BookingDto getBookingById(Integer bookingId) {
		return Optional.ofNullable(bookingMapper.getBookingById(bookingId))
			.map(BookingDto::of)
			.orElseThrow(() -> new NoSuchElementException("Booking not found with id: " + bookingId));
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

	@Override
	public void updateBooking(BookingDto bookingDto) {
		if (bookingMapper.updateBooking(bookingDto.toVo()) == 0) {
			throw new NoSuchElementException("Booking not found with id: " + bookingDto.getBookingId());
		}
	}

	@Override
	public void deleteBooking(Integer bookingId) {
		if (bookingMapper.deleteBooking(bookingId) == 0) {
			throw new NoSuchElementException("Booking not found with id: " + bookingId);
		}
	}

	/**
	 * 특정 예약 상세 조회
	 * @param bookingId 외부 예약 번호
	 * */
	@Override
	public BookingDetailResponseDto getBookingById(String bookingId) {
		// 1. ULID로 DB에서 예약 정보(Vo)를 조회
		BookingVo bookingVo = bookingMapper.findById(bookingId);

		if (bookingVo == null) {
			throw new NoSuchElementException("Booking not found with ulid: " + bookingId);
		}

		// 2. ProductsService에서 상품명을 조회
		String prdtName = productsService.getProductNameByCode(bookingVo.getFinPrdtCode());
		String branchName = branchService.getBranchNameById(bookingVo.getBranchId());

		// 3. BookingDetailResponseDto.of()를 호출하여 최종 DTO 생성
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
	public BookingCheckResponseDto checkBookingExists(String email, String prdtCode) {
		// 1. DB에서 해당 사용자의 특정 상품 예약을 조회 (BookingVo 또는 null을 받음)
		BookingVo existingBooking = bookingMapper.findByEmailAndFinPrdtCode(email, prdtCode);

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
