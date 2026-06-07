# Profile Picture Upload Integration - Complete Guide

## Overview
This guide covers the implementation of profile picture uploads using AWS S3 presigned URLs. The frontend uploads directly to S3, minimizing server bandwidth and compute costs.

## AWS S3 Setup (Required)

### 1. Create S3 Bucket
1. Log in to AWS Console → S3
2. Click **Create bucket**
3. Bucket name: `portfolio-avatars` (or similar)
4. Region: Select closest to your users
5. **Block all public access** (keep bucket private)
6. Create bucket

### 2. Create IAM User
1. Go to IAM → Users → Create user
2. User name: `portfolio-backend-user`
3. Set programmatic access type
4. Skip permissions for now
5. Create user and save **Access Key ID** and **Secret Access Key**

### 3. Attach S3 Permissions
1. Go to IAM → Users → `portfolio-backend-user`
2. Click **Add inline policy** → JSON
3. Paste this policy:

```json
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Effect": "Allow",
            "Action": [
                "s3:GetObject",
                "s3:PutObject",
                "s3:DeleteObject"
            ],
            "Resource": "arn:aws:s3:::portfolio-avatars/*"
        },
        {
            "Effect": "Allow",
            "Action": "s3:ListBucket",
            "Resource": "arn:aws:s3:::portfolio-avatars"
        }
    ]
}
```

### 4. Configure CORS (for frontend browser uploads)
1. Go to S3 → `portfolio-avatars` → Permissions → CORS
2. Edit CORS with:

```json
[
    {
        "AllowedHeaders": ["*"],
        "AllowedMethods": ["GET", "PUT", "POST"],
        "AllowedOrigins": ["http://localhost:3000", "https://yourdomain.com"],
        "ExposeHeaders": ["x-amz-version-id"],
        "MaxAgeSeconds": 3000
    }
]
```

## Backend Configuration

### 1. Update application.properties
Add these AWS configurations:

```properties
aws.s3.bucket-name=portfolio-avatars
aws.s3.region=us-east-1
aws.s3.access-key=${AWS_ACCESS_KEY_ID}
aws.s3.secret-key=${AWS_SECRET_ACCESS_KEY}
aws.s3.presigned-url-expiration=300
aws.s3.max-file-size=5242880
```

### 2. Set Environment Variables
```bash
# Linux/Mac
export AWS_ACCESS_KEY_ID=your_access_key
export AWS_SECRET_ACCESS_KEY=your_secret_key

# Windows (PowerShell)
$env:AWS_ACCESS_KEY_ID="your_access_key"
$env:AWS_SECRET_ACCESS_KEY="your_secret_key"
```

### 3. Database Migration
Run this SQL query on your MySQL database:

```sql
ALTER TABLE users ADD COLUMN avatar_key VARCHAR(512) AFTER location;
```

Or use the provided migration file: `src/main/resources/db_migration_avatar.sql`

### 4. Build the Project
```bash
mvn clean install
mvn spring-boot:run
```

## API Endpoints

### 1. Generate Presigned Upload URL
**POST** `/api/uploads/presign`

Request:
```json
{
    "filename": "avatar.jpg",
    "contentType": "image/jpeg",
    "username": "john_doe"
}
```

Response:
```json
{
    "uploadUrl": "https://portfolio-avatars.s3.amazonaws.com/...",
    "key": "avatars/john_doe/uuid-here.jpg",
    "contentType": "image/jpeg",
    "expirationTimeSeconds": 300
}
```

### 2. Associate Avatar with User
**POST** `/api/uploads/associate/{username}?avatarKey=avatars/john_doe/uuid-here.jpg`

Response: 204 No Content

### 3. Get Avatar Download URL
**GET** `/api/uploads/download/{username}`

Response:
```json
{
    "uploadUrl": "https://portfolio-avatars.s3.amazonaws.com/...",
    "key": "avatars/john_doe/uuid-here.jpg",
    "expirationTimeSeconds": 300
}
```

