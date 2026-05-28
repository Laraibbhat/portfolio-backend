# User Profile Flow Analysis and Corrections

## Executive Summary
The portfolio backend application has been reviewed and corrected to ensure **username-based** user profile retrieval and management throughout the system, instead of ID-based lookups.

---

## Current Application Flow

### 1. **User Profile Storage & Retrieval Flow**

#### CREATE PROFILE (POST /api/profiles)
```
Client Request (ProfileDTO with username)
    ↓
ProfileController.createProfile()
    ↓
ProfileService.createProfile()
    ├→ Maps ProfileDTO to User entity
    ├→ Sets username (unique identifier)
    ├→ Cascades and saves all related entities:
    │   ├→ TechnicalExpertise
    │   ├→ Experience (with ExperienceHighlights)
    │   ├→ Education (with EducationAchievements)
    │   ├→ Certifications
    │   ├→ Publications
    │   ├→ Awards
    │   └→ CoreCompetencies
    ↓
UserRepository.save(user) → Database
    ↓
Response: ProfileDTO with all saved data
```

#### GET PROFILE (GET /api/profiles/{username})
```
Client Request with username parameter
    ↓
ProfileController.getProfileByUsername(username)
    ↓
ProfileService.getProfileByUsername(username)
    ↓
UserRepository.findByUsername(username)  ← USERNAME-BASED LOOKUP ✓
    ↓
ProfileMapper.toProfileDTO(user)
    ├→ Maps User basic fields
    ├→ Maps technicalExpertise (Set<TechnicalExpertise> → Map<String, List<String>>)
    ├→ Maps experiences with highlights
    ├→ Maps educations with achievements
    ├→ Maps certifications, publications, awards
    └→ Maps core competencies
    ↓
Response: ProfileDTO
```

#### UPDATE PROFILE (PUT /api/profiles/{username})
```
Client Request with username parameter + ProfileDTO
    ↓
ProfileController.updateProfile(username, profileDTO)
    ↓
ProfileService.updateProfile(username, profileDTO)
    ↓
UserRepository.findByUsername(username)  ← USERNAME-BASED LOOKUP ✓
    ↓
Updates existing user with:
├→ Basic profile fields (name, email, title, etc.)
├→ Experience years (BigDecimal conversion)
├→ Technical expertise (clears old, adds new)
├→ Experiences with highlights
├→ Educations with achievements
├→ Certifications, publications, awards
└→ Core competencies
    ↓
UserRepository.save(updatedUser)
    ↓
Response: Updated ProfileDTO
```

#### DELETE PROFILE (DELETE /api/profiles/{username})
```
Client Request with username parameter
    ↓
ProfileController.deleteProfile(username)
    ↓
ProfileService.deleteProfile(username)
    ↓
UserRepository.findByUsername(username)  ← USERNAME-BASED LOOKUP ✓
    ↓
UserRepository.delete(user)
    ↓
All cascaded entities automatically deleted (orphanRemoval = true)
    ↓
Response: 204 No Content
```

---

### 2. **User Management Flow (Separate from Profile)**

#### CREATE USER (POST /api/users)
```
Client Request (UserRequestDTO with username)
    ↓
UserController.createUser(userRequestDTO)
    ↓
UserService.createUser(userRequestDTO)
    ↓
UserMapper.toEntity() → User entity
    ├→ Username (required, unique)
    └→ Other basic fields
    ↓
UserRepository.save(user)
    ↓
Response: UserDTO
```

#### GET USER BY ID (GET /api/users/{id})
```
Client Request with numeric ID
    ↓
UserController.getUserById(id)
    ↓
UserService.getUserById(id)
    ↓
UserRepository.findById(id)  ← ID-BASED LOOKUP (Kept for backward compatibility)
    ↓
Response: UserDTO
```

#### GET USER BY USERNAME (GET /api/users/username/{username})
```
Client Request with username
    ↓
UserController.getUserByUsername(username)
    ↓
UserService.getUserByUsername(username)
    ↓
UserRepository.findByUsername(username)  ← USERNAME-BASED LOOKUP ✓
    ↓
Response: UserDTO
```

