@echo off
REM Profile Picture Upload - AWS Setup Script for Windows
REM This script sets up environment variables for AWS S3 integration

echo ========================================
echo AWS S3 Configuration Setup
echo ========================================
echo.

REM Set default values
set AWS_S3_BUCKET_NAME=portfolio-avatars
set AWS_REGION=us-east-1

REM You need to replace these with your actual credentials from AWS IAM
echo Enter your AWS credentials from IAM user (portfolio-backend-user):
echo.

set /p AWS_ACCESS_KEY_ID="Enter AWS Access Key ID: "
set /p AWS_SECRET_ACCESS_KEY="Enter AWS Secret Access Key: "

echo.
echo ========================================
echo Setting Environment Variables...
echo ========================================
echo.

REM Set environment variables (for current session)
setx AWS_ACCESS_KEY_ID "%AWS_ACCESS_KEY_ID%"
setx AWS_SECRET_ACCESS_KEY "%AWS_SECRET_ACCESS_KEY%"
setx AWS_S3_BUCKET_NAME "%AWS_S3_BUCKET_NAME%"
setx AWS_REGION "%AWS_REGION%"

echo ✓ AWS_ACCESS_KEY_ID set
echo ✓ AWS_SECRET_ACCESS_KEY set
echo ✓ AWS_S3_BUCKET_NAME set to: %AWS_S3_BUCKET_NAME%
echo ✓ AWS_REGION set to: %AWS_REGION%
echo.

echo ========================================
echo ⚠️  IMPORTANT: Restart PowerShell/CMD
echo ========================================
echo You MUST restart your terminal window for environment variables to take effect!
echo After restarting, run: mvn spring-boot:run
echo.
pause
