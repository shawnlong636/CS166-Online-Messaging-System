COPY USR
FROM 'usr.csv'
WITH DELIMITER ',' CSV HEADER;

COPY WORK_EXPR
FROM 'work_expr.csv'
WITH DELIMITER ',' CSV HEADER;

COPY EDUCATIONAL_DETAILS
FROM 'educational_details.csv'
WITH DELIMITER ',' CSV HEADER;

COPY MESSAGE
FROM 'message.csv'
WITH DELIMITER ',' CSV HEADER;

COPY CONNECTION_USR
FROM 'connection_usr.csv'
WITH DELIMITER ',' CSV HEADER;