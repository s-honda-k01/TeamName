DROP TABLE TR_MEMO_SHARE_MEMBER CASCADE CONSTRAINTS;
DROP TABLE TR_MEMO CASCADE CONSTRAINTS;
DROP TABLE TR_SALES CASCADE CONSTRAINTS;
DROP TABLE MT_CUSTOMER CASCADE CONSTRAINTS;
DROP TABLE MT_ITEM CASCADE CONSTRAINTS;
DROP TABLE MT_ITEM_GENRE CASCADE CONSTRAINTS;
DROP TABLE MT_USER CASCADE CONSTRAINTS;
DROP TABLE MT_ROLE CASCADE CONSTRAINTS;

CREATE TABLE MT_ROLE(
		role_code                     		VARCHAR2(2)		 NOT NULL		 PRIMARY KEY,
		role_name                     		VARCHAR2(50)		 NOT NULL,
		explain                       		VARCHAR2(64)		 NULL 
);

insert into MT_ROLE values('1','admin','システム管理者');
insert into MT_ROLE values('2','manager','課長');
insert into MT_ROLE values('3','staff','課員');

CREATE TABLE MT_USER(
		user_code                     		VARCHAR2(8)		 NOT NULL		 PRIMARY KEY,
		user_name                     		VARCHAR2(100)		 NOT NULL,
		password                      		VARCHAR2(100)		 NOT NULL,
		role_code                     		VARCHAR2(2)		 NOT NULL,
		validity                      		NUMBER(1)		 NOT NULL,
  FOREIGN KEY (role_code) REFERENCES MT_ROLE (role_code)
);


CREATE TABLE MT_ITEM_GENRE(
		item_genre_code               		VARCHAR2(8)		 NOT NULL		 PRIMARY KEY,
		item_genre_name               		VARCHAR2(100)		 NOT NULL
);

insert into MT_ITEM_GENRE values('00000001','コピー用紙');
insert into MT_ITEM_GENRE values('00000002','クリアファイル');
insert into MT_ITEM_GENRE values('00000003','付箋');

CREATE TABLE MT_ITEM(
		item_code                     		VARCHAR2(8)		 NOT NULL		 PRIMARY KEY,
		item_name                     		VARCHAR2(100)		 NOT NULL,
		item_genre_code               		VARCHAR2(8)		 NOT NULL,
		spec                          		VARCHAR2(300)		 NULL ,
		price                         		NUMBER(7)		 NOT NULL,
		validity                      		NUMBER(1)		 NOT NULL,
  FOREIGN KEY (item_genre_code) REFERENCES MT_ITEM_GENRE (item_genre_code)
);

insert into MT_ITEM values('00000001','コピー用紙A4_01','00000001','A4',3128,1);
insert into MT_ITEM values('00000002','コピー用紙A3_01','00000001','A3',3364,1);
insert into MT_ITEM values('00000003','コピー用紙B5_01','00000001','B5',2901,1);
insert into MT_ITEM values('00000004','クリアファイル_010','00000002','10枚パック',298,1);
insert into MT_ITEM values('00000005','クリアファイル_100','00000002','100枚パック',1478,1);
insert into MT_ITEM values('00000006','付箋_大_010','00000003','大',878,1);
insert into MT_ITEM values('00000007','付箋_中_010','00000003','中',678,1);
insert into MT_ITEM values('00000008','付箋_小_010','00000003','小',568,1);

CREATE TABLE MT_CUSTOMER(
		customer_code                 		VARCHAR2(8)		 NOT NULL		 PRIMARY KEY,
		customer_name                 		VARCHAR2(100)		 NOT NULL,
		validity                      		NUMBER(1)		 NOT NULL
);

insert into MT_CUSTOMER values('C0000001','新橋商店',1);
insert into MT_CUSTOMER values('C0000002','虎ノ門商事',1);
insert into MT_CUSTOMER values('C0000003','霞が関産業',1);

