# User Profile API Quick Reference Guide

## Changes Made Summary

Your application has been updated to use **username-based** lookups for user profile management instead of ID-based lookups. This ensures consistency across the system since username is marked as `unique` in the database.

---

## API Endpoints - Before & After

### User Management API

#### 1. Create User
```
Method: POST
Endpoint: /api/users
Request Body:
{
  "username": "john_doe",
  "name": "John Doe",
  "email": "john@example.com",
  "title": "Software Engineer"
}
Response: UserDTO with id, username, and all fields
```

#### 2. Get User by ID (Read-Only - Backward Compatible)
```
Method: GET
Endpoint: /api/users/{id}
Example: /api/users/1
Response: UserDTO
```

#### 3. Get User by Username
```
Method: GET
Endpoint: /api/users/username/{username}
Example: /api/users/username/john_doe
Response: UserDTO
```

#### 4. Update User by Username ⭐ CHANGED
```
BEFORE: PUT /api/users/{id}
AFTER:  PUT /api/users/username/{username}

Method: PUT
Endpoint: /api/users/username/{username}
Example: /api/users/username/john_doe
Request Body:
{
  "email": "newemail@example.com",
  "title": "Senior Engineer"
}
Response: Updated UserDTO
```

#### 5. Delete User by Username ⭐ CHANGED
```
BEFORE: DELETE /api/users/{id}
AFTER:  DELETE /api/users/username/{username}

Method: DELETE
Endpoint: /api/users/username/{username}
Example: /api/users/username/john_doe
Response: 204 No Content
```

---

### Profile Management API

#### 1. Get All Profiles
```
Method: GET
Endpoint: /api/profiles
Response: List<ProfileDTO>
```

#### 2. Get Profile by Username
```
Method: GET
Endpoint: /api/profiles/{username}
Example: /api/profiles/john_doe
Response: ProfileDTO with all nested entities
```

#### 3. Create Profile
```
Method: POST
Endpoint: /api/profiles
Request Body:
{
  "username": "john_doe",
  "name": "John Doe",
  "email": "john@example.com",
  "title": "Senior Engineer",
  "experienceYears": 8.5,
  "yearsAsSenior": 3.0,
  "location": "San Francisco",
  "summary": "Expert in Java and Cloud",
  "technicalExpertise": {
    "Backend": ["Java", "Spring Boot", "Node.js"],
    "Frontend": ["React", "Angular"],
    "Cloud": ["AWS", "GCP"]
  },
  "experiences": [
    {
      "company": "Tech Corp",
      "position": "Senior Engineer",
      "startDate": "2020-01-01",
      "endDate": "2023-12-31",
      "description": "Led backend development",
      "highlights": ["Led team of 5", "Improved performance by 40%"]
    }
  ],
  "educations": [
    {
      "institution": "MIT",
      "degree": "BS",
      "fieldOfStudy": "Computer Science",
      "startDate": "2010",
      "endDate": "2014",
      "achievements": ["Cum Laude", "Dean's List"]
    }
  ],
  "certifications": [
    {
      "name": "AWS Solutions Architect",
      "issuer": "Amazon",
      "issueDate": "2021-05-15"
    }
  ],
  "publications": [
    {
      "title": "Microservices Best Practices",
      "publisher": "Tech Journal",
      "publicationDate": "2022-03-15"
    }
  ],
  "awards": [
    "Employee of the Year 2022",
    "Innovation Award 2021"
  ],
  "coreCompetencies": [
    {
      "competency": "System Design"
    },
    {
      "competency": "Team Leadership"
    }
  ]
}
Response: Created ProfileDTO with id and username
```

#### 4. Update Profile by Username
```
Method: PUT
Endpoint: /api/profiles/{username}
Example: /api/profiles/john_doe
Request Body: Complete or partial ProfileDTO
Response: Updated ProfileDTO
```

#### 5. Delete Profile by Username
```
Method: DELETE
Endpoint: /api/profiles/{username}
Example: /api/profiles/john_doe
Response: 204 No Content
Cascade: All related entities are automatically deleted
```

---

## Code Implementation Examples

### Java/Spring Client

