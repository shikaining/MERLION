package ejb.session.singleton;

import ejb.session.stateless.EmployeeSessionBeanLocal;
import ejb.session.stateless.RoomSessionBeanLocal;
import ejb.session.stateless.RoomTypeSessionBeanLocal;
import entity.EmployeeEntity;
import entity.RoomTypeEntity;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import util.enumeration.employeeAccessRightEnum;
import util.enumeration.roomStatusEnum;
import util.exception.EmployeeNotFoundException;


@Singleton
@LocalBean
@Startup
public class DataInitSessionBean {

    @EJB
    private RoomTypeSessionBeanLocal roomTypeSessionBeanLocal;
    @EJB
    private EmployeeSessionBeanLocal employeeSessionBeanLocal;
    
    
    

    public DataInitSessionBean() {
    }
   
    @PostConstruct
    public void postConstruct() {
         try
        {
            employeeSessionBeanLocal.retrieveEmployeeByUsername("Admin");
        }
        catch(EmployeeNotFoundException ex)
        {
            initializeData();
        }
    }
    
    private void initializeData()
       {
           employeeSessionBeanLocal.createNewEmployee(new EmployeeEntity("Admin","One","Admin","Password",employeeAccessRightEnum.SYSTEMADMIN));
           employeeSessionBeanLocal.createNewEmployee(new EmployeeEntity("Operation","One","Operation","Password",employeeAccessRightEnum.OPERATIONMANAGER));
           employeeSessionBeanLocal.createNewEmployee(new EmployeeEntity("Sales","One","Sales","Password",employeeAccessRightEnum.SALESMANAGER));
           employeeSessionBeanLocal.createNewEmployee(new EmployeeEntity("Officer","One","Officer","Password",employeeAccessRightEnum.GUESTRELOFFICER));
          
           List<String> amenities = new ArrayList<>();
           amenities.add("Wifi");
           amenities.add("Order-in");
           
           roomTypeSessionBeanLocal.createNewRoomType(new RoomTypeEntity("Deluxe","Cozy","32sqm","single",1,30,amenities));
           roomTypeSessionBeanLocal.createNewRoomType(new RoomTypeEntity("Premier","Modern","38sqm","1 queen",2,30,amenities));
           roomTypeSessionBeanLocal.createNewRoomType(new RoomTypeEntity("Family","Great for fams!","43sqm","1 king",3,20,amenities));
           roomTypeSessionBeanLocal.createNewRoomType(new RoomTypeEntity("Junior Suite","Luxurious","50sqm","2 king",4,20,amenities));
           roomTypeSessionBeanLocal.createNewRoomType(new RoomTypeEntity("Grand Suite","Super Luxurious","62sqm","3 king",5,10,amenities));
           
           
       }
}

