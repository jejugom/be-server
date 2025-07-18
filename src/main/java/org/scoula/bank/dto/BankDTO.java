package org.scoula.bank.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.scoula.bank.domain.BankVO;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BankDTO {
    private String code;
    private String name;
    private String tel;
    private String address;
    private String x;
    private String y;

    public static BankDTO of(BankVO bank) {
        return BankDTO.builder()
                .code(bank.getCode())
                .name(bank.getName())
                .tel(bank.getTel())
                .address(bank.getAddress())
                .x(bank.getX())
                .y(bank.getY())
                .build();
    }

    public BankVO toVO() {
        return BankVO.builder()
                .code(code)
                .name(name)
                .tel(tel)
                .address(address)
                .x(x)
                .y(y)
                .build();
    }
}
