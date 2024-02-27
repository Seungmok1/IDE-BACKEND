# Every IDE
## Web IDE Project - Backend
* 구름톤 트레이닝 풀스택 개발자 과정
* 개발 기간 : 2024/1/29 ~ 2024/2/26

## 배포 주소
> [EVERY IDE](https://k547f55f71a44a.user-app.krampoline.com/my/dashboard/containers)

## 백엔드 팀 소개
|**Name**|정재우|권승목|김민식|윤혜진|
|:---:|:---:|:---:|:---:|:---:|
|**Picture**|<img src="https://avatars.githubusercontent.com/u/133872161?s=70&v=4" height= "80px" width= "80px">|<img src="https://avatars.githubusercontent.com/u/103080705?s=70&v=4" height= "80px" width= "80px">|<img src="https://avatars.githubusercontent.com/u/100274306?s=70&v=4" height= "80px" width= "80px">|<img src="https://avatars.githubusercontent.com/u/148074385?s=70&v=4" height= "80px" width= "80px">|
|**Role**|Backend Lead, OAuth, AWS, Cloud System, Docker|WebSocket, Chatting, Session Management|ERD, API Docs, Terminal, Container, File Dir System|Credential Login, Krampoline, Docker, Local File Upload|
|**Github**|<a href="https://github.com/JamieJai"><img src="http://img.shields.io/badge/JamieJai-green?style=social&logo=github"/>|<a href="https://github.com/Seungmok1"><img src="http://img.shields.io/badge/Seungmok1-green?style=social&logo=github"/>|<a href="https://github.com/k-minsik"><img src="http://img.shields.io/badge/k-minsik-green?style=social&logo=github"/>|<a href="https://github.com/Hyejin724"><img src="http://img.shields.io/badge/Hyejin724-green?style=social&logo=github"/>|

## 프로젝트 소개
### 핵심 기능
![핵심 기능](https://github.com/every-ide/IDE-BACKEND/assets/103080705/0fa9256b-f40e-47e3-9f4c-ebf18641869b)

### 아키텍처
![아키텍처](https://github.com/every-ide/IDE-BACKEND/assets/103080705/55143bd0-53b7-4cdc-b660-5af93ba47ec0)

### 기술 스택
<img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white">  <img src="https://img.shields.io/badge/spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white">  <img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white"> <img src="https://img.shields.io/badge/spring security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white"> <img src="https://img.shields.io/badge/Websocket-112141?style=for-the-badge&logo=websocket&logoColor=white"> <img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=Docker&logoColor=white"> <img src="https://img.shields.io/badge/jwt-000000?style=for-the-badge&logo=JSONWebTokens&logoColor=white">

## 주요 기능
### 로그인
Crendtiial 로그인과 OAuth 로그인. 두 가지 방식으로 로그인 할 수 있습니다. Jwt을 발급하여 인증이 필요한 REST API를 구성하였고, 토큰이 만료되면 쿠키에 있는 Refresh Token을 통해서 새로 발급받을 수 있습니다.
### 개인 실습용 컨테이너 환경
REST API를 통하여 개인 컨테이너 CRUD를 구현하였고 WebSocket API를 통하여 터미널을 사용할 수 있습니다.
### 스터디를 위한 커뮤니티 플랫폼
@ElementCollection을 활용하여 커뮤니티-유저 혹은 유저-커뮤니티 리스트를 활성화 할수 있습니다. 커뮤니티 내부에서 새 컨테이너를 생성하거나 기존의 개인 컨테이너를 복사하여 불러올 수 있고 한 커뮤니티에서 복수의 컨테이너를 활용할 수 있습니다.
### STOMP
사용자가 접속하면 WebSocket을 연결한 후 토픽을 구독하여 여러 목적으로 사용합니다. 이를 통하여 1:N 채팅 기능, 실시간 접속중인 유저의 정보 확인, 터미널을 사용할 수 있습니다.
### 파일 시스템
유저, 컨테이너, 디렉토리, 파일 간의 연관관계를 설정하여 파일과 디렉토리의 CRUD를 구현하였고, 컨테이너의 최상위 디렉토리 내 전체 파일트리 구조를 제공합니다. 로컬 파일을 업로드 할 수 있습니다.

## 배포


<img src="https://i0.wp.com/us.wordcamp.org/2021/files/2021/09/AWS-Lightsail.png?fit=1200%2C436&ssl=1&w=640" width="250">
## AWS Lightsail

Amazon Linux2 Instance를 생성합니다. Gradle build를 활용해 jar파일을 생성하여 서버에서 nohup으로 실행하였습니다.
- Instance Size (1GB memory / 2vCPU processing / 40GB SSD storage / 2TB transfer)
- RDS는 별도 사용하지 않고, 인스턴스 내 MySQL 설치하여 사용하였습니다.
- FileZilla를 사용하여 인스턴스 내 파일 업로드를 진행하였습니다.



<img src="https://www.finops.org/wp-content/uploads/2022/11/Oracle_Cloud_rgb.png" width="250">
## Oracle Cloud Computing

Ubuntu Instance를 생성합니다. Amazon Lightsail과 동일하게 진행하였습니다.
- 평생 무료티어가 가능하지만, 서버가 자주 다운되고, 성능이 좋지 않아 배포 후 테스트 중 중단하였습니다.



<img src="https://github.com/every-ide/IDE-BACKEND/assets/133872161/53714d2b-e0df-4611-a29e-ca1523196dc8" width="250">
## Kakao Krampoline IDE

github 레포지토리의 코드를 D2Hub repository에 이미지를 빌드하여 Kargo를 통해 DKOS Cluster에 D2Hub 이미지를 배포합니다.
- FrontEnd Pod : Frontend App
- BackEnd Pod : Backend API Server
- Database Pod : MySQL
- Nginx Pod : Nginx Proxy Server

---

## 시연영상

[![시연 영상](https://img.youtube.com/vi/DGDVL_U37C8/0.jpg)](https://www.youtube.com/watch?v=DGDVL_U37C8 "시연 영상")
