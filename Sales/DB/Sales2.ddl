DROP TABLE TR_SALES CASCADE CONSTRAINTS;
DROP TABLE MT_CUSTOMER CASCADE CONSTRAINTS;
DROP TABLE MT_ITEM CASCADE CONSTRAINTS;
DROP TABLE MT_ITEM_GENRE CASCADE CONSTRAINTS;
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


CREATE TABLE MT_ITEM_GENRE(
		item_genre_code               		VARCHAR2(8)		 NOT NULL,
		item_genre_name               		VARCHAR2(50)		 NOT NULL
);


CREATE TABLE MT_ITEM(
		item_code                     		VARCHAR2(8)		 NOT NULL,
		item_name                     		VARCHAR2(100)		 NOT NULL,
		item_genre_code               		VARCHAR2(8)		 NOT NULL,
		spec                          		VARCHAR2(300)		 NULL ,
		price                         		NUMBER(7)		 NOT NULL
);


CREATE TABLE MT_CUSTOMER(
		customer_code                 		VARCHAR2(8)		 NOT NULL,
		customer_name                 		VARCHAR2(100)		 NOT NULL
);


CREATE TABLE TR_SALES(
		sales_id                      		NUMBER(8)		 DEFAULT 1		 NOT NULL,
		customer_code                 		VARCHAR2(8)		 NOT NULL,
		user_code                     		VARCHAR2(8)		 NOT NULL,
		item_code                     		VARCHAR2(8)		 NOT NULL,
		quantity                      		NUMBER(4)		 NOT NULL,
		sales_date                    		DATE		 NOT NULL
);

DROP SEQUENCE TR_SALES_sales_id_SEQ;

CREATE SEQUENCE TR_SALES_sales_id_SEQ NOMAXVALUE NOCACHE NOORDER NOCYCLE;

CREATE TRIGGER TR_SALES_sales_id_TRG
BEFORE INSERT ON TR_SALES
FOR EACH ROW
BEGIN
IF :NEW.sales_id IS NULL THEN
  SELECT TR_SALES_sales_id_SEQ.NEXTVAL INTO :NEW.sales_id FROM DUAL;
END IF;
END;
/


ALTER TABLE MT_ROLE ADD CONSTRAINT IDX_MT_ROLE_PK PRIMARY KEY (role_code);

ALTER TABLE MT_USER ADD CONSTRAINT IDX_MT_USER_PK PRIMARY KEY (user_code);
ALTER TABLE MT_USER ADD CONSTRAINT IDX_MT_USER_FK0 FOREIGN KEY (role_code) REFERENCES MT_ROLE (role_code);

ALTER TABLE MT_ITEM_GENRE ADD CONSTRAINT IDX_MT_ITEM_GENRE_PK PRIMARY KEY (item_genre_code);

ALTER TABLE MT_ITEM ADD CONSTRAINT IDX_MT_ITEM_PK PRIMARY KEY (item_code);
ALTER TABLE MT_ITEM ADD CONSTRAINT IDX_MT_ITEM_FK0 FOREIGN KEY (item_genre_code) REFERENCES MT_ITEM_GENRE (item_genre_code);

ALTER TABLE MT_CUSTOMER ADD CONSTRAINT IDX_MT_CUSTOMER_PK PRIMARY KEY (customer_code);

ALTER TABLE TR_SALES ADD CONSTRAINT IDX_TR_SALES_PK PRIMARY KEY (sales_id);
ALTER TABLE TR_SALES ADD CONSTRAINT IDX_TR_SALES_FK0 FOREIGN KEY (user_code) REFERENCES MT_USER (user_code);
ALTER TABLE TR_SALES ADD CONSTRAINT IDX_TR_SALES_FK1 FOREIGN KEY (customer_code) REFERENCES MT_CUSTOMER (customer_code);
ALTER TABLE TR_SALES ADD CONSTRAINT IDX_TR_SALES_FK2 FOREIGN KEY (item_code) REFERENCES MT_ITEM (item_code);

