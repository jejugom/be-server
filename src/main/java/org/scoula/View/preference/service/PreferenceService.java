package org.scoula.View.preference.service;

import org.scoula.View.preference.dto.PreferenceRequestDto;

public interface PreferenceService {
	void setUserPreference(PreferenceRequestDto requestDto,String userEmail);
}
