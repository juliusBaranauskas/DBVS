import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.*;
import java.util.Scanner;
import java.util.Date.*;
import java.sql.Date.*;


public static class SQLExecutor{

    public static void registerCustomer(Connection postGresConn, Customer customer)
    {
        if(postGresConn == null) {
            System.out.println("We should never get here.");
            return;
        }

        Statement stmt = null ;
        try {
            stmt = postGresConn.createStatement();
            stmt.executeUpdate("INSERT INTO juba5766.Customer(First_name, Last_name, Email, Phone_number) " +
                    "VALUES ('"+ customer.First_name +"', '"+ customer.Last_name +"', '"+ customer.Email +"', '"+ customer.Phone_number +"')");
        }
        catch (SQLException e) {
            System.out.println("SQL Error!");
            e.printStackTrace();
        }
        finally {
            try {
                if(null != stmt)
                    stmt.close() ;
            }
            catch (SQLException exp) {
                System.out.println("Unexpected SQL Error!");
                exp.printStackTrace();
            }
        }
    }

    public static void removeItem(Connection postGresConn, String itemId)
    {
        if(postGresConn == null) {
            System.out.println("We should never get here.");
            return;
        }

        Statement stmt = null ;
        try {
            stmt = postGresConn.createStatement();
            stmt.executeUpdate("DELETE FROM juba5766.Rentable_item WHERE Id = " + itemId);
        }
        catch (SQLException e) {
            System.out.println("SQL Error!");
            e.printStackTrace();
        }
        finally {
            try {
                if(null != stmt)
                    stmt.close() ;
            }
            catch (SQLException exp) {
                System.out.println("Unexpected SQL Error!");
                exp.printStackTrace();
            }
        }
    }

    public static void showItems(Connection postGresConn){
        if(postGresConn == null) {
            System.out.println("We should never get here.");
            return;
        }

        Statement stmt = null ;
        ResultSet rs = null ;
        try {
            stmt = postGresConn.createStatement();
            rs = stmt.executeQuery("SELECT Serial_number, Price_per_day, Item_name, Item_type from juba5766.Rentable_item");
            System.out.println("Serial_number" + "\t|\t" + "Price_per_day" + "\t|\t" + "Item_name" + "\t|\t Item_type");
            while (rs.next()){
                System.out.println(rs.getString("Serial_number") + "\t|\t" + rs.getFloat("Price_per_day") + "\t|\t" + rs.getString("Item_name") + "\t|\t" + rs.getString("Item_type"));
            }
        }
        catch (SQLException e) {
            System.out.println("SQL Error!");
            e.printStackTrace();
        }
        finally {
            try {
                if(null != rs)
                    rs.close() ;
                if(null != stmt)
                    stmt.close() ;
            }
            catch (SQLException exp) {
                System.out.println("Unexpected SQL Error!");
                exp.printStackTrace();
            }
        }
    }

    public static void searchCameraByName(Connection postGresConn, String searchString)
    {
        if(postGresConn == null) {
            System.out.println("We should never get here.");
            return;
        }

        Statement stmt = null ;
        ResultSet rs = null ;
        try {
            stmt = postGresConn.createStatement();
            rs = stmt.executeQuery("SELECT Name, Brand, Model, MP_count, Lens_mount_type, MemCard_slot from juba5766.Camera WHERE Name LIKE '%" + searchString + "%'");
            while (rs.next()){
                System.out.println(rs.getString("Name") + "\t|\t" + rs.getString("Brand") + "\t|\t" + rs.getString("Model") + "\t|\t" + rs.getFloat("MP_count") + "\t|\t" +
                        rs.getString("Lens_mount_type") + "\t|\t" +rs.getString("MemCard_slot"));
            }
        }
        catch (SQLException e) {
            System.out.println("SQL Error!");
            e.printStackTrace();
        }
        finally {
            try {
                if(null != rs)
                    rs.close() ;
                if(null != stmt)
                    stmt.close() ;
            }
            catch (SQLException exp) {
                System.out.println("Unexpected SQL Error!");
                exp.printStackTrace();
            }
        }
    }

    public static void setItemReturned(Connection postGresConn, String date_returned, int rent_num)
    {
        if(postGresConn == null) {
            System.out.println("We should never get here.");
            return;
        }

        Statement stmt = null ;
        try {
            stmt = postGresConn.createStatement();
            stmt.executeUpdate("UPDATE juba5766.Rent SET Date_returned = to_Date('" + date_returned +"', 'yyyy/mm/dd') WHERE Rent_number = " + rent_num);
        }
        catch (SQLException e) {
            System.out.println("SQL Error!");
            e.printStackTrace();
        }
        finally {
            try {
                if(null != stmt)
                    stmt.close() ;
            }
            catch (SQLException exp) {
                System.out.println("Unexpected SQL Error!");
                exp.printStackTrace();
            }
        }
    }

