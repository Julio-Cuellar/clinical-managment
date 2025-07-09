# Authentication & User Services Architecture

This document describes the refactored authentication architecture for the Clinical Management system, including improved JWT handling, service-to-service communication, and OAuth2 security.

## Architecture Overview

### Auth-Service (Port 8081)
- **Responsibility**: Authentication, token generation, and user validation
- **Key Features**:
  - JWT token generation with roles
  - User authentication (login/register/logout)
  - Token validation endpoints for other services
  - Blacklist management for invalidated tokens

### User-Service (Port 8082)
- **Responsibility**: User management and profile operations
- **Key Features**:
  - User CRUD operations
  - Role and office management
  - Integration with Auth-Service for token validation
  - Automatic token forwarding to Auth-Service

## Key Improvements

### 1. Consistent JWT Handling
- Both services now use JWT library version 0.12.6
- Standardized secret key handling using `Keys.hmacShaKeyFor()`
- Consistent token parsing and validation logic

### 2. Service-to-Service Communication
- **FeignClient**: User-Service communicates with Auth-Service via Feign
- **Automatic Token Forwarding**: JWT tokens are automatically forwarded in inter-service calls
- **Validation Endpoints**: Auth-Service provides dedicated endpoints for user validation

### 3. OAuth2 Ready Architecture
- Both services include OAuth2 resource server dependencies
- Prepared for future client credentials flow implementation
- Enhanced security configuration

## API Endpoints

### Auth-Service Endpoints

#### Public Endpoints
- `POST /auth/register` - User registration
- `POST /auth/login` - User authentication
- `POST /auth/logout` - User logout (requires token)

#### Protected Endpoints (require valid JWT)
- `GET /api/validate/{username}` - Validate user with token
- `GET /api/user/{username}/roles` - Get user roles

### User-Service Endpoints

#### Protected Endpoints (require valid JWT)
- `GET /api/auth/test` - Test authentication and Auth-Service integration
- `GET /api/auth/validate/{username}` - Admin endpoint to validate specific users

## Configuration

### Environment Variables Required

Both services require these environment variables:

```bash
# Database
DB_PASSWORD=your_mysql_password

# JWT Configuration
JWT_SECRET=YourSecretKeyForJWTTokens123456789  # Must be same for both services

# Auth-Service specific
SPRING_SECURITY_PASSWORD=admin_password

# Optional: Config Server (if using centralized config)
CONFIG_SERVER_URL=http://localhost:8888
```

### Application Properties

#### Auth-Service (application.yml)
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/medical_auth?createDatabaseIfNotExist=true
    username: root
    password: ${DB_PASSWORD}
  jpa:
    hibernate:
      ddl-auto: update

server:
  port: 8081

jwt:
  secret: ${JWT_SECRET}
  expiration-ms: 86400000  # 24 hours
```

#### User-Service (application.yml)
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/medical_users?createDatabaseIfNotExist=true
    username: root
    password: ${DB_PASSWORD}
  jpa:
    hibernate:
      ddl-auto: update

server:
  port: 8082

jwt:
  secret: ${JWT_SECRET}

auth:
  service:
    url: http://localhost:8081
```

## Setup and Testing

### Prerequisites
1. Java 17 or higher
2. Maven 3.6+
3. MySQL 8.0+
4. Environment variables configured

### Starting the Services

1. **Start Auth-Service**:
   ```bash
   cd Auth-Service
   mvn spring-boot:run
   ```

2. **Start User-Service**:
   ```bash
   cd user-service
   mvn spring-boot:run
   ```

### Testing the Integration

1. **Register a user** (Auth-Service):
   ```bash
   curl -X POST http://localhost:8081/auth/register \
   -H "Content-Type: application/json" \
   -d '{
     "username": "admin",
     "email": "admin@example.com",
     "password": "password123",
     "clinicName": "Test Clinic"
   }'
   ```

2. **Login to get JWT token**:
   ```bash
   curl -X POST http://localhost:8081/auth/login \
   -H "Content-Type: application/json" \
   -d '{
     "username": "admin",
     "password": "password123"
   }'
   ```

3. **Test User-Service with token**:
   ```bash
   curl -X GET http://localhost:8082/api/auth/test \
   -H "Authorization: Bearer YOUR_JWT_TOKEN"
   ```

4. **Test validation endpoint**:
   ```bash
   curl -X GET http://localhost:8081/api/validate/admin \
   -H "Authorization: Bearer YOUR_JWT_TOKEN"
   ```

## Security Features

### JWT Token Structure
- **Header**: Algorithm and token type
- **Payload**: Username, roles, issued date, expiration
- **Signature**: HMAC SHA-256 with secret key

### Token Validation Flow
1. User-Service receives request with JWT token
2. JwtAuthenticationFilter validates token locally
3. For additional validation, User-Service calls Auth-Service validation endpoint
4. Auth-Service checks token validity and user status
5. Response indicates if user/token is valid

### Blacklist Management
- Logout invalidates tokens by adding them to blacklist
- All services check blacklist before accepting tokens
- Blacklisted tokens are rejected immediately

## Development Notes

### Code Organization
- **Auth-Service**: `com.luminia.Auth_Service`
- **User-Service**: `com.luminia.clinical.userservice`

### Key Classes
- `JwtUtil`: JWT token handling (both services)
- `JwtAuthenticationFilter`: Request authentication filtering
- `ValidationController`: Auth-Service validation endpoints
- `AuthValidationService`: User-Service integration with Auth-Service
- `FeignConfig`: Automatic token forwarding configuration

### Database Setup
The services will automatically create their databases if they don't exist:
- `medical_auth` - Auth-Service database
- `medical_users` - User-Service database

## Troubleshooting

### Common Issues

1. **Compilation Error: Java version**
   - Ensure Java 17 is installed and JAVA_HOME is set correctly

2. **Database Connection**
   - Verify MySQL is running
   - Check DB_PASSWORD environment variable
   - Ensure databases are created

3. **JWT Secret Mismatch**
   - Both services must use the same JWT_SECRET
   - Secret must be at least 256 bits (32 characters)

4. **Service Communication**
   - Verify both services are running on correct ports
   - Check auth.service.url configuration in User-Service

### Health Checks
- Auth-Service: `http://localhost:8081/actuator/health`
- User-Service: `http://localhost:8082/actuator/health`

## Future Enhancements

1. **OAuth2 Client Credentials**: Implement service-to-service authentication
2. **Rate Limiting**: Add rate limiting for authentication endpoints
3. **Token Refresh**: Implement refresh token mechanism
4. **Audit Logging**: Add comprehensive audit trails
5. **Metrics**: Add detailed metrics and monitoring