package com.company;

import javax.print.DocFlavor;
import javax.swing.plaf.nimbus.State;
import java.sql.*;

public class Main {

    public static void main(String[] args) throws SQLException {
        try (Connection conn = DriverManager.getConnection(
                "jdbc:oracle:thin:@localhost:1521:XE", "student", "STUDENT")) {

            if (conn != null) {
                System.out.println("Conexiunea la baza de date s-a realizat cu succes!");
                Statement stmt=conn.createStatement();
                Statement secondStmt=conn.createStatement();
                ResultSet rs=stmt.executeQuery("SELECT nume,prenume FROM STUDENTI WHERE nume='Popescu'");
                ResultSet secondRs=secondStmt.executeQuery("SELECT MAX(AVG(VALOARE)) FROM STUDENTI s JOIN NOTE n ON s.id=n.id_student GROUP BY id_student");
                while(rs.next())
                    System.out.println(rs.getString(1)+" "+rs.getString(2));
                while(secondRs.next())
                    System.out.println("Nota maxima din baza de date:"+secondRs.getString(1));
            } else {
                System.out.println("Conexiunea la baza de date a esuat!");
            }

        } catch (SQLException e) {
            System.err.format("SQL: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
