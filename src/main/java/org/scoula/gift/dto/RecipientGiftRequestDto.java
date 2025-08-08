package org.scoula.gift.dto;

import java.util.List;

import lombok.Data;

@Data
public class RecipientGiftRequestDto {
	private Integer recipientId;
	private List<CategoryGiftRequestDto> categoriesToGift;
}