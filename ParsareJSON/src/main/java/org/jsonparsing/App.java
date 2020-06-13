package org.jsonparsing;

import org.json.JSONObject;

import java.sql.*;
import java.util.Scanner;

public class App
{
    public static void printJSON(int id) {

            try (Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "student", "STUDENT");) {
                CallableStatement returnStmt = conn.prepareCall("{? = call friendsToJSON(?)}");
                returnStmt.setInt(2,id);
                returnStmt.registerOutParameter(1, Types.VARCHAR);
                returnStmt.execute();
                JSONObject obj=new JSONObject(returnStmt.getString(1));
                System.out.println("Recomandari de prieteni pentru studentul specficat:");
                System.out.println(obj.getJSONObject("student1").get("id")+" "+obj.getJSONObject("student1").get("nume")+" "+obj.getJSONObject("student1").get("prenume"));
                System.out.println(obj.getJSONObject("student2").get("id")+" "+obj.getJSONObject("student2").get("nume")+" "+obj.getJSONObject("student2").get("prenume"));
                System.out.println(obj.getJSONObject("student3").get("id")+" "+obj.getJSONObject("student3").get("nume")+" "+obj.getJSONObject("student3").get("prenume"));
                System.out.println(obj.getJSONObject("student4").get("id")+" "+obj.getJSONObject("student4").get("nume")+" "+obj.getJSONObject("student4").get("prenume"));
                System.out.println(obj.getJSONObject("student5").get("id")+" "+obj.getJSONObject("student5").get("nume")+" "+obj.getJSONObject("student5").get("prenume"));
            } catch (SQLException e) {
                System.err.format("SQL: %s\n%s", e.getSQLState(), e.getMessage());
            }
        }
    public static void main( String[] args)
    {
        System.out.print("Introduceti id-ul studentului pentru care vreti sa primiti recomandari:");
        Scanner scanner=new Scanner(System.in);
        printJSON(scanner.nextInt());
    }
}
