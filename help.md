# Quantity Measurement App - UC17

Spring Boot REST and JPA implementation for the Quantity Measurement application.

## Build

```powershell
.\mvnw.cmd clean test
```

## Run

```powershell
.\mvnw.cmd spring-boot:run
```

## Useful URLs

- REST API: `http://localhost:8080/api/v1/quantities`
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/api-docs`
- H2 Console: `http://localhost:8080/h2-console`
- Actuator health: `http://localhost:8080/actuator/health`

## Example Request

```powershell
Invoke-RestMethod `
  -Uri "http://localhost:8080/api/v1/quantities/compare" `
  -Method Post `
  -ContentType "application/json" `
  -Body '{"first":{"value":1,"unit":"FEET","category":"length"},"second":{"value":12,"unit":"INCHES","category":"length"}}'
```
