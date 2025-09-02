
# Recursos para back end

## Introduction
This is a project of a simple catalog CRUD and management, that was implemented to have as a base and example of backend resourses, like: code exemples, configurations, dev profiles, ORM, database config, project structure, and others. 

## Project Model
![Project Model](/backend/assets/model-jo-catalog.png)

# Resourses back end

## Pagination params

```java
@RequestParam(value = "page", defaultValue = "0") Integer page,
@RequestParam(value = "linesPerPage", defaultValue = "12") Integer linesPerPage,
@RequestParam(value = "orderBy", defaultValue = "moment") String orderBy,
@RequestParam(value = "direction", defaultValue = "DESC") String direction)
```

## JSON product insert and update

### Insert

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

### Update

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

## Provisional configuration to release all endpoints
```java
@Configuration
public class SecurityConfig {

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable());
		http.authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
		return http.build();
	}
}
```

## ConstraintValidator Custom
#### Annotation
```java
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = UserInsertValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)

public @interface UserInsertValid {
  String message() default "Validation error";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
```
#### Validator
```java
import java.util.ArrayList;
import java.util.List;

import com.devjoliveira.jocatalog.controllers.exceptions.FieldMessage;
import com.devjoliveira.jocatalog.dtos.UserDTO;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UserInsertValidator implements ConstraintValidator<UserInsertValid, UserDTO> {
  @Autowired
  private UserRepository repository;

  @Override
  public void initialize(UserInsertValid ann) {
  }

  @Override
  public boolean isValid(UserDTO dto, ConstraintValidatorContext context) {

    List<FieldMessage> list = new ArrayList<>();

		// TESTS and add to list case exists
    if (repository.findByEmail(dto.email()).isPresent()) {
      list.add(new FieldMessage("email", "Email already exists"));
    }

    for (FieldMessage e : list) {
      context.disableDefaultConstraintViolation();
      context.buildConstraintViolationWithTemplate(e.message()).addPropertyNode(e.message())
          .addConstraintViolation();
    }
    return list.isEmpty();
  }
}
```

## Configuration files

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

## Checklist Spring Security
![DomainSpringSecurity](/backend/assets/spring-security-model.png)


## Heroku

#### Prepar the project for implementation
- In root project file, create the file system.properties with:
java.runtime.version=11
- Change the profile for "prod" and save with a new commit

#### Implements the system on Heroku
- Create app on Heroku and provision the Postgresql

- Get the connection string 
- Pegar a string de conexão to the database
- Instantiate a server in your pgAdmin with the connection data
- Run the database creation script
- in terminal:
```
heroku git:remote -a <nome-do-app>
git remote -v
git subtree push --prefix backend heroku main
```

## Postman

Vars:
- host: http://localhost:8080
- client-id: dscatalog
- client-secret: dscatalog123
- username: maria@gmail.com
- password: 123456
- token: 

Script to assign token to Postman environment variable:
```js
if (responseCode.code >= 200 && responseCode.code < 300) {
    var json = JSON.parse(responseBody);
    postman.setEnvironmentVariable('token', json.access_token);
}
```

## Beans for CORS config
```java
@Bean
public CorsConfigurationSource corsConfigurationSource() {
	CorsConfiguration corsConfig = new CorsConfiguration();
	corsConfig.setAllowedOriginPatterns(Arrays.asList("*"));
	corsConfig.setAllowedMethods(Arrays.asList("POST", "GET", "PUT", "DELETE", "PATCH"));
	corsConfig.setAllowCredentials(true);
	corsConfig.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));

	UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	source.registerCorsConfiguration("/**", corsConfig);
	return source;
}

@Bean
public FilterRegistrationBean<CorsFilter> corsFilter() {
	FilterRegistrationBean<CorsFilter> bean 
		= new FilterRegistrationBean<>(new CorsFilter(corsConfigurationSource()));
	bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
	return bean;
}	
```

## CORS Local test

```js
fetch("https://seuprojeto.herokuapp.com", {
  "headers": {
    "accept": "*/*",
    "accept-language": "en-US,en;q=0.9,pt-BR;q=0.8,pt;q=0.7",
    "sec-fetch-dest": "empty",
    "sec-fetch-mode": "cors",
    "sec-fetch-site": "cross-site"
  },
  "referrer": "http://localhost:3000",
  "referrerPolicy": "no-referrer-when-downgrade",
  "body": null,
  "method": "GET",
  "mode": "cors",
  "credentials": "omit"
});
```

## Swagger

#### Dependências Maven
```xml
<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-swagger2</artifactId>
    <version>2.9.2</version>
</dependency>

<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-swagger-ui</artifactId>
    <version>2.9.2</version>
</dependency>
```

#### Configuração class
```java
@Configuration
@EnableSwagger2
public class SwaggerConfig {
	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.withClassAnnotation(RestController.class)).paths(PathSelectors.any())
				.build();
	}
}
```

#### Additional configuration to allow access to Swagger
```java
web.ignoring().antMatchers("/v2/api-docs", "/configuration/ui", "/swagger-resources/**", "/configuration/**", "/swagger-ui.html", "/webjars/**");
```

## Releasing public readability on the S3 bucket

```
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Sid": "PublicReadGetObject",
            "Effect": "Allow",
            "Principal": "*",
            "Action": "s3:GetObject",
            "Resource": "arn:aws:s3:::seu-bucket/*"
        }
    ]
}
```

## CRUD Test Class Structure with MockMvc

```java
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.services.ProductService;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dscatalog.tests.factory.ProductFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductResourceTests {

	@Autowired
	private MockMvc mockMvc;

	@Value("${security.oauth2.client.client-id}")
	private String clientId;
	
	@Value("${security.oauth2.client.client-secret}")
	private String clientSecret;
	
	private String obtainAccessToken(String username, String password) throws Exception {
		 
	    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
	    params.add("grant_type", "password");
	    params.add("client_id", clientId);
	    params.add("username", username);
	    params.add("password", password);
	 
	    ResultActions result 
	    	= mockMvc.perform(post("/oauth/token")
	    		.params(params)
	    		.with(httpBasic(clientId, clientSecret))
	    		.accept("application/json;charset=UTF-8"))
	        	.andExpect(status().isOk())
	        	.andExpect(content().contentType("application/json;charset=UTF-8"));
	 
	    String resultString = result.andReturn().getResponse().getContentAsString();
	 
	    JacksonJsonParser jsonParser = new JacksonJsonParser();
	    return jsonParser.parseMap(resultString).get("access_token").toString();
	}	
}
```

## Basic environment variables after adding security

```
spring.profiles.active=${APP_PROFILE:test}

spring.jpa.open-in-view=false

security.oauth2.client.client-id=${CLIENT_ID:dscatalog}
security.oauth2.client.client-secret=${CLIENT_SECRET:dscatalog123}

jwt.secret=${JWT_SECRET:MY-JWT-SECRET}
jwt.duration=${JWT_DURATION:86400}
```