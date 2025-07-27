package org.scoula.booking.service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.scoula.booking.domain.BookingVo;
import org.scoula.booking.dto.BookingDto;
import org.scoula.booking.dto.BookingRequestDto;
import org.scoula.booking.dto.BookingResponseDto;
import org.scoula.booking.dto.DocInfoDto;
import org.scoula.booking.mapper.BookingMapper;
import org.springframework.stereotype.Service;

import com.github.f4b6a3.ulid.UlidCreator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
	private final BookingMapper bookingMapper;

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
	public BookingResponseDto addBooking(String email, BookingRequestDto requestDto) {
		BookingVo bookingVo = requestDto.toVo();
		DocInfoDto initialDocInfo = generateInitialDocInfo(requestDto.getProdCode());
		String bookingUlid = UlidCreator.getUlid().toString();

		// Vo에 모든 정보 설정
		bookingVo.setEmail(email);
		bookingVo.setDocInfo(initialDocInfo);
		bookingVo.setBookingUlid(bookingUlid);

		// DB에 저장
		bookingMapper.insertBooking(bookingVo);

		// 올바른 인자들로 of 메서드 호출
		return BookingResponseDto.of(bookingVo);
	}

	/**
	 * 헬퍼 메서드: 상품 코드에 따라 필요한 서류 목록을 포함한 초기 DocInfoDto를 생성합니다.
	 * @param prodCode 상품 코드 (예: "LN1001...", "DP1001...")
	 * @return 생성된 DocInfoDto
	 */
	private DocInfoDto generateInitialDocInfo(String prodCode) {
		DocInfoDto docInfo = new DocInfoDto();
		List<String> requiredDocs = new ArrayList<>();

		// prodCode가 null이거나 비어있는 경우를 대비한 방어 코드
		if (prodCode == null || prodCode.isEmpty()) {
			requiredDocs.add("신분증 (필수)");
			docInfo.setRequiredDocuments(requiredDocs);
			return docInfo;
		}

		// 주택담보대출("LN") 상품일 경우
		if (prodCode.startsWith("LN")) {
			requiredDocs.add("신분증");
			requiredDocs.add("주민등록등본");
			requiredDocs.add("가족관계증명서");
			requiredDocs.add("인감증명서 및 인감도장");
			requiredDocs.add("소득증빙서류 (재직/사업자 유형에 따라 상이)");
			requiredDocs.add("부동산 등기권리증 또는 매매계약서");
			requiredDocs.add("건축물대장");
		} else if (prodCode.startsWith("DP")) { // 예금/적금("DP") 상품일 경우
			requiredDocs.add("신분증");
		} else { // 그 외 다른 상품일 경우 (기본 서류)
			requiredDocs.add("신분증");
			requiredDocs.add("상품가입 관련 추가서류 (필요시)");
		}

		docInfo.setRequiredDocuments(requiredDocs);
		return docInfo;
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
}