    public static void viewAvailableCameras(Connection postGresConn)
    {
        if(postGresConn == null) {
            System.out.println("We should never get here.");
            return;
        }

        Statement stmt = null ;
        ResultSet rs = null ;
        try {
            stmt = postGresConn.createStatement();
            rs = stmt.executeQuery("SELECT Name, 2 from juba5766.Available_cameras");
            while (rs.next()){
                System.out.println(rs.getString("Name") + "\t|\t" + rs.getInt(2));
            }
        }
        catch (SQLException e) {
            System.out.println("SQL Error!");
            e.printStackTrace();
        }
        finally {
            try {
                if(null != rs)
                    rs.close() ;
                if(null != stmt)
                    stmt.close() ;
            }
            catch (SQLException exp) {
                System.out.println("Unexpected SQL Error!");
                exp.printStackTrace();
            }
        }
    }

    public static void viewAvailableLenses(Connection postGresConn)
    {
        if(postGresConn == null) {
            System.out.println("We should never get here.");
            return;
        }

        Statement stmt = null ;
        ResultSet rs = null ;
        try {
            stmt = postGresConn.createStatement();
            rs = stmt.executeQuery("SELECT Name, 2 from juba5766.Available_lenses");
            while (rs.next()){
                System.out.println(rs.getString("Name") + "\t|\t" + rs.getInt(2));
            }
        }
        catch (SQLException e) {
            System.out.println("SQL Error!");
            e.printStackTrace();
        }
        finally {
            try {
                if(null != rs)
                    rs.close() ;
                if(null != stmt)
                    stmt.close() ;
            }
            catch (SQLException exp) {
                System.out.println("Unexpected SQL Error!");
                exp.printStackTrace();
            }
        }
    }

    public static void viewItemsTakenBy(Connection postGresConn, int id)
    {
        if(postGresConn == null) {
            System.out.println("We should never get here.");
            return;
        }
        System.out.println(id);
        Statement stmt = null ;
        ResultSet rs = null ;
        try {
            stmt = postGresConn.createStatement();
            rs = stmt.executeQuery("SELECT Serial_number, Name, Date_taken, Return_date FROM juba5766.Taken_cameras WHERE Customer = "+ Integer.toString(id) +
                    " UNION SELECT Serial_number, Name, Date_taken, Return_date FROM juba5766.Taken_lenses WHERE Customer = "+ Integer.toString(id) );
            while (rs.next()){
                System.out.println(rs.getInt("Serial_number") + "\t|\t" + rs.getString("Name").toString()  + "\t|\t" + rs.getDate("Date_taken").toString() + "\t|\t" + rs.getDate("Return_date").toString());
            }
        }
        catch (SQLException e) {
            System.out.println("SQL Error!");
            e.printStackTrace();
        }
        finally {
            try {
                if(null != rs)
                    rs.close() ;
                if(null != stmt)
                    stmt.close() ;
            }
            catch (SQLException exp) {
                System.out.println("Unexpected SQL Error!");
                exp.printStackTrace();
            }
        }
    }

    public static void viewCameraInfo(Connection postGresConn, String camName)
    {
        if(postGresConn == null) {
            System.out.println("We should never get here.");
            return;
        }

        Statement stmt = null ;
        ResultSet rs = null ;
        try {
            stmt = postGresConn.createStatement();
            rs = stmt.executeQuery("SELECT Name, Brand, Model, MP_count, Lens_mount_type, MemCard_slot from juba5766.Camera WHERE Name = '" + camName + "'");
            while (rs.next()){
                System.out.println(rs.getString("Name") + "\t|\t" + rs.getString("Brand") + "\t|\t" + rs.getString("Model") + "\t|\t" + rs.getFloat("MP_count") + "\t|\t" +
                        rs.getString("Lens_mount_type") + "\t|\t" +rs.getString("MemCard_slot"));
            }
        }
        catch (SQLException e) {
            System.out.println("SQL Error!");
            e.printStackTrace();
        }
        finally {
            try {
                if(null != rs)
                    rs.close() ;
                if(null != stmt)
                    stmt.close() ;
            }
            catch (SQLException exp) {
                System.out.println("Unexpected SQL Error!");
                exp.printStackTrace();
            }
        }
    }

    static int getId(Connection postGresConn, String email, String phone)
    {
        if(postGresConn == null) {
            System.out.println("We should never get here.");
            return -1;
        }

        int id = -1;
        Statement stmt = null;
        ResultSet rs = null;
        try
        {
            stmt = postGresConn.createStatement();
            rs = stmt.executeQuery("SELECT Id FROM juba5766.Customer WHERE Email = '" + email + "' AND Phone_number = '" + phone + "' ");
            while (rs.next()){
                id = rs.getInt("Id");
            }
        }
        catch (SQLException e) {
            System.out.println("SQL Error!");
            e.printStackTrace();
        }
        finally {
            try {
                if(null != rs)
                    rs.close() ;
                if(null != stmt)
                    stmt.close() ;
            }
            catch (SQLException exp) {
                System.out.println("Unexpected SQL Error!");
                exp.printStackTrace();
            }
        }
        return id;
    }
}