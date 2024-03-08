import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;
class InvalidUsernameException extends Exception{
    public String toString(){
        return "\n\n\t-----Invalid Username and Password!------\n\n ";
    }
}
class NoMoreTicketsException extends Exception{
    public String toString(){
        return " ALL seats are already booked\nBetter Luck NEXT TIME ";
    }
}
class BusNotFoundException extends Exception{
    public String toString(){
        return "\n\n-------Bus Not Found------------ !!";
    }
}
class Login{
    static String url = "jdbc:mysql://localhost:3306/BusTicketing";
    static String username="pratigya";
    static String password="@pass321";
    static String username1;
    static String password1;
    static int actual_available_seats;
    static int actual_fare;
    static int actual_bus_id1;
    static String new_bus_name;
    static String new_destination;
    static  String new_initialpoint;

    static  void print_ticket(int cus_id,int bus_num,int seatTo){
        try{
            
            int booking_id1;
            int new_bus_number;
            int new_seatTo;
            int new_cusid;
            int charge1;
            int count=0;
            Class.forName("com.mysql.cj.jdbc.Driver");
            java.sql.Connection con = DriverManager.getConnection(url, username, password);
            String q4="SELECT * FROM Bus";
            Statement st1 = con.createStatement();
            ResultSet r = st1.executeQuery(q4);
            
            while(r.next()){
                int bus_id1=r.getInt(1);
                String bus_name1=r.getString(2);
                String dest1=r.getString(3);
                String ip1=r.getString(4);
                if(bus_id1==bus_num){
                    new_bus_name=bus_name1;
                    new_destination=dest1;
                    new_initialpoint=ip1;
                    break;
                }
            }
            String query="SELECT * FROM booking";
            Statement st = con.createStatement();
            ResultSet r1 = st.executeQuery(query);
            System.out.println("\t\t\tTicket Details");
            System.out.println("|--------------------------------------------------------------------------------------------|");
            System.out.printf("| BookId | Cus_ID | Bus_Num |   BusName   |  Destination   |  InitialPoint | Seats | Charge  |\n");
            System.out.println("|--------------------------------------------------------------------------------------------|");
            while(r1.next()){
                booking_id1=r1.getInt(1);
                new_cusid=r1.getInt(2);
                new_bus_number=r1.getInt(3);
                new_seatTo=r1.getInt(4);
                charge1=r1.getInt(5);
                if((new_cusid==cus_id)&&(seatTo==new_seatTo) &&(new_bus_number==bus_num)){
                    System.out.printf("|%-10d%-10d%-10d%-15s%-18s%-15s%-5d%-8d|\n",booking_id1,new_cusid,new_bus_number,new_bus_name,new_destination,new_initialpoint,new_seatTo,charge1);
                    System.out.println("|--------------------------------------------------------------------------------------------|");
                    count=count+1;
                    }
                
                }
            if(count==0){
                System.out.println("\n\t--------No reservation made yet------------");
            }
            System.out.println("|--------------------------------------------------------------------------------------------|");
            }catch(Exception e){
                System.out.println(e);
            }

    }
    static void reserve(int customer_id) throws NoMoreTicketsException{
        Scanner sc=new Scanner(System.in);
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            java.sql.Connection con = DriverManager.getConnection(url, username, password);
            viewBus();
            System.out.println("__________________________________________________________________________________________________________________________\n");
            System.out.println("Enter the Bus Number:");
            int entered_bus_number=sc.nextInt();
            System.out.println("Enter the number of seats you wanna book:");
            int entered_seatTo=sc.nextInt();
            System.out.println("__________________________________________________________________________________________________________________________\n");
            
            String q4="SELECT * FROM Bus ";
            Statement st = con.createStatement();
            ResultSet r1 = st.executeQuery(q4);
            while(r1.next()){
                actual_bus_id1=r1.getInt(1);
                actual_available_seats=r1.getInt(8);
                actual_fare=r1.getInt(9);
                if(entered_bus_number==actual_bus_id1){
                    break;
                }
        }
            String q="INSERT INTO booking(cus_id,bus_no,seatTo,charge)VALUES(?,?,?,?)";
            PreparedStatement p2=con.prepareStatement(q);
            int updated_available_seats=actual_available_seats-entered_seatTo;
            if(actual_available_seats>=0 && updated_available_seats>=0){
                int charge=actual_fare*entered_seatTo;
                p2.setInt(1,customer_id);
                p2.setInt(2,entered_bus_number);
                p2.setInt(3,entered_seatTo);
                p2.setInt(4, charge);
                p2.executeUpdate();
                p2.close();
                String q5=" UPDATE Bus SET available_seats=? WHERE bus_no=?";
                PreparedStatement p3=con.prepareStatement(q5);
                p3.setInt(1,updated_available_seats);
                p3.setInt(2,entered_bus_number);
                p3.executeUpdate();
                p3.close();
            }
            else{
                throw new NoMoreTicketsException();
            }
            con.close();
            print_ticket(customer_id, entered_bus_number, entered_seatTo);
        }catch(Exception e){
                    System.out.println(e);
        }
        
    }
        static String verify(String user, String pass) throws InvalidUsernameException{
        int count=0;
        int id1=0;
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            java.sql.Connection con = DriverManager.getConnection(url, username, password);
            String q4="SELECT * FROM DETAILS";
            Statement st = con.createStatement();
            ResultSet r1 = st.executeQuery(q4);
            while(r1.next()){
                id1=r1.getInt(1);
                String username3=r1.getString(2);
                String password3=r1.getString(3);
                if((user.equals(username3)) && (pass.equals(password3))){
                    count++;
                    break;
                    }
                }
        }catch (Exception e){
            System.out.println(e);
        }
        if(count==1){
            System.out.print("\n\t\t|--------------------------------------------------------|");
            System.out.println("\n\t\t\tPlease Note that your customer id is "+id1);
            System.out.print("\t\t|--------------------------------------------------------|");
                        return "THANKS The information was valid!";
        }
        else{
            throw new InvalidUsernameException();
        }
    }
        static void addData(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            java.sql.Connection con = DriverManager.getConnection(url, username, password);
            String q3="INSERT INTO DETAILS(username,password,firstname,lastname,address,mobile) VALUES(?,?,?,?,?,?)";
            Scanner sc=new Scanner(System.in);
            PreparedStatement p2=con.prepareStatement(q3);
            System.out.println("Enter the username:");
            username1=sc.next();
            System.out.println("Enter the password:");
            password1=sc.next();
            System.out.println("Enter the first name:");
            String fname=sc.next();
            System.out.println("ENter the last name");
            String lname=sc.next();
            System.out.println("Enter the address:");
            String address=sc.next();
            System.out.println("Enter your mobile number");
            String mobile=sc.next();
            p2.setString(1,username1);
            p2.setString(2,password1);
            p2.setString(3,fname);
            p2.setString(4,lname);
            p2.setString(5,address);
            p2.setString(6,mobile);
            p2.executeUpdate();
            p2.close();
            con.close();
            System.out.println("_______You have been Signed in_____");
        } catch (Exception e){
            System.out.println(e);
        }
    }
    static void viewBus(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            java.sql.Connection con = DriverManager.getConnection(url, username, password);
            String q4="SELECT * FROM Bus";
            Statement st = con.createStatement();
            ResultSet r1 = st.executeQuery(q4);
            System.out.println("__________________________________________________________________________________________________________________|\n");
            System.out.println("|Bus_no|  bus_name  | Destination|InitialPoint|      Departure     |        Arrival     |  Total  |Available|Fare |");
            System.out.println("\t\t\t\t\t\t\t\t\t\t\t|  Seats  | Seats   |     |");
            System.out.println("__________________________________________________________________________________________________________________|\n");
            while(r1.next()){
                int bus_id1=r1.getInt(1);
                String bus_name1=r1.getString(2);
                String dest1=r1.getString(3);
                String ip1=r1.getString(4);
                String departure1=r1.getString(5);
                String arrival1=r1.getString(6);
                int total_seats1=r1.getInt(7);
                int available_seats=r1.getInt(8);
                int fare1=r1.getInt(9);
                System.out.printf("%-7d|%-12s|%-12s|%-12s|%20s|%20s| %-9d|% -6d| %-6d|\n",bus_id1,bus_name1,dest1,ip1,departure1,arrival1,total_seats1,available_seats,fare1);
            }
            System.out.println("__________________________________________________________________________________________________________________|\n");
        } catch (Exception e){
            System.out.println(e);
        }

    }
    static void furtherChoice(){
        boolean b=true;
        Scanner sc=new Scanner(System.in);
        while(b){
        int ch;
        System.out.print("\n\n\n|-------------------------------------------------------|\n");
        System.out.print("|1.RESERVE TICKETS                                      |\n");
        System.out.print("|2.VIEW BUS INFO                                        |\n");
        System.out.print("|3.SEARCH                                               |\n");
        System.out.print("|4.CANCEL ticket                                        |\n");
        System.out.print("|5.HISTORY                                              |\n");
        System.out.print("|6.BACK                                                 |\n");
        System.out.print("|-------------------------------------------------------|\n");
        System.out.println("\nEnter your choice:");
        ch=sc.nextInt();
        switch(ch){
            case 1:
                System.out.println("Enter the customer_id");
                int id=sc.nextInt();
                try {
                    reserve(id);
                } catch (NoMoreTicketsException e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                    viewBus();
                    break;
            case 3:
                    System.out.println("Enter the destination you want to search:");
                    String dest=sc.next();
                    Search s1=new Search();
                    try{
                    s1.search(dest);
                    }catch(BusNotFoundException a){
                        System.out.println(a);
                    }
                    break;
            case 4:
                    System.out.println("Enter the Booking id:");
                    int booking_id=sc.nextInt();
                    Cancel c=new Cancel();
                    c.selectBooking(booking_id);
                    break;
            case 5:
                    System.out.println("Enter your customer_id");
                    int cus_id=sc.nextInt();
                    System.out.println("Enter the Bus Number");
                    int bus_num=sc.nextInt();
                    System.out.println("Enter the Number of seats:");
                    int no_seats=sc.nextInt();
                    print_ticket(cus_id,bus_num,no_seats);
                    break;
            case 6:
                        b=false;
                        break;
            default:
                System.out.println("Invalid choice!!");
            }
        }
       
    }


    public void login(){
        try{
            boolean b=true;
            Scanner sc=new Scanner(System.in);
            while(b){
                System.out.println("\n\n\n");
                System.out.print("|--------------------------------------------------|\n|                                                  |\n|");
                System.out.print("1.LOGIN                                           |\n|                                                  |\n|");
                System.out.print("2.NEW to this Bus ticketing System? Sign Up       |\n|                                                  |\n|");
                System.out.print("3.Exit                                            |\n|                                                  |\n|");
                System.out.print("--------------------------------------------------|");
                
                int choice;
                System.out.println("\n\n\nPlease enter your choice:");
                choice=sc.nextInt();
                switch(choice){
                    case 1:

                            System.out.println("Enter your username:");
                            username1=sc.next();
                            System.out.println("Enter your password: ");
                            password1=sc.next();
                            String msg1=verify(username1,password1);
                            System.out.print("\n\n\t\t|--------------------------------------------------------|");
                            System.out.print("\n\t\t\t "+msg1);
                            System.out.print("\n\t\t|--------------------------------------------------------|");
                            if(msg1.equals("THANKS The information was valid!")){
                                furtherChoice();
                            }
                            break;
                    case 2:
                            addData();
                            break;
                    case 3:
                        b=false;
                    break;
                    default:
                        System.out.print("\n\t\t|--------------------------------------------------------|");
                        System.out.print("\n\t\t|\tInvalid choice!!!\t\t |");
                        System.out.print("\n\t\t|--------------------------------------------------------|");
                }
        }
        
    }catch(InvalidUsernameException a){
            System.out.println(a);
        }
    }
}

