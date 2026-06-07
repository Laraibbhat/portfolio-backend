# Render Deployment - Quick Start Checklist

## ✅ What I've Done For You:

1. **Created `Dockerfile`** - Multi-stage build for optimal container size
   - Uses Maven to build your JAR in build stage
   - Lightweight Alpine Linux runtime
   - Exposed port 8080

2. **Created `.dockerignore`** - Optimizes Docker build context

3. **Created `render.yaml`** - Infrastructure as Code (optional but recommended)
   - Defines web service configuration
   - Sets up MySQL database automatically
   - Configures all environment variables

4. **Updated `application.properties`** - Now supports environment variables
   - Database: `${DB_HOST:localhost}:${DB_PORT:3306}`
   - AWS S3: `${AWS_S3_BUCKET_NAME}`, `${AWS_ACCESS_KEY_ID}`, etc.
   - Fallbacks to local values if env vars not set

---

## 🚀 Deployment Steps (Copy-Paste Ready):

### Step 1: Commit & Push to GitHub
```bash
git add .
git commit -m "Configure for Render deployment"
git push origin main
```

### Step 2: Go to Render Dashboard
https://dashboard.render.com

### Step 3: Choose Deployment Method

**EASIEST - Use Blueprint (render.yaml):**
1. Click "New +" → "Blueprint"
2. Connect GitHub → Select your repo
3. Click "Create from Blueprint"
4. Done! Render auto-creates web service + MySQL database

**OR Manual Setup:**
1. Create MySQL database first:
   - "New +" → "MySQL"
   - Name: `portfolio-mysql`
   - DB Name: `portfolio_app`
   - User: `portfolio_user`
   - Plan: Free
   
2. Create Web Service:
   - "New +" → "Web Service"
   - Select repo & branch (main)
   - Runtime: Docker
   - Add environment variables (see table below)

### Step 4: Set Environment Variables

| Variable | Value | Where From |
|----------|-------|-----------|
| `DB_HOST` | Internal Database URL | MySQL dashboard |
| `DB_PORT` | `3306` | MySQL dashboard |
| `DB_NAME` | `portfolio_app` | Default |
| `DB_USER` | `portfolio_user` | Default |
| `DB_PASSWORD` | Your password | MySQL dashboard |
| `AWS_REGION` | `us-east-1` | Your AWS setup |
| `AWS_S3_BUCKET_NAME` | Your bucket | Your AWS setup |
| `AWS_ACCESS_KEY_ID` | Your key | AWS IAM |
| `AWS_SECRET_ACCESS_KEY` | Your secret | AWS IAM |

### Step 5: Deploy
- Render auto-deploys on git push
- Or manually click "Deploy" in dashboard
- Check logs for any errors

---

## 📊 Free Tier Details:

| Feature | Limit | Notes |
|---------|-------|-------|
| Web Service | Free | Spins down after 15 min inactivity |
| MySQL Database | Free | 0.5 GB storage |
| Bandwidth | Generous free quota | Per month |
| Build time | Unlimited | For free tier |

**Cost if you pay:** ~$22/month (Web $7 + MySQL $15)

---

## 🔗 Your Live API URL:
```
https://portfolio-backend.onrender.com
```

Test it after deployment:
```bash
curl https://portfolio-backend.onrender.com/api/health
```

---

## ⚠️ Important Notes:

1. **First request is slow** - Free tier spins down, first request wakes it up (~30s)
2. **Use SSL/TLS** - Already configured in `application.properties`
3. **Keep secrets in Render dashboard** - Never commit AWS keys to GitHub
4. **Monitor logs** - Use Render dashboard "Logs" tab for debugging
5. **Database backups** - Set up regular exports from MySQL dashboard

---

## 🆘 If Something Goes Wrong:

1. Check **Render Logs** tab (top of web service page)
2. Common issues:
   - ❌ Build fails: Check Java/Maven versions (using Java 17)
   - ❌ DB connection fails: Verify env variables match exactly
   - ❌ App won't start: Check Spring Boot startup logs

---

## 📚 Next Steps After Deployment:

1. ✅ Test API endpoints
2. ✅ Set up CI/CD with GitHub workflows
3. ✅ Configure custom domain (optional)
4. ✅ Set up uptime monitoring
5. ✅ Regular database backups

---

## 📖 Full Documentation:
See `RENDER_DEPLOYMENT.md` for complete deployment guide

---

**Ready to deploy? Let me know if you need any help!**