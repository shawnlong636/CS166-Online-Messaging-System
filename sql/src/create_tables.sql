DROP TABLE IF EXISTS WORK_EXPR;
DROP TABLE IF EXISTS EDUCATIONAL_DETAILS;
DROP TABLE IF EXISTS MESSAGE;
DROP TABLE IF EXISTS CONNECTION_USR;
DROP TABLE IF EXISTS USR;


CREATE TABLE USR(
	userId TEXT UNIQUE NOT NULL, 
	password TEXT NOT NULL,
	email TEXT NOT NULL,
	name TEXT,
	dateOfBirth date,
	Primary Key(userId));

CREATE TABLE WORK_EXPR(
	userId TEXT NOT NULL, 
	company TEXT NOT NULL, 
	role TEXT NOT NULL,
	location TEXT,
	startDate date,
	endDate date,
	PRIMARY KEY(userId,company,role,startDate));

CREATE TABLE EDUCATIONAL_DETAILS(
	userId TEXT NOT NULL, 
	instituitionName TEXT NOT NULL, 
	major TEXT NOT NULL,
	degree TEXT NOT NULL,
	startdate date,
	enddate date,
	PRIMARY KEY(userId,major,degree));

CREATE TABLE MESSAGE(
	msgId integer UNIQUE NOT NULL, 
	senderId TEXT NOT NULL,
	receiverId TEXT NOT NULL,
	contents TEXT NOT NULL,
	sendTime timestamp,
	deleteStatus integer,
	status TEXT NOT NULL,
	PRIMARY KEY(msgId));

CREATE TABLE CONNECTION_USR(
	userId TEXT NOT NULL, 
	connectionId TEXT NOT NULL, 
	status TEXT NOT NULL,
	PRIMARY KEY(userId,connectionId));