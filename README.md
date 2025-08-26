
# Recursos para back end

## Introduction
This is a project of a simple catalog CRUD and management, that was implemented to have as a base and example of backend resourses, like: code exemples, configurations, dev profiles, ORM, database config, project structure, and others. 

## Project Model
![Project Model](/backend/assets/model-jo-catalog.png)

## Arquivos de configuração

#### application.properties
```
spring.profiles.active=test
spring.jpa.open-in-view=false
```

#### application-test.properties
```
# H2 connection
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.username=sa
spring.datasource.password=
# H2 client
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
# Show sql
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

#### application-dev.properties
```
#spring.jpa.properties.jakarta.persistence.schema-generation.create-source=metadata
#spring.jpa.properties.jakarta.persistence.schema-generation.scripts.action=create
#spring.jpa.properties.jakarta.persistence.schema-generation.scripts.create-target=create.sql
#spring.jpa.properties.hibernate.hbm2ddl.delimiter=;

spring.datasource.url=jdbc:postgresql://localhost:5432/dscatalog
spring.datasource.username=postgres
spring.datasource.password=1234567

spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.properties.hibernate.jdbc.lob.non_contextual_creation=true
spring.jpa.hibernate.ddl-auto=none
```

#### application-prod.properties
```
spring.datasource.url=${DATABASE_URL}

spring.jpa.hibernate.ddl-auto=none
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=false
```

## Parâmetros de paginação
```java
@RequestParam(value = "page", defaultValue = "0") Integer page,
@RequestParam(value = "linesPerPage", defaultValue = "12") Integer linesPerPage,
@RequestParam(value = "orderBy", defaultValue = "moment") String orderBy,
@RequestParam(value = "direction", defaultValue = "DESC") String direction
```

## Objetos JSON para inserir e atualizar um produto

### Inserir
```javascript
{
  "name": "PS5",
  "description": "The new generation PS5 video game",
  "price": 600.0,
  "imgUrl": "",
  "date": "2020-07-20T10:00:00Z",
  "categories": [
    {
      "id": 1
    },
    {
      "id": 3
    }
  ]
}
```

### Atualizar
```javascript
{
  "name": "Updated product name",
  "description": "Updated product description",
  "price": 600.0,
  "imgUrl": "",
  "date": "2020-07-20T10:00:00Z",
  "categories": [
    {
      "id": 1
    },
    {
      "id": 3
    }
  ]
}
```

### SEED user and role
````
INSERT INTO tb_user (first_name, last_name, email, password) VALUES ('Alex', 'Brown', 'alex@gmail.com', '$2a$10$eACCYoNOHEqXve8aIWT8Nu3PkMXWBaOxJ9aORUYzfMQCbVBIhZ8tG');
INSERT INTO tb_user (first_name, last_name, email, password) VALUES ('Maria', 'Green', 'maria@gmail.com', '$2a$10$eACCYoNOHEqXve8aIWT8Nu3PkMXWBaOxJ9aORUYzfMQCbVBIhZ8tG');

INSERT INTO tb_role (authority) VALUES ('ROLE_OPERATOR');
INSERT INTO tb_role (authority) VALUES ('ROLE_ADMIN');

INSERT INTO tb_user_role (user_id, role_id) VALUES (1, 1);
INSERT INTO tb_user_role (user_id, role_id) VALUES (2, 1);
INSERT INTO tb_user_role (user_id, role_id) VALUES (2, 2);
````

