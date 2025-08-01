package org.scoula.View.preference.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PreferenceRequestDto {
	private int q1;
	private int q2;
	private int q3;
	private int q4;
	private int q5;
}
