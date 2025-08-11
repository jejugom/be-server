package org.scoula.question.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "질문 응답 DTO")
public class QuestionResponseDto {
    
    @ApiModelProperty(value = "처리 상태", example = "SUCCESS", required = true)
    private String status;
    
    @ApiModelProperty(value = "응답 메시지", example = "질문이 성공적으로 처리되었습니다.", required = true)
    private String message;
    
    @ApiModelProperty(value = "처리된 텍스트", example = "금융 용어에 대한 설명", required = false)
    private String processedText;
    
    @ApiModelProperty(value = "AI 응답", example = "AI가 생성한 답변", required = false)
    private String aiResponse;
    
    public QuestionResponseDto(String status, String message) {
        this.status = status;
        this.message = message;
    }
    
    public QuestionResponseDto(String status, String message, String processedText, String aiResponse) {
        this.status = status;
        this.message = message;
        this.processedText = processedText;
        this.aiResponse = aiResponse;
    }
}