CREATE TABLE TR_SALES(
		sales_id                      		NUMBER(8)		 DEFAULT 1		 NOT NULL		 PRIMARY KEY,
		customer_code                 		VARCHAR2(8)		 NOT NULL,
		user_code                     		VARCHAR2(8)		 NOT NULL,
		item_code                     		VARCHAR2(8)		 NOT NULL,
		quantity                      		NUMBER(4)		 NOT NULL,
		sales_date                    		DATE		 NOT NULL,
  FOREIGN KEY (user_code) REFERENCES MT_USER (user_code),
  FOREIGN KEY (customer_code) REFERENCES MT_CUSTOMER (customer_code),
  FOREIGN KEY (item_code) REFERENCES MT_ITEM (item_code)
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

insert into TR_SALES values(TR_SALES_sales_id_SEQ.NEXTVAL,'C0000001','u003','00000001',1,sysdate);
insert into TR_SALES values(TR_SALES_sales_id_SEQ.NEXTVAL,'C0000002','u003','00000002',2,sysdate);
insert into TR_SALES values(TR_SALES_sales_id_SEQ.NEXTVAL,'C0000003','u004','00000003',3,sysdate);

CREATE TABLE TR_MEMO(
		memo_id                       		NUMBER(8)		 DEFAULT 1		 NOT NULL		 PRIMARY KEY,
		user_code                     		VARCHAR2(8)		 NOT NULL,
		title                         		VARCHAR2(300)		 NULL ,
		content                       		VARCHAR2(1000)		 NOT NULL,
		edit_date                     		DATE		 NOT NULL,
		parent_memo_id                		NUMBER(8)		,
		validity                      		NUMBER(1)		 NOT NULL,
  FOREIGN KEY (user_code) REFERENCES MT_USER (user_code)
);

insert into TR_MEMO values(TR_MEMO_memo_id_SEQ.NEXTVAL,'u002','ManagerTest','ManagerTest',sysdate,null,1);
insert into TR_MEMO values(TR_MEMO_memo_id_SEQ.NEXTVAL,'u003','StaffTest','StaffTest',sysdate,null,1);
insert into TR_MEMO values(TR_MEMO_memo_id_SEQ.NEXTVAL,'u003','Re:ManagerTest','Re:ManagerTest',sysdate,1,1);

DROP SEQUENCE TR_MEMO_memo_id_SEQ;

CREATE SEQUENCE TR_MEMO_memo_id_SEQ NOMAXVALUE NOCACHE NOORDER NOCYCLE;

CREATE TRIGGER TR_MEMO_memo_id_TRG
BEFORE INSERT ON TR_MEMO
FOR EACH ROW
BEGIN
IF :NEW.memo_id IS NULL THEN
  SELECT TR_MEMO_memo_id_SEQ.NEXTVAL INTO :NEW.memo_id FROM DUAL;
END IF;
END;
/

CREATE TABLE TR_MEMO_SHARE_MEMBER(
		share_member_id               		NUMBER(8)		 DEFAULT 1		 NOT NULL		 PRIMARY KEY,
		memo_id                       		NUMBER(8)		 NOT NULL,
		user_code                     		VARCHAR2(8)		 NOT NULL,
  FOREIGN KEY (memo_id) REFERENCES TR_MEMO (memo_id),
  FOREIGN KEY (user_code) REFERENCES MT_USER (user_code)
);

insert into TR_MEMO_SHARE_MEMBER values(TR_MSM_share_member_id_SEQ.NEXTVAL,1,'u003');
insert into TR_MEMO_SHARE_MEMBER values(TR_MSM_share_member_id_SEQ.NEXTVAL,1,'u004');
insert into TR_MEMO_SHARE_MEMBER values(TR_MSM_share_member_id_SEQ.NEXTVAL,3,'u002');
insert into TR_MEMO_SHARE_MEMBER values(TR_MSM_share_member_id_SEQ.NEXTVAL,3,'u004');

DROP SEQUENCE TR_MSM_share_member_id_SEQ;

CREATE SEQUENCE TR_MSM_share_member_id_SEQ NOMAXVALUE NOCACHE NOORDER NOCYCLE;

CREATE TRIGGER TR_MSM_share_member_id_TRG
BEFORE INSERT ON TR_MEMO_SHARE_MEMBER
FOR EACH ROW
BEGIN
IF :NEW.share_member_id IS NULL THEN
  SELECT TR_MSM_share_member_id_SEQ.NEXTVAL INTO :NEW.share_member_id FROM DUAL;
END IF;
END;
/

commit;