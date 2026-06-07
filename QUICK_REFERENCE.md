# Profile Picture Upload - Quick Reference

## 🎯 Quick Setup (5 minutes)

### 1. AWS Setup
```bash
# Create bucket: portfolio-avatars
# Create IAM user: portfolio-backend-user
# Copy Access Key and Secret Key
```

### 2. Environment Variables
```bash
export AWS_ACCESS_KEY_ID=your_access_key
export AWS_SECRET_ACCESS_KEY=your_secret_key
```

### 3. Database
```sql
ALTER TABLE users ADD COLUMN avatar_key VARCHAR(512) AFTER location;
```

### 4. Run
```bash
mvn clean install && mvn spring-boot:run
```

## 🔌 API Endpoints

| Method | Endpoint | Purpose |
|--------|----------|---------|
| POST | `/api/uploads/presign` | Get presigned upload URL |
| POST | `/api/uploads/associate/{username}` | Associate avatar with user |
| GET | `/api/uploads/download/{username}` | Get presigned download URL |
| GET | `/api/profiles/{username}` | Get profile with avatar URL |

## 📋 Request/Response Examples

### Generate Presigned URL
```json
POST /api/uploads/presign
{
  "filename": "avatar.jpg",
  "contentType": "image/jpeg",
  "username": "john_doe"
}

Response:
{
  "uploadUrl": "https://...",
  "key": "avatars/john_doe/uuid.jpg",
  "expirationTimeSeconds": 300
}
```

### Associate Avatar
```
POST /api/uploads/associate/john_doe?avatarKey=avatars/john_doe/uuid.jpg
Response: 204 No Content
```

### Get Profile
```
GET /api/profiles/john_doe
Response: { ..., avatarKey: "avatars/...", avatarUrl: "https://..." }
```

## 🎨 Frontend Flow

```
1. Select Image
   ↓
2. POST /api/uploads/presign → Get uploadUrl
   ↓
3. PUT uploadUrl (to S3) with file
   ↓
4. POST /api/uploads/associate/{username}
   ↓
5. GET /api/profiles/{username}
   ↓
6. Display avatarUrl in <img> tag
```

## 📁 Key Files

| File | Purpose |
|------|---------|
| `AwsS3Config.java` | S3 configuration |
| `S3UploadsService.java` | Presigned URL generation |
| `UploadController.java` | API endpoints |
| `application.properties` | AWS settings |
| `db_migration_avatar.sql` | Database schema |
| `PROFILE_PICTURE_SETUP.md` | Detailed setup guide |

## ⚙️ Configuration

```properties
# application.properties
aws.s3.bucket-name=portfolio-avatars
aws.s3.region=us-east-1
aws.s3.presigned-url-expiration=300
aws.s3.max-file-size=5242880
```

## ✅ Validation

- Image types: JPEG, PNG, GIF, WebP
- Max file size: 5MB
- Presigned URL TTL: 5 minutes
- S3 key format: `avatars/{username}/{uuid}.jpg`

## 🔒 Security

- Bucket is private
- All access via presigned URLs
- CORS restricted to approved origins
- Safe key generation with UUIDs

## 💰 Cost

- Free tier covers ~100 avatars/month
- Storage: ~$0.02/month for 50MB
- Requests: ~$0.06/month

## 🐛 Troubleshooting

| Issue | Solution |
|-------|----------|
| Build fails | Check Java 17+, run `mvn clean install` |
| App won't start | Set AWS environment variables |
| Upload fails | Check CORS config, verify IAM permissions |
| Can't access avatar | Verify database has avatar_key, check bucket access |

## 📖 Documentation

- Detailed setup: See `PROFILE_PICTURE_SETUP.md`
- Full implementation: See `IMPLEMENTATION_COMPLETE.md`
- API reference: See this file + docs in code

## 🚀 Ready to Go!

All backend code is implemented and compiled.
Just set up AWS and you're ready to deploy!