class Cancel {
    static String url = "jdbc:mysql://localhost:3306/BusTicketing";
    static String username="pratigya";
    static String password="@pass321";

    static void updateBus(int seatTo,int bus_no){
        try {
            int available_seats=0;
            Scanner sc=new Scanner(System.in);
            Class.forName("com.mysql.cj.jdbc.Driver");
            java.sql.Connection con = DriverManager.getConnection(url, username, password);
            String q4="SELECT bus_no,available_seats FROM Bus";
            Statement st = con.createStatement();
            ResultSet r1 = st.executeQuery(q4);
            int bus_number=0;
            while(r1.next()){
                bus_number=r1.getInt(1);
                available_seats=r1.getInt(2);
                if(bus_no==bus_number){
                    break;
                }
            }
            int newavailableseats=available_seats+seatTo;
            String query="UPDATE Bus SET available_seats=? WHERE bus_no=?";
            PreparedStatement p=con.prepareStatement(query);
            p.setInt(1, newavailableseats);
            p.setInt(2, bus_no);
            p.executeUpdate();
            con.close();
            
        } catch(Exception e) {
            System.out.println(e);
        }
    }
    public void selectBooking(int b_id){
        try{
            int bus_id=0;
            Class.forName("com.mysql.cj.jdbc.Driver");
            java.sql.Connection con = DriverManager.getConnection(url, username, password);
            String query="SELECT bus_id,bus_no,seatTo FROM booking";
            Statement st = con.createStatement();
            ResultSet r1 = st.executeQuery(query);
            while(r1.next()){
                int booking_id=r1.getInt(1);
                bus_id=r1.getInt(2);
                int seatTo=r1.getInt(3);
                if(booking_id==b_id){
                    updateBus(seatTo,bus_id);
                    break;
                }
            }
            String query1="DELETE FROM Booking WHERE bus_id=?";
            PreparedStatement p=con.prepareStatement(query1);
            p.setInt(1,b_id);
            p.executeUpdate();
            System.out.print("\n\t\t|--------------------------------------------------------|");
            System.out.print("\n\t\t|\tTickets Cancelled!!!\t\t\t\t |");
            System.out.print("\n\t\t|--------------------------------------------------------|");
            con.close();
        }catch(Exception e){
            System.out.println(e);
        }
    }
    
}
class Search{
    int bus_id1;
    String bus_name1;
    String dest2;
    String ip1;
    String departure1;
    String arrival1;
    int total_seats1;
    int available_seats;
    int fare1;

