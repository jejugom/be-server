package org.scoula.View.codef.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;
import org.scoula.View.codef.service.CodefTokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class CodefApiClient {

	@Value("${codef.oauth.domain}")
	private String oauthDomain;

	@Value("${codef.api.domain}")
	private String apiDomain;

	@Value("${codef.client.id}")
	private String clientId;

	@Value("${codef.client.secret}")
	private String clientSecret;

	@Value("${codef.public.key}")
	private String publicKey;

	private static final String GET_TOKEN_PATH = "/oauth/token";
	private static final String CREATE_ACCOUNT_PATH = "/v1/account/create";
	private static final String KR_BK_1_P_001_PATH = "/v1/kr/bank/p/account/account-list";
	private static final ObjectMapper mapper = new ObjectMapper();

	private CodefTokenService codefTokenService;

	public void setCodefTokenService(CodefTokenService codefTokenService) {
		this.codefTokenService = codefTokenService;
	}

	private String getAccessToken() {
		if (codefTokenService != null) {
			return codefTokenService.getAccessToken();
		}
		return null;
	}

	public Map<String, Object> publishToken() {
		BufferedReader br = null;
		try {
			URL url = new URL(oauthDomain + GET_TOKEN_PATH);
			String params = "grant_type=client_credentials&scope=read";

			HttpURLConnection con = (HttpURLConnection)url.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

			String auth = clientId + ":" + clientSecret;
			String authHeader = "Basic " + Base64.encodeBase64String(auth.getBytes());
			con.setRequestProperty("Authorization", authHeader);
			con.setDoInput(true);
			con.setDoOutput(true);

			try (OutputStream os = con.getOutputStream()) {
				os.write(params.getBytes());
			}

			int responseCode = con.getResponseCode();
			if (responseCode != HttpURLConnection.HTTP_OK) {
				log.error("Failed to get access token. Response Code: {}", responseCode);
				return null;
			}

			StringBuilder responseStr = new StringBuilder();
			br = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			while ((inputLine = br.readLine()) != null) {
				responseStr.append(inputLine);
			}

			String decoded = URLDecoder.decode(responseStr.toString(), "UTF-8");
			return mapper.readValue(decoded, Map.class);

		} catch (Exception e) {
			log.error("Error while publishing token: {}", e.getMessage(), e);
			return null;
		} finally {
			if (br != null)
				try {
					br.close();
				} catch (IOException e) {
					log.error("Error closing reader", e);
				}
		}
	}

	public Map<String, Object> createConnectedId(Map<String, Object> bodyMap) {
		BufferedReader br = null;
		try {
			URL url = new URL(apiDomain + CREATE_ACCOUNT_PATH);
			HttpURLConnection con = (HttpURLConnection)url.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("Authorization", "Bearer " + getAccessToken());
			con.setDoInput(true);
			con.setDoOutput(true);

			String jsonInputString = mapper.writeValueAsString(bodyMap);
			try (OutputStream os = con.getOutputStream()) {
				os.write(jsonInputString.getBytes("UTF-8"));
			}

			int responseCode = con.getResponseCode();
			if (responseCode != HttpURLConnection.HTTP_OK) {
				log.error("Failed to create ConnectedId. Response Code: {}", responseCode);
				try (BufferedReader errorBr = new BufferedReader(new InputStreamReader(con.getErrorStream()))) {
					StringBuilder errorResponse = new StringBuilder();
					String errorLine;
					while ((errorLine = errorBr.readLine()) != null) {
						errorResponse.append(errorLine);
					}
					log.error("Error Response: {}", errorResponse.toString());
				}
				return null;
			}

			StringBuilder responseStr = new StringBuilder();
			br = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			while ((inputLine = br.readLine()) != null) {
				responseStr.append(inputLine);
			}

			String decoded = URLDecoder.decode(responseStr.toString(), "UTF-8");
			return mapper.readValue(decoded, Map.class);

		} catch (Exception e) {
			log.error("Error while creating ConnectedId: {}", e.getMessage(), e);
			return null;
		} finally {
			if (br != null)
				try {
					br.close();
				} catch (IOException e) {
					log.error("Error closing reader", e);
				}
		}
	}

	public Map<String, Object> getAccountInfo(String connectedId, String organization) {
		BufferedReader br = null;
		try {
			URL url = new URL(apiDomain + KR_BK_1_P_001_PATH);
			HttpURLConnection con = (HttpURLConnection)url.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("Authorization", "Bearer " + getAccessToken());
			con.setDoInput(true);
			con.setDoOutput(true);

			Map<String, Object> bodyMap = new HashMap<>();
			bodyMap.put("connectedId", connectedId);
			bodyMap.put("organization", organization);

			String jsonInputString = mapper.writeValueAsString(bodyMap);
			try (OutputStream os = con.getOutputStream()) {
				os.write(jsonInputString.getBytes("UTF-8"));
			}

			int responseCode = con.getResponseCode();
			if (responseCode != HttpURLConnection.HTTP_OK) {
				log.error("Failed to get account info. Response Code: {}", responseCode);
				try (BufferedReader errorBr = new BufferedReader(new InputStreamReader(con.getErrorStream()))) {
					StringBuilder errorResponse = new StringBuilder();
					String errorLine;
					while ((errorLine = errorBr.readLine()) != null) {
						errorResponse.append(errorLine);
					}
					log.error("Error Response: {}", errorResponse.toString());
				}
				return null;
			}

			StringBuilder responseStr = new StringBuilder();
			br = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			while ((inputLine = br.readLine()) != null) {
				responseStr.append(inputLine);
			}

			String decoded = URLDecoder.decode(responseStr.toString(), "UTF-8");
			return mapper.readValue(decoded, Map.class);

		} catch (Exception e) {
			log.error("Error while getting account info: {}", e.getMessage(), e);
			return null;
		} finally {
			if (br != null)
				try {
					br.close();
				} catch (IOException e) {
					log.error("Error closing reader", e);
				}
		}
	}

	public String encryptRSA(String plainText, String base64PublicKey) throws Exception {
		byte[] bytePublicKey = java.util.Base64.getDecoder().decode(base64PublicKey);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PublicKey publicKey = keyFactory.generatePublic(new X509EncodedKeySpec(bytePublicKey));

		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		byte[] bytePlain = plainText.getBytes("UTF-8");
		log.info("Plain text byte length: {}", bytePlain.length);
		return java.util.Base64.getEncoder().encodeToString(cipher.doFinal(bytePlain));
	}

	public String getPublicKey() {
		return publicKey;
	}
}

