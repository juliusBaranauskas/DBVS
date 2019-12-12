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
    static SQLExecutor _SQLExecutor = new SQLExecutor();

    public enum CustomerActions
    {
        AVAILABLE_CAMERAS,
        AVAILABLE_LENSES,
        ITEMS_TAKEN_BY,
        CAMERA_INFO,
        LENS_INFO
    }

    public enum EmployeeActions
    {
        AVAILABLE_CAMERAS,
        AVAILABLE_LENSES,
        TAKEN_ITEMS,
        CAMERA_INFO,
        REGISTER_CUSTOMER,
        REGISTER_CAMERA,
        REGISTER_LENS,
        REMOVE_ITEM,
        REGISTER_RENT,
        SEARCH_CAMERA,
        SET_RETURNED,
        ISSUE_ITEM
    }

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

    static Customer getCustomer()
    {
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

    static CustomerActions askCustomer()
    {
        System.out.println("Enter 0 to VIEW cameras available for rent");
        System.out.println("Enter 1 to VIEW lenses available for rent");
        System.out.println("Enter 2 to VIEW your taken items");
        System.out.println("Enter 3 to VIEW information about specific camera");

        int choice = scanner.nextInt();
        return choice <= CustomerActions.values().length && choice >= 0 ? CustomerActions.values()[choice] : askCustomer();
    }

    static EmployeeActions askEmployee()
    {
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
        System.out.println("Enter 10 to set item returned");
        System.out.println("Enter 11 to issue item");

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

    static boolean askIfBySerial()
    {
        System.out.println("Enter 1 to remove by serial\nEnter 2 to remove by Id");
        int choice = scanner.nextInt();

        if(choice == 1){
            return true;
        }else if (choice == 2){
            return false;
        }else{
            System.out.println("Wrong number entered");
            return askIfBySerial();
        }
    }

    static boolean askIfCamera()
    {
        System.out.println("Enter 1 if it's a camera\nEnter 2 to if it's lens");
        int choice = scanner.nextInt();

        if(choice == 1){
            return true;
        }else if (choice == 2){
            return false;
        }else{
            System.out.println("Wrong number entered");
            return askIfCamera();
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

        return _SQLExecutor.getId(postGresConn, mail, phone);
    }

    static String askCameraName()
    {
        String name ="";
        System.out.println("Please enter camera name: ");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        try
        {
            name = bufferedReader.readLine();
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return name;
    }

    public static String takeCare(String toTake)
    {
        toTake = "Visurtoks" + toTake;
        return toTake+"("+Integer.toString(231)+")";
    }

    public static String getSerial()
    {
        String serial ="";
        System.out.println("Please enter serial number ");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        try
        {
            serial = bufferedReader.readLine();
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return serial;
    }

    public static String getSearchString()
    {
        String searchString ="";
        System.out.println("Please enter camera you want to search for");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        try
        {
            searchString = bufferedReader.readLine();
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return searchString;
    }

    public static String askDate()
    {
        String searchString ="";
        System.out.println("Please enter date of return (yyyy/mm/dd)");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        try
        {
            searchString = bufferedReader.readLine();
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return searchString;
    }

    public static String askNum()
    {
        String searchString ="";
        System.out.println("Please enter item id from table above");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        try
        {
            searchString = bufferedReader.readLine();
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return searchString;
    }

    /********************************************************/
    public static void main(String[] args)
    {
        loadDriver();
        Connection con = getConnection(takeCare(args[0]), args[1]);
        boolean isCustomer = askForType();
        int customerId = -1;
        boolean keepAsking = true;

        if(isCustomer){
            customerId = getCustomerId(con);
            while(keepAsking){
                switch (askCustomer()){
                    case AVAILABLE_CAMERAS:
                        _SQLExecutor.viewAvailableCameras(con);
                        break;
                    case AVAILABLE_LENSES:
                        _SQLExecutor.viewAvailableLenses(con);
                        break;
                    case ITEMS_TAKEN_BY:
                        _SQLExecutor.viewItemsTakenBy(con, customerId);
                        break;
                    case CAMERA_INFO:
                        _SQLExecutor.viewCameraInfo(con, askCameraName());
                        break;
                    default:
                        System.out.println("NO CORRESPONDING ACTION FOUND");
                        System.out.println("exiting...");
                        keepAsking = false;
                        break;
                }
            }
        }else{
            while (keepAsking){
                switch (askEmployee()) {
                    case AVAILABLE_CAMERAS:
                        _SQLExecutor.viewAvailableCameras(con);
                        break;
                    case AVAILABLE_LENSES:
                        _SQLExecutor.viewAvailableLenses(con);
                        break;
                    case TAKEN_ITEMS:
                        _SQLExecutor.viewTakenItems(con);
                        break;
                    case CAMERA_INFO:
                        _SQLExecutor.viewCameraInfo(con, askCameraName());
                        break;
                    case REGISTER_CUSTOMER: // INSERT: works
                        _SQLExecutor.registerCustomer(con, getCustomer());
                        break;
                    case REGISTER_CAMERA:
                        break;
                    case REGISTER_LENS:
                        break;
                    case REMOVE_ITEM: // DELETE:

                        if(askIfBySerial()){
                            _SQLExecutor.removeItem(con, getSerial(), true, askIfCamera());
                        }else{
                            _SQLExecutor.showItems(con);
                            _SQLExecutor.removeItem(con, getSerial(), false);
                        }

                        break;
                    case REGISTER_RENT:
                        break;
                    case SEARCH_CAMERA:// SELECT: works
                        _SQLExecutor.searchCameraByName(con, getSearchString());
                        break;
                    case SET_RETURNED: // UPDATE: works
                        _SQLExecutor.viewTakenItems(con);
                        _SQLExecutor.setItemReturned(con, askDate(), askNum());
                        break;
                    case ISSUE_ITEM:
                        // askForRentDetails(): Rent (Date_taken, _return_date, Item, Customer)
                        break;
                    default:
                        System.out.println("NO CORRESPONDING ACTION FOUND");
                        System.out.println("exiting...");
                        keepAsking = false;
                        break;
                }
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
}
