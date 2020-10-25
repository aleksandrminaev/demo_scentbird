Demo application for scentbird Inc. ==> java14 + mvn 3.6.3

curl --location --request GET 'http://localhost:8080/covid/total/countries?countries=russia,canada&from=2020-04-04&to=2020-04-05'

- mvn spring-boot:build-image -f pom.xml
- docker build -t demo .
- docker-compose up