COPY USR
FROM 'usr.csv'
WITH DELIMITER ',';

COPY WORK_EXPR
FROM 'work_expr.csv'
WITH DELIMITER ',';

COPY EDUCATIONAL_DETAILS
FROM 'educational_details.csv'
WITH DELIMITER ',';

COPY MESSAGE
FROM 'message.csv'
WITH DELIMITER ',';

COPY CONNECTION_USR
FROM 'connection_usr.csv'
WITH DELIMITER ',';