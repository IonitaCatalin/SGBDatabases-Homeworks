CREATE OR REPLACE PROCEDURE exportCatalogAsXML
IS
    CURSOR catalog_cursor IS SELECT ID,NR_MATRICOL,NUME,PRENUME FROM STUDENTI;
    v_xml CLOB;
    v_note_clob CLOB;
    v_student_clob CLOB;
    v_catalog_clob CLOB;
    v_file UTL_FILE.FILE_TYPE;
    v_amount NUMBER:=32500;
    v_length NUMBER;
    v_buffer VARCHAR2(32760);
    v_offset NUMBER:=1;


BEGIN

     DBMS_LOB.createtemporary(v_catalog_clob, cache => true, dur => dbms_lob.call);
    FOR v_iterator IN catalog_cursor
    LOOP
        SELECT XMLAGG(XMLELEMENT("nota",
                    XMLATTRIBUTES(nota.ID as "idNota",nota.ID_CURS AS "idCurs",curs.TITLU_CURS as "titluCurs",curs.SEMESTRU as "semestru"),
                    nota.VALOARE
            )).getClobVal() INTO v_note_clob
        FROM NOTE nota
        JOIN CURSURI curs ON curs.ID=nota.ID_CURS
        WHERE nota.ID_STUDENT=v_iterator.ID;
        SELECT XMLAGG(XMLELEMENT("student",
                    XMLATTRIBUTES(v_iterator.NUME||v_iterator.PRENUME as "nume",v_iterator.ID AS "id",v_iterator.NR_MATRICOL AS "nrMatricol"),
                    v_note_clob
            )).getClobVal()
        INTO v_student_clob
        FROM DUAL;
        DBMS_LOB.APPEND(v_catalog_clob,v_student_clob);
    END LOOP;
    SELECT XMLELEMENT("catalog",v_catalog_clob).getClobVal() INTO v_xml FROM DUAL;
    v_file:=UTL_FILE.FOPEN('MYDIR','export2.xml','A',32760);
    v_length:=NVL(dbms_lob.getlength(v_xml), 0);

    DBMS_OUTPUT.PUT_LINE(v_length);

    WHILE(v_offset < v_length) LOOP
        DBMS_LOB.READ(v_xml,v_amount,v_offset,v_buffer);
        UTL_FILE.PUT(v_file,v_buffer);
        --UTL_FILE.FFLUSH(v_file);
        --UTL_FILE.NEW_LINE(v_file);
        UTL_FILE.FCLOSE(v_file);
        v_offset:=v_offset+v_amount;
        v_file:=UTL_FILE.FOPEN('MYDIR','export2.xml','A',32760);

    END LOOP;

    UTL_FILE.FCLOSE(v_file);

END;

BEGIN
    exportCatalogAsXML();
END;

COMMIT;

