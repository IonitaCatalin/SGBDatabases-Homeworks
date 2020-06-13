DROP TABLE log_table;
/
CREATE USER STUDENT2 IDENTIFIED BY STUDENT;
/
GRANT ALL PRIVILEGES TO STUDENT2;
/
CREATE TABLE log_table(
  user_implicat VARCHAR2(20),
  id_nota NUMBER,
  id_student NUMBER,
  id_curs CHAR(4),
  valoare_veche NUMBER(2),
  valoare_noua NUMBER(2),
  data_modificare TIMESTAMP,
  actiune CHAR(10)
);
/
CREATE OR REPLACE TRIGGER db_logon
AFTER LOGON ON DATABASE
BEGIN
   IF (UPPER(USER)='STUDENT2') THEN
               EXECUTE IMMEDIATE 'ALTER SESSION SET current_schema=STUDENT';
               DBMS_APPLICATION_INFO.set_module(USER, 'Current_Schema Set');
   END IF;
END;

CREATE OR REPLACE TRIGGER trg_log_table
AFTER INSERT OR UPDATE OR DELETE ON note
FOR EACH ROW
DECLARE
    v_user varchar2(30);
BEGIN
    SELECT USER INTO v_user FROM DUAL;
	IF UPDATING THEN
		INSERT INTO log_table (user_implicat,id_nota,id_student, id_curs, valoare_veche,valoare_noua, data_modificare, actiune)
		VALUES (v_user,:old.id,:old.id_student,:old.id_curs,:old.valoare, :new.valoare, SYSTIMESTAMP,'UPDATE');
	END IF;
	IF INSERTING THEN
		INSERT INTO log_table (user_implicat,id_nota,id_student, id_curs, valoare_veche,valoare_noua, data_modificare, actiune)
		VALUES (v_user,:new.id,:new.id_student, :new.id_curs, :new.valoare,:new.valoare,SYSTIMESTAMP,'INSERT');
	END IF;
	IF DELETING THEN
		INSERT INTO log_table (user_implicat,id_nota,id_student, id_curs, valoare_veche,valoare_noua, data_modificare, actiune)
		VALUES (v_user,:old.id_student, :old.id_curs, :old.valoare, :old.valoare,:old.valoare,SYSTIMESTAMP, 'DELETE');
	END IF;
END;
/
DELETE FROM note WHERE id=2 AND id_curs=2;
UPDATE note SET valoare=10 WHERE id=520 AND id_curs=16;
SELECT * FROM log_table;





