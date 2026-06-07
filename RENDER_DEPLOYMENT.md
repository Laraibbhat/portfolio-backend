# Render Deployment Guide

## Prerequisites
- GitHub account with your portfolio-backend repository pushed
- Render.com account (free)

## Step 1: Prepare Your Repository

1. Make sure all changes are committed and pushed to GitHub:
```bash
git add .
git commit -m "Add Render deployment configuration"
git push origin main
```

2. Files created for deployment:
   - `Dockerfile` - Containerizes your Spring Boot application
   - `.dockerignore` - Optimizes Docker build
   - `render.yaml` - Infrastructure as Code (optional)

## Step 2: Create Render Services

### Option A: Using render.yaml (Recommended)
1. Go to https://dashboard.render.com
2. Click "New +" → "Blueprint"
3. Connect your GitHub repository
4. Select the `render.yaml` file
5. Click "Create" - Render will automatically set up the web service and MySQL database

### Option B: Manual Setup via Dashboard

#### Create MySQL Database:
1. Go to https://dashboard.render.com
2. Click "New +" → "MySQL"
3. Fill in:
   - **Name:** `portfolio-mysql`
   - **Database Name:** `portfolio_app`
   - **User:** `portfolio_user`
   - **Region:** Choose closest to your users
   - **Plan:** Free (0.5 GB storage)
4. Click "Create Database"
5. Copy the connection details (Internal Database URL)

#### Create Web Service:
1. Click "New +" → "Web Service"
2. Select your GitHub repository
3. Fill in:
   - **Name:** `portfolio-backend`
   - **Branch:** `main`
   - **Runtime:** `Docker`
   - **Build Command:** (leave empty - uses Dockerfile)
   - **Start Command:** (leave empty - uses Dockerfile)
   - **Plan:** Free
4. Under "Advanced," click "Add Environment Variable":

   | Key | Value |
   |-----|-------|
   | `DB_HOST` | Internal Database URL (from MySQL service) |
   | `DB_PORT` | `3306` |
   | `DB_NAME` | `portfolio_app` |
   | `DB_USER` | `portfolio_user` |
   | `DB_PASSWORD` | (From MySQL dashboard) |
   | `AWS_REGION` | `us-east-1` |
   | `AWS_S3_BUCKET_NAME` | Your S3 bucket name |
   | `AWS_ACCESS_KEY_ID` | Your AWS access key |
   | `AWS_SECRET_ACCESS_KEY` | Your AWS secret key |

5. Click "Create Web Service"

## Step 3: Database Migration

After services are deployed:

1. Get your database host from Render dashboard (MySQL service)
2. Create `.env` or use MySQL client to connect:
   ```
   mysql -h [render-db-host] -u portfolio_user -p portfolio_app
   ```
3. Run your migration scripts if needed:
   ```sql
   source src/main/resources/db_migration_avatar.sql
   ```
   Or use Spring Boot's Flyway/Liquibase if configured.

## Step 4: Initial Deployment

1. After environment variables are set, click "Manual Deploy" → "Deploy latest commit"
2. View deployment logs in the "Logs" tab
3. Once deployed, your app will be available at: `https://portfolio-backend.onrender.com`

## Important Notes

### Free Tier Limitations:
- Web service spins down after 15 minutes of inactivity
- First request after spin-down takes ~30 seconds
- MySQL database limited to 0.5 GB
- 100 MB max file upload (configure via AWS S3)

### Cost After Free Tier:
- Web Service: $7/month (if kept on free tier, it's free with spin-down)
- MySQL Database: $15/month (or upgrade to managed option)
- Total: ~$22/month minimum, or free with limitations

### To Keep Everything Free:
- Use Render's free tier (with spin-down)
- Or upgrade only MySQL when needed
- Use AWS S3 free tier for file storage (keep within limits)

## Troubleshooting

### Build Fails
Check the build logs in Render dashboard. Common issues:
- Java version mismatch (using Java 17)
- Missing Maven settings
- Timeout during dependency download

### Database Connection Issues
- Verify environment variables match exactly
- Check DB_HOST is the Internal Database URL, not external
- Ensure SSL requirements match your MySQL setup

### Application Won't Stay Running
- Check Spring Boot logs for startup errors
- Verify all environment variables are set
- Ensure database migrations completed

## Database Backup
Before making changes:
1. Export data regularly using:
   ```bash
   mysqldump -h [host] -u portfolio_user -p portfolio_app > backup.sql
   ```

## Next Steps
1. Push all changes to GitHub
2. Connect Render to your GitHub account
3. Deploy using render.yaml or manual setup
4. Test API endpoints at `https://portfolio-backend.onrender.com/api/*`

## Useful Links
- Render Documentation: https://render.com/docs
- Spring Boot Docker: https://spring.io/guides/gs/spring-boot-docker/
- MySQL on Render: https://render.com/docs/deploy-mysql