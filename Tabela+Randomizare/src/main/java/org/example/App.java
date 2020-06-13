package org.example;


import com.github.javafaker.Faker;
import javax.swing.plaf.nimbus.State;
import javax.xml.transform.Result;
import java.awt.*;
import java.sql.*;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Random;

public class App
{
    public static String randomFormatDate()
    {
        Random random=new Random();

        int day = (random.nextInt(27)+1);
        int year=(random.nextInt(20)+2000);
        int month=(random.nextInt(12)+1);
        System.out.println(year);
        String date="";
        date=(day<10?("0"+day): String.valueOf(day))+"-";
        date+=(new DateFormatSymbols().getMonths()[month-1]).substring(0,3).toUpperCase()+"-";
        date+=(year%100);
         return date;

    }
    public static void addRandomRows()
    {
        try( Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "student", "STUDENT");)
        {
            Random random=new Random();
            Faker faker=new Faker();
            Statement getIds=conn.createStatement();
            Statement rowCount=conn.createStatement();
            Statement insertRow=conn.createStatement();
            String insertRowTemplate="INSERT INTO VACANTA VALUES(";
            ResultSet ids=getIds.executeQuery("SELECT id from STUDENTI ORDER BY id");
            int counter=0;
            while(ids.next())
            {
                String insertRowQuery=insertRowTemplate;
                insertRowQuery+=(counter++)+","+ids.getString(1)+","+"'"+faker.country().capital().replaceAll("'"," ")+
                        "'"+","+"'"+randomFormatDate().format(randomFormatDate())+"'"+","+
                        (random.nextInt(2)==1?"'DA'":"'NU'")+","+random.nextFloat()*1000+","+
                        "'"+random.nextInt(40)+"'"+")";
                System.out.println(insertRowQuery);
                insertRow.executeQuery(insertRowQuery);
            }
            conn.close();
            System.out.printf("Done");

        } catch (SQLException e) {
            System.err.format("SQL: %s\n%s", e.getSQLState(), e.getMessage());
        }
    }
    public static void dropAndCreateTable(){

        try(Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "student", "STUDENT");)
        {

            Statement dropAndCreateTable=conn.createStatement();
            String dropQueryString="drop table VACANTA";
            String createQueryString="create table VACANTA(id NUMBER,id_student NUMBER,destinatie VARCHAR(60),data_plecarii DATE,all_inclusive CHAR(2),estimare_pret NUMBER(12,6),numar_persoane VARCHAR2(2))";
            dropAndCreateTable.addBatch(dropQueryString);
            dropAndCreateTable.addBatch(createQueryString);
            dropAndCreateTable.executeBatch();
            System.out.printf("The table was created succesfully after dropping it!");
            conn.close();

        } catch (SQLException e) {

            if(e.getErrorCode()==17081)
            {
                try( Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "student", "STUDENT");) {
                    System.out.println("The table was dropped succesfully,now creating ...!");
                    Statement createTable = conn.createStatement();
                    String createQueryString="create table VACANTA(id NUMBER,id_student NUMBER,destinatie VARCHAR(60),data_plecarii DATE,all_inclusive CHAR(2),estimare_pret NUMBER(12,6),numar_persoane VARCHAR2(2))";
                    createTable.executeQuery(createQueryString);


                }
                catch (SQLException exception)
                {
                    System.err.format("SQL: %s\n%s", e.getSQLState(), e.getMessage());
                }
            }
            else
            {
                System.err.format("SQL: %s\n%s", e.getSQLState(), e.getMessage());
            }
        }

    }
    public static void main( String[] args )
    {
        dropAndCreateTable();
        addRandomRows();
    }
}
