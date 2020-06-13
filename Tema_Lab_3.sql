CREATE OR REPLACE FUNCTION friendsToJSON(p_stud_id IN STUDENTI.ID%TYPE)
RETURN VARCHAR2
IS
    CURSOR cursor_friends IS SELECT ID_STUDENT2,COUNT(ID_STUDENT2),NUME,PRENUME FROM PRIETENI
                            JOIN STUDENTI stud2 ON PRIETENI.ID_STUDENT2 = stud2.ID
                            WHERE ID_STUDENT1 IN
                                (SELECT ID_STUDENT2 FROM PRIETENI WHERE ID_STUDENT1=p_stud_id)
                            GROUP BY ID_STUDENT2, NUME, PRENUME
                            ORDER BY 2 DESC;
    v_count_friends INTEGER:=0;
    v_friendship_relation INTEGER;
    v_output_json VARCHAR(1000);
BEGIN
    v_output_json:='{';
    FOR v_iterator IN cursor_friends LOOP
        IF v_count_friends<5 THEN
            SELECT COUNT(*) INTO v_friendship_relation FROM PRIETENI WHERE ID_STUDENT1=p_stud_id AND ID_STUDENT2=v_iterator.ID_STUDENT2;
                if(v_friendship_relation=0)
                THEN
                    if v_iterator.ID_STUDENT2 <> p_stud_id THEN
                        IF v_count_friends<>4 THEN
                        v_count_friends:=v_count_friends+1;
                        v_output_json:=v_output_json || '"student'||v_count_friends||'":'||'{'||'"nume":'||'"'||v_iterator.NUME||'"'||',"prenume":'||'"'||v_iterator.PRENUME||'"'||',"id":'||'"'||v_iterator.ID_STUDENT2||'"'||'},';
                        ELSE
                            v_count_friends:=v_count_friends+1;
                            v_output_json:=v_output_json || '"student'||v_count_friends||'":'||'{'||'"nume":'||'"'||v_iterator.NUME||'"'||',"prenume":'||'"'||v_iterator.PRENUME||'"'||',"id":'||'"'||v_iterator.ID_STUDENT2||'"'||'}';
                        END IF;
                    END IF;
                END IF;
        END IF;
    END LOOP;
    v_output_json:=v_output_json||'}';
    RETURN v_output_json;
END;

SELECT friendsToJSON(319) FROM DUAL;