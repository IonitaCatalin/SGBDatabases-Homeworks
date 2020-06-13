package org.tema;

import org.apache.commons.lang3.SerializationUtils;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.*;

public class App {
    public static void connectAndSerialize()
    {
        TestOnSerialization first=new TestOnSerialization(10,.5f, 12);
        System.out.println("Inainte de serializare si stocare in baza de date:"+first.toString());
        try(Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "student", "STUDENT");) {
            byte[] serializedDataobject = SerializationUtils.serialize(first);
            CallableStatement storeStmt = conn.prepareCall("begin insert_serialized_object_db (SERIALIZED_OBJECT_WRAPPER(?, ?)); end;");
            Blob firstBlob=conn.createBlob();
            firstBlob.setBytes(1,serializedDataobject);
            storeStmt.registerOutParameter(1, Types.VARCHAR);
            storeStmt.registerOutParameter(2, Types.BLOB);
            storeStmt.setString(1, first.getUniqueID());
            storeStmt.setBlob(2, firstBlob);
            storeStmt.execute();

            CallableStatement returnStmt = conn.prepareCall("{? = call get_last_serialized_instance()}");
            returnStmt.registerOutParameter(1,Types.BLOB);
            returnStmt.execute();
            Blob secondBlob=new SerialBlob(returnStmt.getBlob(1));
            TestOnSerialization unserialisedFirst=(TestOnSerialization) SerializationUtils.deserialize(secondBlob.getBytes(1,(int)secondBlob.length()));
            System.out.println("Dupa incarcarea din baza de date si deserializare:"+unserialisedFirst.toString());
        }
        catch (SQLException e) {
            System.err.format("SQL: %s\n%s", e.getSQLState(), e.getMessage());
        }
    }
    public static void main(String[] args)
    {
        connectAndSerialize();
    }
}
