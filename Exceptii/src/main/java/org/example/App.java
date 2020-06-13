package org.example;

import java.sql.*;

public class App
{
    private static final int NO_STUDENT_FOUND = 20001;
    private static final int NO_GRADES=20002;
    public static void exceptionExample(String name,String surname) {
        try (Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "student", "STUDENT");) {
            CallableStatement cstmt = conn.prepareCall ("{? = call get_student_grade_avg (?, ?)}");
            cstmt.registerOutParameter (1, Types.FLOAT);
            cstmt.registerOutParameter(2,Types.CHAR);
            cstmt.registerOutParameter(3,Types.CHAR);
            cstmt.setString (2, name);
            cstmt.setString (3, surname);
            cstmt.execute ();
            float stud_avg= cstmt.getFloat (1);
            System.out.println("Media studentul"+name+" "+surname+"este:"+stud_avg);
        } catch (SQLException e) {
            switch (e.getErrorCode())
            {

                case NO_STUDENT_FOUND:
                {
                    System.out.println("A fost prinsa o exceptie:"+e.getErrorCode());
                    System.out.println("Studentul "+name+" "+surname+" mentionat nu exista!");
                    break;
                }
                case NO_GRADES:
                {
                    System.out.println("A fost prinsa o exceptie"+e.getErrorCode());
                    System.out.println("Studentul "+name+" "+surname+ " mentionat nu are note inregistrate in baza de date!");
                    break;
                }
                default:
                {
                    System.out.println("SQL:"+e.getErrorCode()+" "+e.getMessage());
                }
            }
        }
    }
        public static void main( String[] args )
    {
        //exceptionExample("Silitra","Diana Anca");
        exceptionExample("Ciucanu","Nicusor");
    }
}