    public void search(String dest) throws BusNotFoundException{
        try{
            int count=0;
            int flag=0;
            String url = "jdbc:mysql://localhost:3306/BusTicketing";
            String username="pratigya";
            String password="@pass321";
            Class.forName("com.mysql.cj.jdbc.Driver");
            java.sql.Connection con = DriverManager.getConnection(url, username, password);
            String q4="SELECT * FROM Bus";
            Statement st = con.createStatement();
            ResultSet r1 = st.executeQuery(q4);
            String dest1;
            System.out.println("__________________________________________________________________________________________________________________|\n");
            System.out.println("|Bus_no|  bus_name  | Destination|InitialPoint|      Departure     |        Arrival     |  Total  |Available|Fare |");
            System.out.println("\t\t\t\t\t\t\t\t\t\t\t|  Seats  | Seats   |     |");
            System.out.println("__________________________________________________________________________________________________________________|\n");
            while(r1.next()){
                bus_id1=r1.getInt(1);
                bus_name1=r1.getString(2);
                dest2=r1.getString(3);
                ip1=r1.getString(4);
                departure1=r1.getString(5);
                arrival1=r1.getString(6);
                total_seats1=r1.getInt(7);
                available_seats=r1.getInt(8);
                fare1=r1.getInt(9);
                dest1=dest2;
                flag ++;
                    if(dest.equals(dest1)){
                        
                        System.out.printf("%-7d|%-12s|%-12s|%-12s|%20s|%20s| %-9d|% -6d| %-6d|\n",bus_id1,bus_name1,dest1,ip1,departure1,arrival1,total_seats1,available_seats,fare1);
                    }
                    else{
                        count++;
                    }
            }
            System.out.println("__________________________________________________________________________________________________________________|\n");
            if(count==flag){
                throw new BusNotFoundException();
            }
        
        }catch(Exception e){
            System.out.println(e);
        }
    }
}
public class Connection{
    public static void main(String[] args)
    {
        System.out.print("\n\n\n\t\t|*------------------------------------------------------*|\n");
        System.out.print("\t\t|--------------------------------------------------------|\n");
		System.out.print("\t\t|\t\t\t\t\t\t\t |");
		System.out.print("\n\t\t|\t\tWELCOME TO BUS TICKETING SYSTEM!!!\t |");
		System.out.print("\n\t\t|\t\t\t\t\t\t\t |");
		System.out.print("\n\t\t|\t\t\t\t\t\t\t |");
		System.out.print("\n\t\t|--------------------------------------------------------|\n");
        System.out.print("\t\t*--------------------------------------------------------*");
        Scanner sc=new Scanner(System.in);
        String choice;
        do{
            Login l=new Login();
            l.login();
            System.out.println("\n\nDo You want to Continuee?(YES/NO)");
            choice =sc.next();
        }while(choice.equalsIgnoreCase("YES"));
        System.out.print("\n\t\t|--------------------------------------------------------|");
        System.out.print("\n\t\t|\tYou have succesfully taken an exit!!!\t\t |");
        System.out.print("\n\t\t|--------------------------------------------------------|");
        
    }
}