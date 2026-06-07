# RENDER DEPLOYMENT - CHEAT SHEET

## 🚀 3-Step Deploy

```bash
# Step 1: Push to GitHub
git add . && git commit -m "Add Render config" && git push origin main

# Step 2: Go to https://dashboard.render.com

# Step 3: Click "New +" → "Blueprint" → Select repo → "Create"
```

---

## ENV VARIABLES TO SET

```
AWS_REGION=us-east-1
AWS_S3_BUCKET_NAME=your-bucket-name
AWS_ACCESS_KEY_ID=your-aws-key
AWS_SECRET_ACCESS_KEY=your-aws-secret
```
*Database vars auto-set by render.yaml*

---

## 🎯 Quick Reference

| Task | Command |
|------|---------|
| Deploy | Push to GitHub or click "Manual Deploy" in Render |
| Update API | `git commit && git push` (auto-deploys) |
| Check Logs | Render Dashboard → Web Service → Logs |
| Test API | `curl https://portfolio-backend.onrender.com/api/health` |
| SSH Database | From MySQL service in Render Dashboard |

---

## 📍 Your URLs

```
API Base:    https://portfolio-backend.onrender.com
Health:      https://portfolio-backend.onrender.com/api/health
Experiences: https://portfolio-backend.onrender.com/api/experiences
```

---

## 💰 Cost

```
FREE ✓ (with 15 min spin-down)
or $22/month for always-on + reliable DB
```

---

## 🔍 Troubleshooting Quick Check

```
Error?
  ↓
Check Render Logs Tab
  ↓
Search error in RENDER_TROUBLESHOOTING.md
  ↓
Fix locally
  ↓
git push (auto-deploys)
```

---

## 📚 Guides

- `DEPLOY_NOW.md` - Start here!
- `RENDER_VISUAL_GUIDE.md` - Flowcharts
- `RENDER_QUICK_START.md` - Detailed steps
- `RENDER_TROUBLESHOOTING.md` - Error solutions

---

## ✅ Success Checklist

- [ ] GitHub repo up to date
- [ ] render.yaml deployed
- [ ] Logs show: "Build and Deploy successful"
- [ ] API responds at your URL
- [ ] AWS env vars added

---

## 🎉 You're Live!

Your API is now running at:
```
https://portfolio-backend.onrender.com
```

Share that URL with the world! 🌍