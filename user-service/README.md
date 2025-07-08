# User Service

User Service for the Luminia Clinical Management System. This microservice handles user management, role-based access control, and integration with the Auth Service.

## Features

- **User Management**: Create, update, retrieve, and delete users
- **Role-Based Access Control**: Support for ADMIN, DOCTOR, NURSE, RECEPTIONIST, and ASSISTANT roles
- **Medical Office Access**: Manage user access to specific medical offices within clinics
- **Integration with Auth Service**: JWT-based authentication and authorization
- **Spring Cloud Integration**: Service discovery with Eureka and centralized configuration
- **MySQL Database**: Persistent storage with JPA/Hibernate
- **Security**: Method-level security with role-based permissions
- **Validation**: Input validation with custom error handling
- **Docker Support**: Containerized deployment

## API Endpoints

### User Management
- `POST /api/users` - Create a new user (ADMIN only)
- `PUT /api/users/{id}` - Update user (ADMIN or own profile)
- `GET /api/users/{id}` - Get user by ID
- `GET /api/users/username/{username}` - Get user by username
- `GET /api/users/email/{email}` - Get user by email
- `GET /api/users` - Get all users with pagination (ADMIN/DOCTOR)
- `GET /api/users/search?q={query}` - Search users (ADMIN/DOCTOR)
- `DELETE /api/users/{id}` - Delete user (ADMIN only)

### User Status Management
- `PATCH /api/users/{id}/enable` - Enable user (ADMIN only)
- `PATCH /api/users/{id}/disable` - Disable user (ADMIN only)  
- `PATCH /api/users/{id}/lock` - Lock user account (ADMIN only)
- `PATCH /api/users/{id}/unlock` - Unlock user account (ADMIN only)

### Role Management
- `POST /api/users/{id}/roles` - Assign roles to user (ADMIN only)
- `DELETE /api/users/{id}/roles` - Remove roles from user (ADMIN only)

### Medical Office Access
- `POST /api/users/{id}/offices` - Assign office access (ADMIN/DOCTOR)
- `DELETE /api/users/{id}/offices` - Remove office access (ADMIN/DOCTOR)

### Filtering Endpoints
- `GET /api/users/clinic/{clinicId}` - Get users by clinic (ADMIN/DOCTOR)
- `GET /api/users/role/{roleName}` - Get users by role (ADMIN only)
- `GET /api/users/office/{officeId}` - Get users by office access (ADMIN/DOCTOR)

## Configuration

### Environment Variables
- `DB_PASSWORD` - MySQL database password
- `JWT_SECRET` - JWT secret key for token validation

### Application Properties
The service uses Spring Cloud Config for centralized configuration. Key configurations:
- Database connection to MySQL
- Eureka service discovery
- JWT authentication settings
- Server port: 8082

## Running the Service

### Local Development
```bash
mvn spring-boot:run
```

### Docker
```bash
docker build -t user-service .
docker run -p 8082:8082 user-service
```

### Prerequisites
- Java 17+
- Maven 3.6+
- MySQL 8.0+
- Eureka Server (for service discovery)
- Config Server (for configuration management)

## Testing

Run unit and integration tests:
```bash
mvn test
```

## Database Schema

The service manages the following entities:
- **Users**: Core user information with authentication details
- **Roles**: System roles for access control
- **Clinics**: Medical clinic/practice information
- **Medical Offices**: Specific office locations within clinics
- **User-Role mappings**: Many-to-many relationship for role assignments
- **User-Office Access**: Many-to-many relationship for office-specific permissions

## Security

- JWT-based authentication with tokens issued by Auth Service
- Method-level security with role-based access control
- CORS configuration for cross-origin requests
- Input validation and sanitization
- Secure password handling (managed by Auth Service)

## Architecture

This service follows microservices architecture patterns:
- **Service Layer**: Business logic implementation
- **Repository Layer**: Data access with Spring Data JPA
- **Controller Layer**: REST API endpoints
- **Configuration Layer**: Security, validation, and cloud integration
- **Exception Handling**: Global exception handler with standardized error responses