### 4. Get User Profile (with Avatar)
**GET** `/api/profiles/{username}`

Response includes:
```json
{
    "username": "john_doe",
    "name": "John Doe",
    "avatarKey": "avatars/john_doe/uuid-here.jpg",
    "avatarUrl": "https://...(presigned URL)...",
    ...
}
```

## Frontend Integration Example

### Step 1: Request Presigned URL
```javascript
const response = await fetch('/api/uploads/presign', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({
        filename: file.name,
        contentType: file.type,
        username: 'john_doe'
    })
});
const { uploadUrl, key } = await response.json();
```

### Step 2: Upload to S3
```javascript
await fetch(uploadUrl, {
    method: 'PUT',
    headers: { 'Content-Type': file.type },
    body: file
});
```

### Step 3: Associate Avatar with User
```javascript
await fetch(`/api/uploads/associate/john_doe?avatarKey=${key}`, {
    method: 'POST'
});
```

### Step 4: Fetch Profile (includes avatar URL)
```javascript
const profile = await fetch('/api/profiles/john_doe').then(r => r.json());
console.log(profile.avatarUrl); // Use this to display the avatar
```

## Security & Best Practices

✅ **Implemented:**
- Keep presigned URLs short-lived (5 minutes default)
- Validate content type (images only)
- Generate safe S3 keys using UUIDs
- Keep S3 bucket private
- Presigned URLs for all access (no public URLs)

✅ **Recommended:**
- Set S3 lifecycle policy to delete unassociated avatars after 30-90 days
- Enable versioning for recovery
- Monitor S3 costs

❌ **Never:**
- Use public S3 URLs without presigned URLs
- Store plain file paths
- Allow arbitrary file types
- Use long-lived credentials in code

## Lifecycle Rule Setup (Optional)
Create an S3 lifecycle rule to auto-delete orphaned files:

1. Go to S3 → `portfolio-avatars` → Management → Lifecycle policies
2. Create rule:
   - Filter: `avatars/` prefix
   - Action: Delete object version (30+ days old)

## Troubleshooting

**Issue:** `Access Denied` when uploading
- Check IAM user has `s3:PutObject` permission
- Verify CORS configuration on bucket

**Issue:** `Signature does not match` error
- Verify AWS credentials are correct
- Check system time is synchronized

**Issue:** Presigned URL expires before upload
- Increase `aws.s3.presigned-url-expiration` value
- Consider decreasing file size

**Issue:** Backend fails to start
- Verify AWS SDK dependency in pom.xml
- Check environment variables are set
- Review Spring logs for detailed errors

## Cost Estimation

Monthly cost for 100 users with avatars:
- S3 Storage: ~$0.02 (50MB storage)
- PUT requests: ~$0.05 (10k presign + upload requests)
- GET requests: ~$0.01 (presigned download URLs)
- **Total: ~$0.08/month** ✅ Free tier covers this

## Support

For issues or questions:
1. Check AWS CloudWatch logs
2. Verify IAM permissions
3. Ensure database migration ran
4. Test presigned URL generation with curl

## Files Changed

- `src/main/java/com/portfolio/backend/config/AwsS3Config.java` - AWS S3 client configuration
- `src/main/java/com/portfolio/backend/service/S3UploadsService.java` - Presigned URL generation
- `src/main/java/com/portfolio/backend/controller/UploadController.java` - Upload endpoints
- `src/main/java/com/portfolio/backend/entity/User.java` - Added avatarKey column
- `src/main/java/com/portfolio/backend/dto/UserDTO.java` - Added avatar fields
- `src/main/java/com/portfolio/backend/dto/ProfileDTO.java` - Added avatar fields
- `src/main/java/com/portfolio/backend/service/UserService.java` - Avatar update methods
- `src/main/java/com/portfolio/backend/service/ProfileService.java` - Avatar URL generation
- `pom.xml` - Added AWS SDK v2 dependency
- `application.properties` - AWS configuration
- `db_migration_avatar.sql` - Database schema migration
