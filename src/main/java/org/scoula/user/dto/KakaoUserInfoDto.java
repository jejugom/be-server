package org.scoula.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class KakaoUserInfoDto {
	private Long id;
	@JsonProperty("connected_at")
	private String connectedAt;
	private Properties properties;
	@JsonProperty("kakao_account")
	private KakaoAccount kakaoAccount;

	@Data
	public static class Properties {
		private String nickname;
		@JsonProperty("profile_image")
		private String profileImage;
		@JsonProperty("thumbnail_image")
		private String thumbnailImage;
	}

	@Data
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class KakaoAccount {
		@JsonProperty("profile_nickname_needs_agreement")
		private Boolean profileNicknameNeedsAgreement;
		private Profile profile;
		private Boolean emailNeedsAgreement;
		private Boolean isEmailValid;
		private Boolean isEmailVerified;
		private String email;
		private String birthyear;
		private String birthday;

		@Data
		@JsonIgnoreProperties(ignoreUnknown = true)
		public static class Profile {
			private String nickname;
			@JsonProperty("thumbnail_image_url")
			private String thumbnailImageUrl;
			@JsonProperty("profile_image_url")
			private String profileImageUrl;
			@JsonProperty("is_default_image")
			private Boolean isDefaultImage;
		}
	}
}
