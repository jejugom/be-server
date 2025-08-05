FROM tomcat:9.0-jdk16

# 기존 default webapps 삭제 (필수 아님, 권장)
RUN rm -rf /usr/local/tomcat/webapps/*

# WAR 복사 (Gradle 빌드 결과물)
COPY build/libs/*.war /usr/local/tomcat/webapps/ROOT.war

# Tomcat 포트 노출
EXPOSE 8080
