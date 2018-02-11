# hero-ability API

### Run:

```
./mvnw spring-boot:run
```

or

```
java -jar hero-ability.jar
```

or from your favorite IDE (Lombok plugin installation recommended for comfortable playing with the code)

### Usage:

Open [http://localhost:8080/api/](http://localhost:8080/api/) in [Postman](http://getpostman.com), [Insomnia](http://insomnia.rest), `curl`, or [httpie](http://httpie.org).

List of endpoints:

[http://localhost:8080/api/heros](http://localhost:8080/api/heros) - hero list
[http://localhost:8080/api/heros/{hero_id}](http://localhost:8080/api/heros/{hero_id}) - hero data
[http://localhost:8080/api/heros/{hero_id}/abilities](http://localhost:8080/api/heros/{hero_id}/abilities) - hero ability list
[http://localhost:8080/api/abilities](http://localhost:8080/api/abilities) - ability list
[http://localhost:8080/api/abilities/{ability_id}](http://localhost:8080/api/abilities/{ability_id}) - ability data

### Auto-generated description:

+ WADL: [http://localhost:8080/api/application.wadl](http://localhost:8080/api/application.wadl)
+ JSON Swagger: [http://localhost:8080/api/swagger.json](http://localhost:8080/api/swagger.json)
+ JSON Swagger: [http://localhost:8080/api/swagger.yaml](http://localhost:8080/api/swagger.yaml)
