# User-Role Assignment Fix Verification Guide

## Problem Summary
The issue was that when registering a new user through `AuthService.registerNewUser()`, the relationship between the user and the ADMIN role was not being saved correctly in the `user_roles` table, despite the code attempting to assign this role.

## Root Cause Analysis
The problem was caused by:
1. **Missing JPA Cascade Settings**: The @ManyToMany relationship in the User entity lacked cascade settings, preventing automatic persistence of join table entries.
2. **Improper Set Initialization**: Using `Set.of(adminRole)` created an immutable set that couldn't be properly managed by JPA.
3. **Lombok Builder Issues**: The @Builder annotation was not properly handling default values for collections.

## Changes Made

### 1. User Entity (`User.java`)
- Added cascade settings: `cascade = {CascadeType.MERGE, CascadeType.PERSIST}`
- Added `@Builder.Default` to `enabled` field to fix Lombok warning
- Added `@Builder.Default` to `roles` field with `new HashSet<>()` initialization
- Imported `HashSet` for proper collection initialization

### 2. AuthService (`AuthService.java`)
- Changed role assignment strategy from `Set.of(adminRole)` to explicit HashSet initialization
- Added explicit null check and HashSet initialization for roles
- Enhanced logging throughout the registration process
- Added import for `HashSet`

### 3. InitData (`InitData.java`)
- Enhanced with detailed logging for role initialization tracking
- Added individual role creation logging with IDs

## Expected Behavior After Fix
1. When `registerNewUser()` is called, the ADMIN role should be properly assigned to the user
2. The `user_roles` table should contain an entry linking the user ID and ADMIN role ID
3. When querying the user from the database, the roles collection should contain the ADMIN role
4. The login process should properly retrieve user roles for JWT token generation

## Manual Verification Steps

### Step 1: Start the Application
1. Ensure MySQL is running with the `medical_auth` database
2. Start the Auth-Service application
3. Check logs for role initialization messages:
   ```
   INFO c.l.A.config.InitData : Initializing roles in database...
   INFO c.l.A.config.InitData : Created ADMIN role with ID: 1
   ```

### Step 2: Register a New User
Make a POST request to the registration endpoint:
```bash
curl -X POST http://localhost:8081/api/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin-test",
    "email": "admin@test.com", 
    "password": "password123",
    "clinicName": "Test Clinic"
  }'
```

### Step 3: Check Application Logs
Look for these log messages indicating successful role assignment:
```
INFO c.l.A.service.AuthService : Starting user registration for username: admin-test
INFO c.l.A.service.AuthService : ADMIN role found with ID: 1, Name: ADMIN
INFO c.l.A.service.AuthService : User object created. Roles assigned: [ADMIN]
INFO c.l.A.service.AuthService : User saved with ID: 1
INFO c.l.A.service.AuthService : User roles after save: [ADMIN]
```

### Step 4: Verify Database Entries
Check the database tables:

1. **roles table**:
   ```sql
   SELECT * FROM roles WHERE name = 'ADMIN';
   ```
   Should return: `id=1, name='ADMIN', description='Administrador de consultorio'`

2. **users table**:
   ```sql
   SELECT * FROM users WHERE username = 'admin-test';
   ```
   Should return the created user with `enabled=true`

3. **user_roles table** (This is the critical fix):
   ```sql
   SELECT * FROM user_roles WHERE user_id = 1 AND role_id = 1;
   ```
   Should return an entry linking user and role

### Step 5: Test Login Functionality
```bash
curl -X POST http://localhost:8081/api/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin-test",
    "password": "password123"
  }'
```

Should return a JWT token containing the ADMIN role.

## Troubleshooting

If the fix doesn't work:
1. Check application logs for any ERROR messages during user creation
2. Verify that the roles were properly initialized at startup
3. Check database constraints and foreign key relationships
4. Ensure the database user has INSERT permissions on the `user_roles` table

## Technical Details

The key changes ensure that:
- JPA properly cascades the relationship persistence
- The roles collection is mutable and properly initialized
- Hibernate can manage the bidirectional relationship correctly
- Transaction boundaries are respected for all related entity saves