#### UPDATE USER (PUT /api/users/username/{username}) - **CHANGED ✓**
```
BEFORE:  PUT /api/users/{id}  [ID-based]
AFTER:   PUT /api/users/username/{username}  [USERNAME-based]

Client Request with username + UserRequestDTO
    ↓
UserController.updateUserByUsername(username, userRequestDTO)
    ↓
UserService.updateUserByUsername(username, userRequestDTO)
    ↓
UserRepository.findByUsername(username)  ← USERNAME-BASED LOOKUP ✓
    ↓
UserMapper.updateEntityFromDto()
    ↓
UserRepository.save(updatedUser)
    ↓
Response: Updated UserDTO
```

#### DELETE USER (DELETE /api/users/username/{username}) - **CHANGED ✓**
```
BEFORE:  DELETE /api/users/{id}  [ID-based]
AFTER:   DELETE /api/users/username/{username}  [USERNAME-based]

Client Request with username
    ↓
UserController.deleteUserByUsername(username)
    ↓
UserService.deleteUserByUsername(username)
    ↓
UserRepository.findByUsername(username)  ← USERNAME-BASED LOOKUP ✓
    ↓
UserRepository.delete(user)
    ↓
Response: 204 No Content
```

---

## Key Issues Corrected

### Issue #1: Inconsistent User Lookup Strategy
**Problem:** UserController had mixed ID-based and username-based endpoints
```
PUT /api/users/{id}        ← ID-based (❌ Inconsistent)
DELETE /api/users/{id}     ← ID-based (❌ Inconsistent)
```

**Solution:** Changed to username-based endpoints
```
PUT /api/users/username/{username}        ← Username-based (✓ Consistent)
DELETE /api/users/username/{username}     ← Username-based (✓ Consistent)
```

### Issue #2: Service Layer Not Aligned with Controller
**Problem:** UserService had methods expecting ID parameter
```
updateUser(Integer id, ...)  ← ID-based
deleteUser(Integer id)       ← ID-based
```

**Solution:** Updated to use username
```
updateUserByUsername(String username, ...)  ← Username-based
deleteUserByUsername(String username)       ← Username-based
```

### Issue #3: Database Constraint Not Leveraged
**Problem:** User entity has `@Column(unique = true, nullable = false)` on username field, but it was not being used consistently

**Solution:** All lookups now use the unique `username` field instead of ID

---

## Database Entity Relationships

```
User (username is UNIQUE identifier for profile queries)
├── TechnicalExpertise (cascade: ALL, orphanRemoval: true)
├── Experience (cascade: ALL, orphanRemoval: true)
│   └── ExperienceHighlight
├── Education (cascade: ALL, orphanRemoval: true)
│   └── EducationAchievement
├── Certification (cascade: ALL, orphanRemoval: true)
├── Publication (cascade: ALL, orphanRemoval: true)
├── Award (cascade: ALL, orphanRemoval: true)
└── CoreCompetency (cascade: ALL, orphanRemoval: true)
```

---

## API Endpoints Summary

### Profile Management (Username-based) ✓
| Method | Endpoint | Purpose |
|--------|----------|---------|
| GET | `/api/profiles` | Get all profiles |
| GET | `/api/profiles/{username}` | Get profile by username |
| POST | `/api/profiles` | Create new profile |
| PUT | `/api/profiles/{username}` | Update profile by username |
| DELETE | `/api/profiles/{username}` | Delete profile by username |

### User Management (Mixed - ID for read, Username for write) ✓
| Method | Endpoint | Purpose |
|--------|----------|---------|
| GET | `/api/users` | Get all users |
| GET | `/api/users/{id}` | Get user by ID (backward compatible) |
| GET | `/api/users/username/{username}` | Get user by username |
| POST | `/api/users` | Create new user |
| PUT | `/api/users/username/{username}` | Update user by username ✓ CHANGED |
| DELETE | `/api/users/username/{username}` | Delete user by username ✓ CHANGED |

