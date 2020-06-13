package org.pagination;

import java.sql.*;
import java.util.Scanner;

/**
 * Hello world!
 *
 */

public class App 
{
    public static int numberOfPages(int entitiesPerPage)
    {
        try (Connection conn = DriverManager.getConnection(
                "jdbc:oracle:thin:@localhost:1521:XE", "student", "STUDENT")) {

            if (conn != null) {
                String query="SELECT CEIL(COUNT(*)/?) FROM STUDENTI stud JOIN NOTE nt ON nt.ID_STUDENT=stud.ID JOIN CURSURI curs on nt.ID_CURS = curs.ID";
                PreparedStatement getCountStmt= conn.prepareStatement(query);
                getCountStmt.setInt(1,entitiesPerPage);
                ResultSet res = getCountStmt.executeQuery();
                if(res.next())
                {
                    return res.getInt(1);
                }
            } else {
                System.out.println("Conexiunea la baza de date a esuat!");
            }

        } catch (SQLException e) {
            System.err.format("SQL: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    public static void printPage(int pageSize, int pageIndex)
    {
        try (Connection conn = DriverManager.getConnection(
                "jdbc:oracle:thin:@localhost:1521:XE", "student", "STUDENT")) {

            /*
            SELECT * FROM(
            SELECT inner_query.*, rownum r_val
            FROM(
                SELECT * FROM STUDENTI stud JOIN NOTE nt ON nt.ID_STUDENT=stud.ID
                JOIN CURSURI curs on nt.ID_CURS = curs.ID order by stud.ID ASC) inner_query
            WHERE rownum < ((pageNumber * pageSize) + 1 ))
            WHERE r_val >= (((pageNumber-1) * pageSize) + 1)
            */
            if (conn != null) {
                String query="SELECT * FROM ( SELECT inner_query.*, rownum r_val FROM (  SELECT * FROM STUDENTI stud JOIN NOTE nt ON nt.ID_STUDENT=stud.ID JOIN CURSURI curs on nt.ID_CURS = curs.ID order by stud.ID ASC ) inner_query WHERE rownum < ((? * ?) + 1 ) ) WHERE r_val >= (((?-1) * ?) + 1)";
                PreparedStatement getPageStmt= conn.prepareStatement(query);
                getPageStmt.setInt(1,pageIndex);
                getPageStmt.setInt(2,pageSize);
                getPageStmt.setInt(3,pageIndex);
                getPageStmt.setInt(4,pageSize);
                ResultSet res = getPageStmt.executeQuery();
                while(res.next())
                {
                    System.out.println(res.getString(1)+" "+res.getString(2)+" "+res.getString(3)+" "+res.getString(4)+" "+res.getString(5)+" "+res.getString(6)+" "+res.getString(7));
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
        System.out.print("Number of rows per page is:");
        Scanner scanner=new Scanner(System.in);
        int rowsPerPage=scanner.nextInt();
        int pageCount=numberOfPages(rowsPerPage);
        int currentPage=1;
        while(true)
        {
            System.out.println("Page index:"+currentPage+"/"+pageCount);
            System.out.print("Jump to:");
            int jumpTo=scanner.nextInt();
            if(jumpTo==0)
                break;
            else
            {
                if(jumpTo<=pageCount) {
                    currentPage = jumpTo;
                    printPage(rowsPerPage, jumpTo);
                }
                else
                {
                    System.out.println("Page index does not exists");
                }
            }
        }

    }
}
