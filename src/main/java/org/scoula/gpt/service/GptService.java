package org.scoula.gpt.service;

import org.scoula.gpt.dto.ChatRequestDto;
import org.scoula.gpt.dto.GptParsedResponse;

public interface GptService {
	GptParsedResponse getGptResponseAndParse(ChatRequestDto chatRequestDto);
}
