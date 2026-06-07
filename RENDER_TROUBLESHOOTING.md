# Render Deployment Troubleshooting Guide

## 🔍 Debugging Workflow

When deployment fails:
1. Check **Render dashboard Logs tab**
2. Look for error patterns below
3. Copy relevant error and follow the solution
4. Re-deploy after fix

---

## Common Issues & Solutions

### 1. ❌ Build Fails - "Java version not found"

**Error in logs:**
```
ERROR: The image you're running on doesn't support the Java version you're trying to install
```

**Solution:**
- Dockerfile uses `eclipse-temurin:17-jre-alpine` (already configured)
- Check your `pom.xml` has `<java.version>17</java.version>`

**Verify:**
```bash
grep -n "java.version" pom.xml
# Should show: <java.version>17</java.version>
```

---

### 2. ❌ Build Fails - "Maven dependency download timeout"

**Error in logs:**
```
[ERROR] Failed to execute goal on project portfolio-backend
[FATAL] Could not find resource
```

**Solution:**
```bash
# Clear Maven cache and rebuild
rm -rf ~/.m2/repository
mvn clean package -DskipTests
```

Or increase Docker build timeout in Render dashboard settings.

---

### 3. ❌ Build Fails - "Dockerfile not found"

**Error in logs:**
```
ERROR: Couldn't find a Dockerfile
```

**Solution:**
- Verify `Dockerfile` exists in root directory:
  ```bash
  ls -la Dockerfile
  ```
- Ensure it's committed to GitHub:
  ```bash
  git add Dockerfile
  git commit -m "Add Dockerfile"
  git push
  ```

---

### 4. ❌ App Crashes - "Connection refused" to MySQL

**Error in logs:**
```
com.mysql.cj.jdbc.exceptions.CommunicationException: Communications link failure
java.net.ConnectException: Connection refused
```

**Solutions:**
1. **Check all database env vars are set:**
   - `DB_HOST` - Should be Render's internal database URL (NOT localhost)
   - `DB_PORT` - Usually 3306
   - `DB_NAME` - portfolio_app
   - `DB_USER` - portfolio_user
   - `DB_PASSWORD` - Correct password

2. **Verify env vars in Render dashboard:**
   ```bash
   # These must be exact (copy from MySQL service page)
   DB_HOST=postgres-xxx.render.internal
   DB_PORT=3306
   ```

3. **Check MySQL database is running:**
   - Go to Render dashboard → MySQL service
   - Status should be "Available"

4. **Database not migrated:**
   ```sql
   -- SSH into database and run migration
   source db_migration_avatar.sql
   ```

---

### 5. ❌ App Crashes - "Unknown database 'portfolio_app'"

**Error in logs:**
```
ERROR 1049 (42000): Unknown database 'portfolio_app'
```

**Solution:**
1. Verify database was created with correct name:
   - Render Dashboard → MySQL → Check "Database Name" = `portfolio_app`

2. If missing, create it:
   ```bash
   mysql -h [db_host] -u portfolio_user -p -e "CREATE DATABASE portfolio_app;"
   ```

3. Or recreate the MySQL service in Render dashboard

---

### 6. ❌ App Crashes - "Access denied for user"

**Error in logs:**
```
Access denied for user 'portfolio_user'@'x.x.x.x'
SQLSTATE[28000]: [MySQL] [ODBC 3.51 Driver]
```

**Solutions:**
1. **Password mismatch:**
   - Get correct password from Render MySQL dashboard
   - Update `DB_PASSWORD` env var exactly
   - Verify no spaces or special characters

2. **User doesn't exist:**
   ```bash
   # Connect as admin and verify user exists
   mysql -h [host] -u portfolio_user -p -e "SELECT USER();"
   ```

3. **Host not allowed:**
   - Render handles this automatically
   - Use Internal Database URL (from dashboard)

---

### 7. ❌ App Crashes - "Column not found" errors

**Error in logs:**
```
ERROR 1054 (42000): Unknown column 'profile_picture_url'
```

**Solution:**
1. Run database migrations:
   ```bash
   source src/main/resources/db_migration_avatar.sql
   ```

2. Check schema matches entity classes:
   - Review entity classes in `src/main/java/com/portfolio/backend/entity/`
   - Compare with database schema

3. Change `spring.jpa.hibernate.ddl-auto` temporarily:
   ```properties
   # In application.properties - ONLY for testing!
   spring.jpa.hibernate.ddl-auto=update
   ```

---

### 8. ❌ 502 Bad Gateway Error

