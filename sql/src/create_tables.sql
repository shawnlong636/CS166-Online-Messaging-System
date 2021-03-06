DROP TABLE IF EXISTS WORK_EXPR;
DROP TABLE IF EXISTS EDUCATIONAL_DETAILS;
DROP TABLE IF EXISTS MESSAGE;
DROP TABLE IF EXISTS REQUESTS;
DROP TABLE IF EXISTS FRIENDS;
DROP TABLE IF EXISTS USR;

CREATE TABLE USR(
	userId TEXT UNIQUE NOT NULL, 
	password TEXT NOT NULL,
	email TEXT NOT NULL,
	name TEXT,
	dateOfBirth date,
	Primary Key(userId));

CREATE TABLE MESSAGE(
	msgId integer UNIQUE NOT NULL, 
	senderId TEXT NOT NULL,
	receiverId TEXT NOT NULL,
	contents TEXT NOT NULL,
	sendTime timestamp,
	status TEXT NOT NULL,
	PRIMARY KEY(msgId),
	FOREIGN KEY(senderId) REFERENCES USR(userId) ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY(receiverId) REFERENCES USR(userId) ON DELETE CASCADE ON UPDATE CASCADE
	);

CREATE TABLE FRIENDS(
	userA TEXT NOT NULL,
	userB TEXT NOT NULL,
	PRIMARY KEY(userA,userB),
	FOREIGN KEY(userA) REFERENCES USR ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY(userB) REFERENCES USR ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE REQUESTS(
	userId TEXT NOT NULL,
	connectionId TEXT NOT NULL,
	PRIMARY KEY(userId,connectionId),
	FOREIGN KEY (userId) REFERENCES USR ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY(connectionId) REFERENCES USR ON DELETE CASCADE ON UPDATE CASCADE
);