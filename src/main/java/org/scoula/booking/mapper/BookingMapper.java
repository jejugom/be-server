package org.scoula.booking.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.scoula.booking.domain.BookingVo;

public interface BookingMapper {

	/**
	 * 이메일로 예약 목록 조회
	 * @param email 사용자 이메일
	 * @return 예약 VO 리스트
	 */
	List<BookingVo> getBookingsByEmail(String email);

	/**
	 * 예약 ID로 예약 조회 (정수형 ID 사용 시)
	 * @param id 예약 ID
	 * @return 예약 VO
	 */
	BookingVo getBookingById(Integer id);

	/**
	 * 예약 정보 DB에 삽입
	 * @param booking 예약 VO
	 */
	void insertBooking(BookingVo booking);

	/**
	 * 예약 정보 업데이트
	 * @param booking 수정할 예약 VO
	 * @return 영향받은 행(row) 수
	 */
	int updateBooking(BookingVo booking);

	/**
	 * 예약 삭제
	 * @param bookingId 삭제할 예약의 ULID 문자열
	 * @return 영향받은 행(row) 수
	 */
	int deleteBooking(String bookingId);

	int deleteByEmail(@Param("email") String email);

	/**
	 * 특정 지점(branchId), 날짜(date), 시간(time)에 해당하는 예약 건수 조회
	 * @param branchId 지점 번호
	 * @param date 예약 날짜 (java.util.Date)
	 * @param time 예약 시간 (HH:mm 형식)
	 * @return 예약 건수
	 */
	int countByBranchDateTime(@Param("branchId") int branchId, @Param("date") Date date,
		@Param("time") String time);

	/**
	 * 예약 ID(ULID)로 예약 조회
	 * @param bookingId 예약 ULID
	 * @return 예약 VO
	 */
	BookingVo findById(String bookingId);

	/**
	 * 특정 지점의 현재 날짜와 시간 이후에 예약된 미래 예약 목록 조회
	 * @param branchId 지점 번호
	 * @param currentTime 현재 시간 (HH:mm 형식)
	 * @return 미래 예약 VO 리스트
	 */
	List<BookingVo> findFutureByBranch(
		@Param("branchId") int branchId,
		@Param("currentTime") String currentTime
	);

	/**
	 * 이메일과 금융 상품 코드(finPrdtCode)를 기준으로 예약 조회
	 * @param email 사용자 이메일
	 * @param finPrdtCode 금융 상품 코드
	 * @return 예약 VO (존재하지 않으면 null 반환)
	 */
	BookingVo findByEmailAndFinPrdtCode(@Param("email") String email, @Param("finPrdtCode") String finPrdtCode);
}
