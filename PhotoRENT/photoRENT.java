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
        AVAILABLE_CAMERAS,
        AVAILABLE_LENSES,
        ITEMS_TAKEN_BY,
        CAMERA_INFO,
        LENS_INFO
    }

    enum EmployeeActions{
        AVAILABLE_CAMERAS,
        AVAILABLE_LENSES,
        TAKEN_ITEMS,
        CAMERA_INFO,
        LENS_INFO
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


    static int askCustomer(){
        System.out.println("Enter 1 to view cameras available for rent");
        System.out.println("Enter 2 to view lenses available for rent");
        System.out.println("Enter 3 to see your taken items");
        System.out.println("Enter 4 to view information about specific camera");
        System.out.println("Enter 5 to ");
        System.out.println("Enter 6 to ");
        System.out.println("Enter 1 to ");

        switch (scanner.nextInt()){
            case CustomerActions.AVAILABLE_CAMERAS:
                return 1; // Enum.viewCameras
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
        System.out.println("Enter 1 to view cameras available for rent");
        System.out.println("Enter 2 to view lenses available for rent");
        System.out.println("Enter 3 to see taken items");
        System.out.println("Enter 4 to view information about specific item");
        System.out.println("Enter 5 to register new customer");
        System.out.println("Enter 6 to register new item");
        System.out.println("Enter 7 to register new camera");

        switch (scanner.nextInt()){
            case EmployeeActions.AVAILABLE_CAMERAS:
                return 1; // Enum.viewCameras
            case EmployeeActions.AVAILABLE_LENSES:
                return 2;
            case EmployeeActions.ITEMS_TAKEN_BY:
                return 3;
            case EmployeeActions.CAMERA_INFO:
                return 4;
            default:
                System.out.println("Wrong key entered, try again");
                return askCustomer();
        }
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
