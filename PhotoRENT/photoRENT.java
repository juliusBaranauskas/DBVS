import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.*;
import java.util.Scanner;
import java.util.Date.*;
import java.sql.Date.*;

public class photoRENT {

    static Scanner scanner = new Scanner(System.in);

    enum CustomerActions{
        AVAILABLE_CAMERAS = 1,
        AVAILABLE_LENSES,
        ITEMS_TAKEN_BY,
        CAMERA_INFO,
        LENS_INFO
    }

    enum EmployeeActions{
        AVAILABLE_CAMERAS = 1,
        AVAILABLE_LENSES,
        TAKEN_ITEMS,
        CAMERA_INFO,
        REGISTER_CUSTOMER,
        REGISTER_CAMERA,
        REGISTER_LENS,
        REMOVE_CUSTOMER,
        REMOVE_ITEM,
        REGISTER_RENT

    }

    /********************************************************/
    public static void loadDriver()
    {
        try {
            Class.forName("org.postgresql.Driver");
        }
        catch (ClassNotFoundException cnfe) {
            System.out.println("Couldn't find driver class!");
            cnfe.printStackTrace();
            System.exit(1);
        }
    }
    /********************************************************/
    public static Connection getConnection(String conSetting, String conValidator)
    {
        Connection postGresConn = null;
        try {
            postGresConn = DriverManager.getConnection("jdbc:postgresql://pgsql3.mif/studentu", conValidator, conSetting) ;
        }
        catch (SQLException sqle) {
            System.out.println("Couldn't connect to database!");
            sqle.printStackTrace();
            return null ;
        }
        System.out.println("Successfully connected to Postgres Database");

        return postGresConn ;
    }
    /********************************************************/
    public static int countBooks(Connection postGresConn)
    {
        int nofBooks = -1 ;

        if(postGresConn == null) {
            System.out.println("We should never get here.");
            return nofBooks ;
        }

        Statement stmt = null ;
        ResultSet rs = null ;
        try {
            stmt = postGresConn.createStatement();
            rs = stmt.executeQuery("SELECT COUNT(*) from juba5766.Lens");
            rs.next();
            nofBooks = rs.getInt(1);
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
        return nofBooks ;
    }

    public static void registerCustomer(Connection postGresConn, Customer customer)
    {
        if(postGresConn == null) {
            System.out.println("We should never get here.");
            return;
        }

        Statement stmt = null ;
        ResultSet rs = null ;
        try {
            stmt = postGresConn.createStatement();
            rs = stmt.executeQuery("INSERT INTO juba5766.Customer(First_name, Last_name, Email, Phone_number)" +
                    "VALUES ('"+ customer.First_name +"', '"+ customer.Last_name +"', '"+ customer.Email +"', '"+ customer.Phone_number +"')");
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

    public static void removeItem(Connection postGresConn, int itemId)
    {
        if(postGresConn == null) {
            System.out.println("We should never get here.");
            return;
        }

        Statement stmt = null ;
        ResultSet rs = null ;
        try {
            stmt = postGresConn.createStatement();
            rs = stmt.executeQuery("DELETE FROM juba5766.Rentable_item WHERE Id = " + Integer.toString(itemId));
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

    public static void searchCameraByName(Connection postGresConn, string searchString)
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

        Statement stmt = null ;
        ResultSet rs = null ;
        try {
            stmt = postGresConn.createStatement();
            rs = stmt.executeQuery("SELECT Serial_number, Name, Date_taken, Return_date FROM juba5766.Taken_cameras WHERE Customer = "+ id +
                    " UNION SELECT Serial_number, Name, Date_taken, Return_date FROM juba5766.Taken_lenses WHERE Customer = "+ id );
            while (rs.next()){
                System.out.println(rs.getInt("Serial_number") + "\t|\t" + rs.getString("Name") + rs.getDate("Date_taken") + "\t|\t" + rs.getDate("Return_date"));
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

    public static void viewCameraInfo(Connection postGresConn, string camName)
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


    static int askCustomer(){
        System.out.println("Enter 1 to VIEW cameras available for rent");
        System.out.println("Enter 2 to VIEW lenses available for rent");
        System.out.println("Enter 3 to VIEW your taken items");
        System.out.println("Enter 4 to VIEW information about specific camera");
        System.out.println("Enter 5 to ");
        System.out.println("Enter 6 to ");

        switch (scanner.nextInt()){
            case CustomerActions.AVAILABLE_CAMERAS:
                return 1;
            case CustomerActions.AVAILABLE_LENSES:
                return 2;
            case CustomerActions.ITEMS_TAKEN_BY:
                return 3;
            case CustomerActions.CAMERA_INFO:
                return 4;
            default:
                System.out.println("Wrong key entered, try again");
                return askCustomer();
        }
    }

    static int askEmployee(){
        System.out.println("Enter 1 to VIEW cameras available for rent");
        System.out.println("Enter 2 to VIEW lenses available for rent");
        System.out.println("Enter 3 to VIEW taken items");
        System.out.println("Enter 4 to VIEW information about specific camera");
        System.out.println("Enter 5 to REGISTER new customer");
        System.out.println("Enter 6 to REGISTER new camera");
        System.out.println("Enter 7 to REGISTER new lens");
        System.out.println("Enter 8 to REMOVE an item");
        System.out.println("Enter 9 to REGISTER new Rent of an item");
/*
        switch (scanner.nextInt()){
            case EmployeeActions.AVAILABLE_CAMERAS:
                return 1;
            case EmployeeActions.AVAILABLE_LENSES:
                return 2;
            case EmployeeActions.TAKEN_ITEMS:
                return 3;
            case EmployeeActions.CAMERA_INFO:
                return 4;
            case EmployeeActions.REGISTER_CUSTOMER:
                return 5;
            case EmployeeActions.REGISTER_CAMERA:
                return 6;
            case EmployeeActions.REGISTER_LENS:
                return 7;
            case EmployeeActions.REMOVE_ITEM:
                return 8;
            case EmployeeActions.REGISTER_RENT:
                return 9;
            default:
                System.out.println("Wrong key entered, try again");
                return askCustomer();
        }*/
        int choice = scanner.nextInt();
        return choice <= EmployeeActions.values().length && choice > 0 ? (int)EmployeeActions.values()[choice] : askCustomer();
    }

    static boolean askForType()
    {
        System.out.println("Enter 1 to enter Customer menu\nEnter 2 to enter Employee menu");
        int choice = scanner.nextInt();

        if(choice == 1){
            return true;
        }else if (choice == 2){
            return false;
        }else{
            System.out.println("Wrong number entered");
            return askForType();
        }
    }

    static int getCustomerId(Connection postGresConn)
    {
        String mail = "",
                phone = "";
        System.out.println("Please enter your email address: ");

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		try
        {
			mail = bufferedReader.readLine();
			System.out.println("Please enter your phone number: ");
			phone = bufferedReader.readLine();
		}
		catch(IOException e){
			System.out.println("Please enter your phone number: ");
		}

        return getId(postGresConn, mail, phone);
    }


    static int getId(Connection postGresConn, String email, String phone)
    {
        if(postGresConn == null) {
            System.out.println("We should never get here.");
            return -1;
        }

        Statement stmt = null;
        ResultSet rs = null;
        try
        {
            stmt = postGresConn.createStatement();
            rs = stmt.executeQuery("SELECT Id FROM juba5766.Customer WHERE Email = '" + email + "' AND Phone_number = '" + phone + "' ");
            while (rs.next()){
                System.out.println(rs.getInt("Id"));
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
        return -1;
    }

    /********************************************************/
    public static void main(String[] args)
    {
        loadDriver();
        Connection con = getConnection(args[0], args[1]);
        boolean isCustomer = askForType();
        int customerId = -1;

        if(isCustomer){
            customerId = getCustomerId(con);
            switch (askCustomer()){
                case CustomerActions.AVAILABLE_CAMERAS:
                    viewAvailableCameras(con);
                    break;
                case CustomerActions.AVAILABLE_LENSES:
                    viewAvailableLenses(con);
                    break;
                case CustomerActions.ITEMS_TAKEN_BY:
                    viewItemsTakenBy(con, customerId);
                    break;
                case CustomerActions.CAMERA_INFO:
                    //viewCameraInfo(con, askCameraName());
                    break;
                default:
                    break;
            }
        }else{
            askEmployee();
        }
        if( null != con ) {
            int nofBooks = countBooks(con);
            System.out.println("Number of lenses: " + nofBooks);
        }
        if( null != con ) {
            try {
                con.close() ;
            }
            catch (SQLException exp) {
                System.out.println("Can not close connection!");
                exp.printStackTrace();
            }
        }
    }
    /********************************************************/
}