**Error:**
```
502 Bad Gateway
Powered by Render
```

**Solutions:**
1. **App is starting but not ready yet:**
   - Wait 30-60 seconds and refresh
   - Check Render logs for startup status

2. **App crashed after starting:**
   - Check Logs tab for runtime errors
   - Look for OutOfMemory exceptions
   - Check database connection issues

3. **Port not available:**
   - Verify `server.port=8080` in application.properties
   - Render exposes port 8080 automatically

---

### 9. ❌ 503 Service Unavailable (Free Tier)

**Error:**
```
Service Unavailable (503)
```

**Cause:** Free tier app spun down after 15 minutes of inactivity

**Solution:**
- Wait 30 seconds for app to wake up
- Or upgrade to paid tier to prevent spin-down
- First request always takes longer (~30s)

---

### 10. ❌ AWS S3 Connection Issues

**Error in logs:**
```
The AWS Access Key Id you provided does not exist in our records
```

**Solutions:**
1. **Verify AWS credentials:**
   ```bash
   # Get from AWS IAM dashboard
   AWS_ACCESS_KEY_ID=AKIA...
   AWS_SECRET_ACCESS_KEY=wJal...
   ```

2. **Check S3 bucket exists:**
   ```bash
   aws s3 ls s3://portfolio-avatars --region us-east-1
   ```

3. **Verify IAM permissions:**
   - Policy must include `s3:*` actions
   - Or at minimum: `GetObject`, `PutObject`, `DeleteObject`

4. **Update env vars in Render:**
   - `AWS_ACCESS_KEY_ID`
   - `AWS_SECRET_ACCESS_KEY`
   - `AWS_S3_BUCKET_NAME`
   - `AWS_REGION`

---

### 11. ❌ Logs Show "No errors but app not responding"

**Possible causes:**
1. App is running but requests timeout
2. Database queries too slow
3. Memory pressure (512MB limit on free tier)

**Debug steps:**
```bash
# Check app startup logs
curl -I https://portfolio-backend.onrender.com
# Should return 200 OK within 30s

# Check database connectivity
# Look for slow query logs
```

**Solutions:**
1. Add request timeout logging
2. Optimize slow database queries
3. Increase memory allocation (paid tier)

---

## 📊 Debugging Checklist

When deployment fails, go through this:

- [ ] Check Render Logs tab (top of web service page)
- [ ] Search for "ERROR" or "FATAL" keywords
- [ ] Note the exact error message
- [ ] Match error to section above
- [ ] Follow the solution steps
- [ ] Make changes locally
- [ ] Commit and push to GitHub
- [ ] Click "Manual Deploy" or wait for auto-deploy
- [ ] Check Logs again after retry

---

## 🔧 Manual Debugging

### SSH into App Container (if needed):
```bash
# Not available on free tier, but useful for paid tier
# Would use: renderctl shell
```

### Check Database Directly:
```bash
mysql -h [render-db-host] \
      -u portfolio_user \
      -p[password] \
      portfolio_app -e "SHOW TABLES;"
```

### Check MySQL Logs:
- Render Dashboard → MySQL service → Logs tab

---

## 📞 Get Help

1. **Render Official Docs:** https://render.com/docs
2. **Spring Boot Troubleshooting:** https://spring.io/projects/spring-boot
3. **MySQL Documentation:** https://dev.mysql.com/doc/
4. **Stack Overflow:** Tag with `render`, `spring-boot`, `mysql`

---

## 🚀 Testing Your Deployment

After successful deployment:

```bash
# Test API health
curl https://portfolio-backend.onrender.com/api/health

# Test with actual data
curl https://portfolio-backend.onrender.com/api/experiences

# Check response time (free tier: should be 500-2000ms first request)
time curl https://portfolio-backend.onrender.com/api/awards
```

---

## 📝 Debugging Log Example

```
[INFO] BUILD SUCCESS - App built successfully ✓
[INFO] Starting Spring Boot Application... - App starting
[DEBUG] Connecting to database - DB connection initiated
[DEBUG] Hibernate initialized successfully - DB schema loaded
[INFO] Server is running on port 8080 - App ready!
[ERROR] Connection refused - Something went wrong (use solutions above)
```

---

## When to Upgrade to Paid Tier

- ❌ Free tier errors become frequent
- ❌ First request timeout after spin-down
- ❌ Database grows beyond 0.5 GB
- ❌ Need 24/7 uptime
- ✅ Upgrade: $22/month gives you always-on + reliable database