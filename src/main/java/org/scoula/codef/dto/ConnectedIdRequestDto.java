package org.scoula.codef.dto;

import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class ConnectedIdRequestDto {
	private List<Map<String, Object>> accountList;
}
