package org.scoula.booking.config; // 혹은 공통 패키지

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.scoula.booking.dto.DocInfoDto;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@MappedTypes(DocInfoDto.class) // 이 TypeHandler가 처리할 Java 타입을 지정
public class JsonTypeHandler extends BaseTypeHandler<DocInfoDto> {

	private static final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, DocInfoDto parameter, JdbcType jdbcType) throws SQLException {
		try {
			ps.setString(i, objectMapper.writeValueAsString(parameter));
		} catch (JsonProcessingException e) {
			throw new SQLException("Error converting DocInfoDto to JSON string", e);
		}
	}

	@Override
	public DocInfoDto getNullableResult(ResultSet rs, String columnName) throws SQLException {
		String json = rs.getString(columnName);
		return json == null ? null : parseJson(json);
	}

	@Override
	public DocInfoDto getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		String json = rs.getString(columnIndex);
		return json == null ? null : parseJson(json);
	}

	@Override
	public DocInfoDto getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		String json = cs.getString(columnIndex);
		return json == null ? null : parseJson(json);
	}

	private DocInfoDto parseJson(String json) throws SQLException {
		try {
			return objectMapper.readValue(json, DocInfoDto.class);
		} catch (JsonProcessingException e) {
			throw new SQLException("Error parsing JSON string to DocInfoDto", e);
		}
	}
}
