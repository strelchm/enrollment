# enrollment
Market API service. OpenAPI UI is avaliable here: http://10.20.1.118/swagger-ui/index.html

Stack
===================
* Java >= 11
* Docker
* Spring (boot, validation, data JPA)
* Hikari connection pool
* Hibernate (Criteria API queries, batch insert/updates)
* PostgreSQL
* Lombok
* OpenAPI
* MapStruct
* Python API test

Deploy
===================
1. JAR build: `$ gradle clean build`
2. Container build: `$ sudo docker-compose build`
3. Container start (auto restart with system reset): `$ sudo docker-compose start`
