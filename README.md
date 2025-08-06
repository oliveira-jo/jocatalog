
# Recursos para back end


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

## SQL para seed da base de dados de teste

```sql
INSERT INTO tb_category (name, created_At) VALUES ('Livros', NOW());
INSERT INTO tb_category (name, created_At) VALUES ('Eletrônicos', NOW());
INSERT INTO tb_category (name, created_At) VALUES ('Computadores', NOW());


````

