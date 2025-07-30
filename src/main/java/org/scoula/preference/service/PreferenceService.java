package org.scoula.preference.service;

import org.scoula.preference.dto.PreferenceRequestDto;

public interface PreferenceService {
	void setUserPreference(PreferenceRequestDto requestDto,String userEmail);
}
