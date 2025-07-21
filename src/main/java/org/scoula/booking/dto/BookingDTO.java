package org.scoula.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.scoula.booking.domain.BookingVO;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingDTO {
    private int bookingId;
    private String email;
    private String prdtCode;
    private Date date;
    private String time;
    private String docInfo;
    private String branchName;
    private String branchBranchName;
    private String userEmail;

    public static BookingDTO of(BookingVO booking) {
        return BookingDTO.builder()
                .bookingId(booking.getBookingId())
                .email(booking.getEmail())
                .prdtCode(booking.getPrdtCode())
                .date(booking.getDate())
                .time(booking.getTime())
                .docInfo(booking.getDocInfo())
                .branchName(booking.getBranchName())
                .branchBranchName(booking.getBranchBranchName())
                .userEmail(booking.getUserEmail())
                .build();
    }

    public BookingVO toVO() {
        return BookingVO.builder()
                .bookingId(bookingId)
                .email(email)
                .prdtCode(prdtCode)
                .date(date)
                .time(time)
                .docInfo(docInfo)
                .branchName(branchName)
                .branchBranchName(branchBranchName)
                .userEmail(userEmail)
                .build();
    }
}
