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
    private int bookingId;
    private String email;
    private String prdtCode;
    private Date date;
    private String time;
    private String docInfo;
    private String branchName;
    private String branchBranchName;
    private String userEmail;
}