---

## Data Flow Examples

### Example 1: Create Complete Profile
```json
POST /api/profiles
{
  "username": "john_doe",
  "name": "John Doe",
  "title": "Senior Software Engineer",
  "email": "john@example.com",
  "experienceYears": 8.5,
  "technicalExpertise": {
    "Backend": ["Java", "Spring Boot"],
    "Frontend": ["React", "Angular"]
  },
  "experiences": [
    {
      "company": "Tech Corp",
      "position": "Senior Engineer",
      "highlights": ["Led team", "Improved performance"]
    }
  ]
}
```

**Flow:**
1. ProfileService receives ProfileDTO
2. Maps to User entity with username: "john_doe"
3. Creates related TechnicalExpertise records
4. Creates Experience with ExperienceHighlights
5. Saves everything in transaction
6. Returns complete ProfileDTO

### Example 2: Update User Profile by Username
```
PUT /api/users/username/john_doe
{
  "email": "newemail@example.com",
  "title": "Lead Engineer"
}
```

**Flow:**
1. UserController receives username: "john_doe"
2. Calls UserService.updateUserByUsername("john_doe", ...)
3. UserRepository.findByUsername("john_doe") ← **USERNAME lookup**
4. Updates email and title fields
5. Saves and returns updated UserDTO

---

## Mapper Conversions

### UserMapper (Basic User Data)
- `User` ↔ `UserDTO`
- `UserRequestDTO` → `User`
- Collections ignored (handled by service layer)

### ProfileMapper (Complete Profile with Related Entities)
- `User` → `ProfileDTO` (with nested DTOs)
- `ProfileDTO` → `User` (for creation)
- Custom mappings:
  - `BigDecimal` ↔ `Double` (experience years)
  - `Set<TechnicalExpertise>` → `Map<String, List<String>>`
  - `Set<Award>` → `List<String>`

---

## Cascade Behavior

All relationship cascades are set to `ALL` with `orphanRemoval = true`:
- When a User is deleted, all related records are automatically deleted
- When a collection is cleared and user is saved, orphaned records are removed
- This ensures data integrity and prevents orphaned records

---

## Testing Recommendations

1. **Test Profile Creation:**
   - Create profile with username "test_user"
   - Verify all nested entities are saved
   - Query by username to confirm

2. **Test Username-Based Updates:**
   - Update user via `PUT /api/users/username/test_user`
   - Verify email/title changed in database

3. **Test Username-Based Deletion:**
   - Delete user via `DELETE /api/users/username/test_user`
   - Verify user and all related records removed

4. **Test Cascade Deletes:**
   - Create profile with experiences
   - Delete profile
   - Verify experiences are automatically deleted

5. **Test Backward Compatibility:**
   - ID-based GET still works: `GET /api/users/{id}`
   - Ensure no breaking changes for read operations

---

## Summary of Changes

✅ **UserController:**
- Changed `PUT /api/users/{id}` → `PUT /api/users/username/{username}`
- Changed `DELETE /api/users/{id}` → `DELETE /api/users/username/{username}`

✅ **UserService:**
- Renamed `updateUser()` → `updateUserByUsername()`
- Renamed `deleteUser()` → `deleteUserByUsername()`
- Both methods now use `findByUsername()` instead of `findById()`

✅ **ProfileService:**
- Already correctly using username-based lookups (no changes needed)

✅ **ProfileController:**
- Already correctly using username-based endpoints (no changes needed)

✅ **Database Queries:**
- All profile operations use unique `username` field
- User operations now prefer `username` for write operations
- Read operations kept for backward compatibility

---

## Conclusion

The application now consistently uses **username** as the primary identifier for user profile storage and retrieval, while maintaining backward compatibility for ID-based read operations. All data flows through proper service and mapper layers with cascading relationships managed correctly.