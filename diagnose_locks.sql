-- MySQL Diagnosis Script for Lock Issues
-- Run these commands to identify what's holding locks on the users table

-- 1. Show current processes and active connections
SHOW FULL PROCESSLIST;

-- 2. Check InnoDB transaction information
SELECT * FROM INFORMATION_SCHEMA.INNODB_TRX;

-- 3. Check InnoDB locks and see what's locked
SELECT * FROM INFORMATION_SCHEMA.INNODB_LOCKS;

-- 4. Check which transactions are waiting for locks
SELECT * FROM INFORMATION_SCHEMA.INNODB_LOCK_WAITS;

-- 5. Get full InnoDB status (includes deadlock info, lock info, etc.)
SHOW ENGINE INNODB STATUS\G