# gradle과 JDK를 포함하는 이미지 사용
FROM gradle:7.3.1-jdk17 AS build

# 작업 디렉토리 설정
WORKDIR /home/gradle/project

# 소스코드 복사
COPY --chown=gradle:gradle . .

# gradlew를 이용한 프로젝트 빌드
RUN ./gradlew clean build -x test

# 최종 실행 이미지
FROM openjdk:17
WORKDIR /app
COPY --from=build /home/gradle/project/build/libs/web-ide-0.0.1-SNAPSHOT.jar /app/

# DATABASE_URL 환경 변수 설정
#ENV DATABASE_URL=jdbc:mysql://localhost:3306/everyide

# 빌드 결과 jar 파일을 실행
CMD ["java", "-jar", "web-ide-0.0.1-SNAPSHOT.jar"]
