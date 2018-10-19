package holidayreservationsystemclient;

import ejb.session.stateful.HolidayReservationSessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import entity.Customer;
import entity.ItineraryItem;
import entity.Transaction;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import util.enumeration.PaymentModeEnum;
import util.exception.CheckoutException;
import util.exception.InvalidLoginCredentialException;
import util.helper.BigDecimalHelper;

//partner organisation:
/*
1) partner login
2) search room by checkin checkout
*/

public class MainApp
{
    private HolidayReservationSessionBeanRemote holidayReservationSessionBeanRemote;
    private CustomerSessionBeanRemote customerSessionBeanRemote;
    private Customer currentCustomer;
    //PLEASE FETCH PROPERLY
    //MORE CHANGE
    
    public MainApp() 
    {
        
    }

    
    
    public MainApp(HolidayReservationSessionBeanRemote holidayReservationSessionBeanRemote, CustomerSessionBeanRemote customerSessionBeanRemote) 
    {
        this();
        
        this.holidayReservationSessionBeanRemote = holidayReservationSessionBeanRemote;
        this.customerSessionBeanRemote = customerSessionBeanRemote;
    }



    public void runApp()
    {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** Welcome to Holiday Reservation System ***\n");
            
            if(currentCustomer != null)
            {
                System.out.println("You are login as " + currentCustomer.getFullName() + "\n");
            }
            else
            {            
                System.out.println("1: Login");
            }
            
            System.out.println("2: Search Holiday");
            System.out.println("3: Exit\n");
            response = 0;
            
            while(response < 1 || response > 3)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {
                    if(currentCustomer == null)
                    {
                        try
                        {
                            doLogin();
                            System.out.println("Login successful as " + currentCustomer.getFullName() + "!\n");                                                
                        }
                        catch(InvalidLoginCredentialException ex) 
                        {
                            System.out.println("Invalid login credential: " + ex.getMessage() + "\n");
                        }
                    }
                    else
                    {
                        System.out.println("You are already login as " + currentCustomer.getFullName() + "\n");
                    }
                }
                else if (response == 2)
                {
                    doSearchHoliday();
                }
                else if (response == 3)
                {
                    break;
                }
                else
                {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
            
            if(response == 3)
            {
                break;
            }
        }
        
        holidayReservationSessionBeanRemote.remove();
    }
    
    
    
    private void doLogin() throws InvalidLoginCredentialException
    {
        Scanner scanner = new Scanner(System.in);
        String email = "";
        String password = "";
        
        System.out.println("*** Holiday Reservation System :: Login ***\n");
        System.out.print("Enter email> ");
        email = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        password = scanner.nextLine().trim();
        
        if(email.length() > 0 && password.length() > 0)
        {
            //calls login method in customer session bean
            currentCustomer = customerSessionBeanRemote.login(email, password);
        }
        else
        {
            throw new InvalidLoginCredentialException("Missing login credential!");
        }
    }
    
    
    
    private void doSearchHoliday()
    {
        try
        {
            Scanner scanner = new Scanner(System.in);
            Integer response = 0;
            
            //want to take in date input
            SimpleDateFormat inputDateFormat = new SimpleDateFormat("d/M/y");
            SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
            
            Date departureDate;
            Date returnDate;
            String departureCity;
            String destinationCity;
            Integer numberOfTravellers;

            System.out.println("*** Holiday Reservation System :: Search Holiday ***\n");
           
            System.out.print("Enter Departure Date (dd/mm/yyyy)> ");
            departureDate = inputDateFormat.parse(scanner.nextLine().trim());
            
            System.out.print("Enter Return Date (dd/mm/yyyy)> ");
            returnDate = inputDateFormat.parse(scanner.nextLine().trim());            
            
            System.out.print("Enter Departure City> ");
            departureCity = scanner.nextLine().trim();
            System.out.print("Enter Destination City> ");
            destinationCity = scanner.nextLine().trim();
            System.out.print("Enter Number of Travellers> ");
            numberOfTravellers = scanner.nextInt();
            
            List<ItineraryItem> itineraryItems = holidayReservationSessionBeanRemote.searchHolidays(departureDate, returnDate, departureCity, destinationCity, numberOfTravellers);
            
            System.out.printf("%8s%22s   %s\n", "Seq. No.", "Date/Time", "Itinerary");
            //print out the itinerary list after they have been generated acocrding to client's requests
            for(ItineraryItem itineraryItem:itineraryItems)
            {
                System.out.printf("%8s%22s   %s\n", itineraryItem.getSequenceNumber(), outputDateFormat.format(itineraryItem.getDateTime()), itineraryItem.getActivity());
            }
            
            System.out.println("------------------------");
            System.out.println("1: Make Reservation");
            System.out.println("2: Back\n");
            System.out.print("> ");
            response = scanner.nextInt();
            
            if(response == 1)
            {
                if(currentCustomer != null)
                    //logged in!
                {
                    PaymentModeEnum paymentMode;
                    String creditCardNumber;
                    
                    System.out.println("\nTotal Amount Payable is " + BigDecimalHelper.formatCurrency(holidayReservationSessionBeanRemote.getTotalAmount())) ;
                    //formats big decimal to currency format
                    
                    while(true)
                    { 
                        System.out.print("Select Payment Mode (1: VISA, 2: MasterCard, 3: AMEX)> ");
                        Integer paymentModeInt = scanner.nextInt();

                        if(paymentModeInt >= 1 && paymentModeInt <= 3)
                        {
                            paymentMode = PaymentModeEnum.values()[paymentModeInt-1];
                            break;
                        }
                        else
                        {
                            System.out.println("Invalid option, please try again!\n");
                        }
                    }
                    
                    scanner.nextLine();
                    System.out.print("Enter Credit Card Number> ");
                    creditCardNumber = scanner.nextLine().trim();
                    
                    try 
                    {
                        //calls a method in holidayReservationSessionBean
                        //which calls another method in checkoutsessionbean
                        System.out.println("id: "+currentCustomer.getCustomerId());
                        Transaction transaction = new Transaction();
                        transaction = holidayReservationSessionBeanRemote.reserveHoliday(currentCustomer.getCustomerId(), paymentMode, creditCardNumber);
                        
                        System.out.println("Reservation of holiday completed successfully!: " + transaction.getTransactionId() + "\n");
                    } 
                    catch (CheckoutException ex) 
                    {
                        System.out.println("An error has occurred while making the reservation: " + ex.getMessage() + "\n");
                    }
                }
                else
                {
                    System.out.println("Please login first before making a reservation!\n");
                }
            }
        }
        catch(ParseException ex)
        {
            System.out.println("Invalid date input!\n");
        }
    }
}