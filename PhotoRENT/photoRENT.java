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
        REGISTER_CUSTOMER,
        REGISTER_CAMERA,
        REGISTER_LENS,
        REMOVE_ITEM,
        REGISTER_RENT,
        SEARCH_CAMERA

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


    static Customer getCustomer(){
        String email = "", phone_number="", fname="", lname="";
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        try
        {
            System.out.println("Please enter your First name: ");
            fname = bufferedReader.readLine();
            System.out.println("Please enter your Last name: ");
            lname = bufferedReader.readLine();
            System.out.println("Please enter your email: ");
            email = bufferedReader.readLine();
            System.out.println("Please enter your phone number: ");
            phone_number = bufferedReader.readLine();
        }
        catch(IOException e){
            e.printStackTrace();
        }
        Customer customer = new Customer(fname, lname, email, phone_number);
        return customer;
    }

    static CustomerActions askCustomer(){
        System.out.println("Enter 0 to VIEW cameras available for rent");
        System.out.println("Enter 1 to VIEW lenses available for rent");
        System.out.println("Enter 2 to VIEW your taken items");
        System.out.println("Enter 3 to VIEW information about specific camera");

        int choice = scanner.nextInt();
        return choice <= CustomerActions.values().length && choice >= 0 ? CustomerActions.values()[choice] : askCustomer();
    }

    static EmployeeActions askEmployee(){
        System.out.println("Enter 0 to VIEW cameras available for rent");
        System.out.println("Enter 1 to VIEW lenses available for rent");
        System.out.println("Enter 2 to VIEW taken items");
        System.out.println("Enter 3 to VIEW information about specific camera");
        System.out.println("Enter 4 to REGISTER new customer");
        System.out.println("Enter 5 to REGISTER new camera");
        System.out.println("Enter 6 to REGISTER new lens");
        System.out.println("Enter 7 to REMOVE an item");
        System.out.println("Enter 8 to REGISTER new Rent of an item");
        System.out.println("Enter 9 to SEARCH for camera");

        int choice = scanner.nextInt();
        return choice <= EmployeeActions.values().length && choice >= 0 ? EmployeeActions.values()[choice] : askEmployee();
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
			e.printStackTrace();
		}

        return getId(postGresConn, mail, phone);
    }

    static String askCameraName(){
        String name ="";
        System.out.println("Please enter camera name: ");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        try
        {
            name = bufferedReader.readLine();
        }
        catch(IOException e){
            System.out.println("Please enter your phone number: ");
        }
        return name;
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

    public static String getSerial(){
        String serial ="";
        System.out.println("Please enter serial number ");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        try
        {
            serial = bufferedReader.readLine();
        }
        catch(IOException e){
            System.out.println("Please enter your phone number: ");
        }
        return serial;
    }

    public static String getSearchString(){
        String searchString ="";
        System.out.println("Please enter camera you want to search for");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        try
        {
            searchString = bufferedReader.readLine();
        }
        catch(IOException e){
            System.out.println("Please enter your phone number: ");
        }
        return searchString;
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
            System.out.println(customerId);
            switch (askCustomer()){
                case AVAILABLE_CAMERAS:
                    viewAvailableCameras(con);
                    break;
                case AVAILABLE_LENSES:
                    viewAvailableLenses(con);
                    break;
                case ITEMS_TAKEN_BY:
                    viewItemsTakenBy(con, customerId);
                    break;
                case CAMERA_INFO:
                    viewCameraInfo(con, askCameraName());
                    break;
                default:
                    System.out.println("NO CORRESPONDING ACTION FOUND");
                    break;
            }
        }else{
            switch (askEmployee()) {
                case AVAILABLE_CAMERAS:
                    viewAvailableCameras(con);
                    break;
                case AVAILABLE_LENSES:
                    viewAvailableLenses(con);
                    break;
                case TAKEN_ITEMS:
                    viewItemsTakenBy(con, customerId);
                    break;
                case CAMERA_INFO:
                    viewCameraInfo(con, askCameraName());
                    break;
                case REGISTER_CUSTOMER:
                    registerCustomer(con, getCustomer());
                    break;
                case REGISTER_CAMERA:
                    break;
                case REGISTER_LENS:
                    break;
                case REMOVE_ITEM:
                    showItems(con);
                    removeItem(con, getSerial());
                    break;
                case REGISTER_RENT:
                    break;
                case SEARCH_CAMERA:
                    searchCameraByName(con, getSearchString());
                    break;
                default:
                    System.out.println("NO CORRESPONDING ACTION FOUND");
                    break;
            }
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
