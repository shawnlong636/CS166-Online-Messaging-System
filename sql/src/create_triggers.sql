
DROP FUNCTION IF EXISTS get_pair_count(user1 TEXT, user2 TEXT);
ALTER TABLE FRIENDS DROP CONSTRAINT IF EXISTS unique_pair;
ALTER TABLE FRIENDS DROP CONSTRAINT IF EXISTS no_self_edge;
ALTER TABLE REQUESTS DROP CONSTRAINT IF EXISTS unique_requests;

-- CONSTRAINTS ON USERS TABLE
ALTER TABLE USR ADD CONSTRAINT unique_user UNIQUE(userId);

-- Credit to https://inviqa.com/blog/storing-graphs-database-sql-meets-social-network
-- which was used as a reference when creating the procedure
-- for the Friends Table

-- CONSTRAINTS ON FRIENDS TABLE
CREATE FUNCTION get_pair_count(IN user1 TEXT, IN user2 TEXT)
RETURNS INTEGER AS $body$
DECLARE retval INTEGER DEFAULT 0;
BEGIN
SELECT COUNT(*) INTO retval FROM (
    SELECT * FROM FRIENDS WHERE userA = user1 AND userB = user2
    UNION
    SELECT * FROM FRIENDS WHERE userA = user2 AND userB = user1
) AS pairs;
RETURN retval;
END
$body$
LANGUAGE 'plpgsql';

ALTER TABLE FRIENDS ADD CONSTRAINT unique_pair 
CHECK (get_pair_count(userA, userB) < 1);

ALTER TABLE FRIENDS ADD CONSTRAINT no_self_edge CHECK (userA <> userB);

-- CONSTRAINTS ON REQUESTS TABLE
ALTER TABLE REQUESTS ADD CONSTRAINT unique_requests UNIQUE (userId, connectionId);