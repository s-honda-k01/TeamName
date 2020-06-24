DROP TABLE TR_ToDo CASCADE CONSTRAINTS;
DROP TABLE MT_STATUS CASCADE CONSTRAINTS;
DROP TABLE MT_ToDoITEM CASCADE CONSTRAINTS;
DROP TABLE MT_USER CASCADE CONSTRAINTS;
DROP TABLE MT_ROLE CASCADE CONSTRAINTS;

CREATE TABLE MT_ROLE(
		role_code                     		VARCHAR2(2)		 NOT NULL,
		role_name                     		VARCHAR2(50)		 NOT NULL,
		explain                       		VARCHAR2(64)		 NULL 
);


CREATE TABLE MT_USER(
		user_code                     		VARCHAR2(8)		 NOT NULL,
		user_name                     		VARCHAR2(100)		 NOT NULL,
		password                      		VARCHAR2(100)		 NOT NULL,
		role_code                     		VARCHAR2(2)		 NOT NULL,
		validity                      		NUMBER(1)		 NOT NULL
);


CREATE TABLE MT_ToDoITEM(
		todo_item_code                		VARCHAR2(8)		 NOT NULL,
		todo_item                     		VARCHAR2(100)		 NOT NULL
);


CREATE TABLE MT_STATUS(
		status_code                   		VARCHAR2(8)		 NOT NULL,
		status                        		VARCHAR2(100)		 NOT NULL
);


CREATE TABLE TR_ToDo(
		todo_id                       		NUMBER(8)		 DEFAULT 1		 NOT NULL,
		user_code                     		VARCHAR2(8)		 NOT NULL,
		todo_item_code                		VARCHAR2(8)		 NULL ,
		status_code                   		VARCHAR2(8)		 NULL 
);

DROP SEQUENCE TR_ToDo_todo_id_SEQ;

CREATE SEQUENCE TR_ToDo_todo_id_SEQ NOMAXVALUE NOCACHE NOORDER NOCYCLE;

CREATE TRIGGER TR_ToDo_todo_id_TRG
BEFORE INSERT ON TR_ToDo
FOR EACH ROW
BEGIN
IF :NEW.todo_id IS NULL THEN
  SELECT TR_ToDo_todo_id_SEQ.NEXTVAL INTO :NEW.todo_id FROM DUAL;
END IF;
END;
/


ALTER TABLE MT_ROLE ADD CONSTRAINT IDX_MT_ROLE_PK PRIMARY KEY (role_code);

ALTER TABLE MT_USER ADD CONSTRAINT IDX_MT_USER_PK PRIMARY KEY (user_code);
ALTER TABLE MT_USER ADD CONSTRAINT IDX_MT_USER_FK0 FOREIGN KEY (role_code) REFERENCES MT_ROLE (role_code);

ALTER TABLE MT_ToDoITEM ADD CONSTRAINT IDX_MT_ToDoITEM_PK PRIMARY KEY (todo_item_code);

ALTER TABLE MT_STATUS ADD CONSTRAINT IDX_MT_STATUS_PK PRIMARY KEY (status_code);

ALTER TABLE TR_ToDo ADD CONSTRAINT IDX_TR_ToDo_PK PRIMARY KEY (todo_id);
ALTER TABLE TR_ToDo ADD CONSTRAINT IDX_TR_ToDo_FK0 FOREIGN KEY (user_code) REFERENCES MT_USER (user_code);
ALTER TABLE TR_ToDo ADD CONSTRAINT IDX_TR_ToDo_FK1 FOREIGN KEY (todo_item_code) REFERENCES MT_ToDoITEM (todo_item_code);
ALTER TABLE TR_ToDo ADD CONSTRAINT IDX_TR_ToDo_FK2 FOREIGN KEY (status_code) REFERENCES MT_STATUS (status_code);

