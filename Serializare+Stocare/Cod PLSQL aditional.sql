CREATE SEQUENCE table_of_ids
START WITH 1
INCREMENT BY 1
NOCYCLE;

CREATE OR REPLACE TYPE serialized_object_wrapper AS OBJECT
(
    object_id VARCHAR2(55),
    object_instance BLOB
);
DROP TABLE store_objects;
CREATE TABLE store_objects(id NUMBER,object serialized_object_wrapper);

CREATE OR REPLACE PROCEDURE insert_serialized_object_db(p_new_serialized_obj IN serialized_object_wrapper)
IS
BEGIN
    INSERT INTO store_objects VALUES(table_of_ids.nextval,p_new_serialized_obj);
END;


CREATE OR REPLACE FUNCTION get_last_serialized_instance
RETURN BLOB
IS
    v_object serialized_object_wrapper;
BEGIN
    SELECT OBJECT INTO v_object FROM (
    SELECT * FROM STORE_OBJECTS ORDER BY ID DESC
    ) WHERE ROWNUM = 1;
    RETURN v_object.OBJECT_INSTANCE;
END;



