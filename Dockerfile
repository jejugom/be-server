FROM tomcat:9.0-jdk16

# 패키지 목록을 업데이트하고, FFmpeg을 설치.
# -y 플래그는 설치 과정의 모든 질문에 'Yes'로 답하도록 설정.
RUN apt-get update && apt-get install -y ffmpeg

# 기존 default webapps 삭제 (필수 아님, 권장)
RUN rm -rf /usr/local/tomcat/webapps/*

# WAR 복사 (Gradle 빌드 결과물)
COPY build/libs/*.war /usr/local/tomcat/webapps/ROOT.war

# Tomcat 포트 노출
EXPOSE 8080