```java
// Creating a profile
RestTemplate restTemplate = new RestTemplate();
ProfileDTO profileDTO = new ProfileDTO();
profileDTO.setUsername("john_doe");
profileDTO.setName("John Doe");
profileDTO.setEmail("john@example.com");
// ... set other fields

ProfileDTO created = restTemplate.postForObject(
    "http://localhost:8080/api/profiles",
    profileDTO,
    ProfileDTO.class
);

// Getting a profile by username
ProfileDTO profile = restTemplate.getForObject(
    "http://localhost:8080/api/profiles/john_doe",
    ProfileDTO.class
);

// Updating a profile by username
restTemplate.put(
    "http://localhost:8080/api/profiles/john_doe",
    profileDTO
);

// Deleting a profile by username
restTemplate.delete("http://localhost:8080/api/profiles/john_doe");
```

### JavaScript/Fetch

```javascript
// Creating a profile
const profileData = {
  username: "john_doe",
  name: "John Doe",
  email: "john@example.com"
};

fetch('http://localhost:8080/api/profiles', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify(profileData)
})
.then(response => response.json())
.then(data => console.log('Profile created:', data));

// Getting a profile by username
fetch('http://localhost:8080/api/profiles/john_doe')
  .then(response => response.json())
  .then(data => console.log('Profile:', data));

// Updating a profile
fetch('http://localhost:8080/api/profiles/john_doe', {
  method: 'PUT',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify(updatedProfileData)
})
.then(response => response.json())
.then(data => console.log('Profile updated:', data));

// Deleting a profile
fetch('http://localhost:8080/api/profiles/john_doe', {
  method: 'DELETE'
})
.then(() => console.log('Profile deleted'));
```

### cURL Examples

```bash
# Create profile
curl -X POST http://localhost:8080/api/profiles \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "name": "John Doe",
    "email": "john@example.com"
  }'

# Get profile by username
curl http://localhost:8080/api/profiles/john_doe

# Update profile by username
curl -X PUT http://localhost:8080/api/profiles/john_doe \
  -H "Content-Type: application/json" \
  -d '{
    "email": "newemail@example.com"
  }'

# Delete profile by username
curl -X DELETE http://localhost:8080/api/profiles/john_doe

# Update user by username (USER API)
curl -X PUT http://localhost:8080/api/users/username/john_doe \
  -H "Content-Type: application/json" \
  -d '{
    "email": "newemail@example.com",
    "title": "Senior Engineer"
  }'

# Delete user by username (USER API)
curl -X DELETE http://localhost:8080/api/users/username/john_doe
```

---

## Key Points to Remember

1. **Username is UNIQUE**: The username field in the User entity is marked as unique and not null, making it perfect for lookups

2. **Profile vs User**:
   - **Profile API** (`/api/profiles`): Complete profile with all nested entities
   - **User API** (`/api/users`): Just the user basic information

3. **Cascade Operations**: When you delete a profile, all related records are automatically deleted:
   - Technical Expertise
   - Experiences and Experience Highlights
   - Educations and Education Achievements
   - Certifications
   - Publications
   - Awards
   - Core Competencies

4. **Username-Based Operations**:
   - Create: Still uses POST with username in body
   - Read: Can use both ID (`/api/users/{id}`) or username (`/api/users/username/{username}`)
   - Update: Now uses username (`/api/users/username/{username}`)
   - Delete: Now uses username (`/api/users/username/{username}`)

5. **Backward Compatibility**: The ID-based read endpoint (`GET /api/users/{id}`) is still available for backward compatibility

---

## Database Constraints

```sql
-- Username is unique and not null
CREATE TABLE users (
  id INT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(50) UNIQUE NOT NULL,
  name VARCHAR(100) NOT NULL,
  email VARCHAR(100),
  ...
);
```

This ensures that every username lookup is efficient and guaranteed to return at most one result.

---

## Error Responses

### Profile Not Found
```
Status: 404
Body: {
  "message": "Profile not found with username: invalid_user"
}
```

### User Not Found
```
Status: 404
Body: {
  "message": "User not found with username: invalid_user"
}
```

### Validation Error
```
Status: 400
Body: {
  "message": "Username cannot be empty"
}
```

---

## Migration Notes for Frontend

If you have existing frontend code using the old endpoints:

**Old Endpoints** → **New Endpoints**
- `PUT /api/users/{id}` → `PUT /api/users/username/{username}`
- `DELETE /api/users/{id}` → `DELETE /api/users/username/{username}`

Update all calls to these endpoints to pass username instead of numeric ID.

---

## Testing Checklist

- [ ] Create a new profile with username
- [ ] Retrieve profile by username
- [ ] Update profile by username
- [ ] Delete profile by username (verify cascade delete)
- [ ] Get user by username
- [ ] Update user by username
- [ ] Delete user by username
- [ ] Verify ID-based GET still works (backward compatibility)