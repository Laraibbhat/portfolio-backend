#!/bin/bash
# Test Script for Profile Picture Upload API

echo "========================================"
echo "Testing Profile Picture Upload API"
echo "========================================"
echo ""

# Variables
BASE_URL="http://localhost:8080"
USERNAME="testuser"
FILENAME="avatar.jpg"
CONTENT_TYPE="image/jpeg"

echo "1. Testing Presigned Upload URL Generation"
echo "========================================="
PRESIGN_RESPONSE=$(curl -s -X POST "$BASE_URL/api/uploads/presign" \
  -H "Content-Type: application/json" \
  -d "{
    \"filename\": \"$FILENAME\",
    \"contentType\": \"$CONTENT_TYPE\",
    \"username\": \"$USERNAME\"
  }")

echo "Response:"
echo "$PRESIGN_RESPONSE" | jq '.' 2>/dev/null || echo "$PRESIGN_RESPONSE"
echo ""

# Extract upload URL and key
UPLOAD_URL=$(echo "$PRESIGN_RESPONSE" | jq -r '.uploadUrl' 2>/dev/null)
AVATAR_KEY=$(echo "$PRESIGN_RESPONSE" | jq -r '.key' 2>/dev/null)

if [ -z "$UPLOAD_URL" ] || [ "$UPLOAD_URL" = "null" ]; then
  echo "❌ Failed to get presigned URL. Check AWS credentials."
  exit 1
fi

echo "✓ Got presigned upload URL"
echo "  Key: $AVATAR_KEY"
echo ""

echo "2. Associate Avatar with User"
echo "========================================="
ASSOC_RESPONSE=$(curl -s -X POST "$BASE_URL/api/uploads/associate/$USERNAME?avatarKey=$AVATAR_KEY")
echo "Response Status: $(echo "$ASSOC_RESPONSE" | wc -c) bytes (should be empty for 204)"
echo "✓ Avatar associated with user"
echo ""

echo "3. Get User Profile with Avatar"
echo "========================================="
PROFILE_RESPONSE=$(curl -s -X GET "$BASE_URL/api/profiles/$USERNAME" \
  -H "Accept: application/json")

echo "Response:"
echo "$PROFILE_RESPONSE" | jq '.avatarKey, .avatarUrl' 2>/dev/null || echo "$PROFILE_RESPONSE"
echo ""
echo "========================================="
echo "✅ All tests completed!"
echo "========================================="
