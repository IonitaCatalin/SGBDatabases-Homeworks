package com.company;

import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class DatabaseController {

    public DefaultTableModel getQueryResult(String query) throws SQLException {

        try (Connection conn = DriverManager.getConnection(
                "jdbc:oracle:thin:@localhost:1521:XE", "student", "STUDENT")) {

            if (conn != null) {
                {
                    DefaultTableModel model = new DefaultTableModel(new String[]{"Nume","Prenume","Bursa"}, 0);
                    Statement stmt=conn.createStatement();
                    System.out.println("Query:"+query);
                    ResultSet rs=stmt.executeQuery(query);
                    while(rs.next())
                    {
                        String name=rs.getString("Nume");
                        String surname=rs.getString("Prenume");
                        String stock=rs.getString("Bursa");
                        model.addRow(new Object[]{name,surname,stock});
                    }
                    return model;
                }
            } else {
                System.out.println("Query-ul la baza de date a esuat!");
                return new DefaultTableModel();
            }

        } catch (SQLException e) {
            System.err.format("SQL: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new DefaultTableModel();
    }

}
