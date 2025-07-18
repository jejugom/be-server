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
    private Integer id;
    private String email;
    private String bankCode;
    private String prodCode;
    private Date date;
    private String time;
    private String docInfo;

    public static BookingDTO of(BookingVO booking) {
        return BookingDTO.builder()
                .id(booking.getId())
                .email(booking.getEmail())
                .bankCode(booking.getBankCode())
                .prodCode(booking.getProdCode())
                .date(booking.getDate())
                .time(booking.getTime())
                .docInfo(booking.getDocInfo())
                .build();
    }

    public BookingVO toVO() {
        return BookingVO.builder()
                .id(id)
                .email(email)
                .bankCode(bankCode)
                .prodCode(prodCode)
                .date(date)
                .time(time)
                .docInfo(docInfo)
                .build();
    }
}
