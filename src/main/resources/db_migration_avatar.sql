-- Avatar Key Column Addition for Profile Picture Support
-- Description: Adds avatar_key column to users table to store S3 object keys for profile pictures

ALTER TABLE users ADD COLUMN avatar_key VARCHAR(512) AFTER location;

-- Optional: Add index for better query performance if needed
-- CREATE INDEX idx_avatar_key ON users(avatar_key);
