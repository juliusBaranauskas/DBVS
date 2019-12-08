import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class photoRENT {

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
    public static Connection getConnection(String conSetting, String conValidator) {
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

    int askCustomer(){
        System.out.println("Enter 1 to view cameras available for rent");
        System.out.println("Enter 2 to view lenses available for rent");
        System.out.println("Enter 3 to see your taken items");
        System.out.println("Enter 4 to view information about specific item");
        System.out.println("Enter 5 to ");
        System.out.println("Enter 6 to ");
        System.out.println("Enter 1 to ");

        string choice = System.in.read();

        switch (choice){
            case "1":
                return 1; // Enum.viewCameras
            case "2":
                return 2;
            case "3":
                return 3;
            case "4":
                return 4;
            default:
                System.out.println("Wrong key entered, try again");
                return printCustomerMenu();
        }
    }

    int askEmployee(){
        System.out.println("Enter 1 to view cameras available for rent");
        System.out.println("Enter 2 to view lenses available for rent");
        System.out.println("Enter 3 to see taken items");
        System.out.println("Enter 4 to view information about specific item");
        System.out.println("Enter 5 to register new customer");
        System.out.println("Enter 6 to register new item");
        System.out.println("Enter 7 to register new camera");

        char choice = System.in.read();
        if (Character.getNumericValue(choice)){

        }

        switch (choice){
            case "1":
                return 1; // Enum.viewCameras
            case "2":
                return 2;
            case "3":
                return 3;
            case "4":
                return 4;
            default:
                System.out.println("Wrong key entered, try again");
                return printCustomerMenu();
        }
    }

    boolean askForType(){
        System.out.println("Enter c to enter Customer menu\nEnter e to enter Employee menu");
        char choice = System.in.read();
        if(choice == 'c'){
            return true;
        }else if (choice == 'e'){
            return false;
        }else{
            System.out.println("Wrong character entered");
            return askForType();
        }
    }

    int getCustomerId(){
        System.out.println("Please enter your email address: ");
        try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in))){
            string mail = bufferedReader.readLine();
            for (char a: mail) {
                System.out.println(a);
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
            customerId = getCustomerId();
            switch (askCustomer()){
                case 1:
                    viewAvailableCameras();
                    break;
                case 2:
                    viewAvailableLenses();
                    break;
                case 3:
                    viewItemsTakenBy();
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