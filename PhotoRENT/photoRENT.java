import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.*;
import java.util.Scanner;

public class photoRENT {

    static Scanner scanner = new Scanner(System.in);

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


    static int askCustomer(){
        System.out.println("Enter 1 to view cameras available for rent");
        System.out.println("Enter 2 to view lenses available for rent");
        System.out.println("Enter 3 to see your taken items");
        System.out.println("Enter 4 to view information about specific item");
        System.out.println("Enter 5 to ");
        System.out.println("Enter 6 to ");
        System.out.println("Enter 1 to ");

        switch (scanner.nextInt()){
            case 1:
                return 1; // Enum.viewCameras
            case 2:
                return 2;
            case 3:
                return 3;
            case 4:
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
            case 1:
                return 1; // Enum.viewCameras
            case 2:
                return 2;
            case 3:
                return 3;
            case 4:
                return 4;
            default:
                System.out.println("Wrong key entered, try again");
                return askEmployee();
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
        String mail = "", phone = "";
        System.out.println("Please enter your email address: ");

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        mail = bufferedReader.readLine();
        System.out.println("Please enter your phone number: ");

        phone = bufferedReader.readLine();
        return getId(postGresConn, mail, phone);
    }


    static int getId(Connection postGresConn, String email, String phone)
    {
        if(postGresConn == null) {
            System.out.println("We should never get here.");
            return -1;
        }

        Statement stmt = null ;
        ResultSet rs = null ;
        try {
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
                case 1:
                    viewAvailableCameras(con);
                    break;
                case 2:
                    //viewAvailableLenses();
                    break;
                case 3:
                    //viewItemsTakenBy();
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