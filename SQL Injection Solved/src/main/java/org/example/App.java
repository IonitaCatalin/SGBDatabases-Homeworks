package org.example;

import java.sql.*;
import java.util.Scanner;

public class App
{
    public static void printStudentsGrade(String nrMatricol)
    {
        try (Connection conn = DriverManager.getConnection(
                "jdbc:oracle:thin:@localhost:1521:XE", "student", "STUDENT")) {

            if (conn != null) {
                System.out.println("Conexiunea la baza de date s-a realizat cu succes!");
                String query="SELECT NUME,PRENUME,STUDENTI.AN,CURSURI.TITLU_CURS,NOTE.VALOARE FROM STUDENTI JOIN  NOTE ON NOTE.ID=STUDENTI.ID JOIN CURSURI on NOTE.ID_CURS = CURSURI.ID WHERE STUDENTI.NR_MATRICOL=?";
                PreparedStatement getGradeStmt= conn.prepareStatement(query);
                getGradeStmt.setString(1,nrMatricol);
                ResultSet res = getGradeStmt.executeQuery();
                while(res.next())
                {
                    System.out.println(res.getString(1)+" "+res.getString(2)+" "+res.getString(3)+" "+res.getString(4)+" "+res.getString(5));
                }
            } else {
                System.out.println("Conexiunea la baza de date a esuat!");
            }

        } catch (SQLException e) {
            System.err.format("SQL: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main( String[] args )
    {
        System.out.print("Student nr_matricol:");
        Scanner scanner=new Scanner(System.in);
        String nrMatricol=scanner.nextLine();
        printStudentsGrade(nrMatricol);

    }
}
