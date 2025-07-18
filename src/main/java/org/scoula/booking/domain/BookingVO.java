package org.scoula.booking.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingVO {
    private Integer id;
    private String email;
    private String bankCode;
    private String prodCode;
    private Date date;
    private String time;
    private String docInfo;
}
