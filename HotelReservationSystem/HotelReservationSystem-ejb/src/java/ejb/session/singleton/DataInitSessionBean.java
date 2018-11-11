package ejb.session.singleton;

import ejb.session.stateless.EmployeeSessionBeanLocal;
import ejb.session.stateless.RoomRateSessionBeanLocal;
import ejb.session.stateless.RoomTypeSessionBeanLocal;
import entity.EmployeeEntity;
import entity.RoomRateEntity;
import entity.RoomTypeEntity;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import util.enumeration.employeeAccessRightEnum;
import util.enumeration.rateTypeEnum;
import util.exception.EmployeeNotFoundException;
import util.exception.RoomTypeNotFoundException;

@Singleton
@LocalBean
@Startup
public class DataInitSessionBean {

    @EJB
    private RoomRateSessionBeanLocal roomRateSessionBeanLocal;
    @EJB
    private RoomTypeSessionBeanLocal roomTypeSessionBeanLocal;
    @EJB
    private EmployeeSessionBeanLocal employeeSessionBeanLocal;

    public DataInitSessionBean() {
    }

    @PostConstruct
    public void postConstruct() {
        try {
            employeeSessionBeanLocal.retrieveEmployeeByUsername("Admin");
        } catch (EmployeeNotFoundException ex) {
            initializeData();
        }
    }

    private void initializeData() {
        employeeSessionBeanLocal.createNewEmployee(new EmployeeEntity("Admin", "One", "Admin", "Password", employeeAccessRightEnum.SYSTEMADMIN));
        employeeSessionBeanLocal.createNewEmployee(new EmployeeEntity("Operation", "One", "Operation", "Password", employeeAccessRightEnum.OPERATIONMANAGER));
        employeeSessionBeanLocal.createNewEmployee(new EmployeeEntity("Sales", "One", "Sales", "Password", employeeAccessRightEnum.SALESMANAGER));
        employeeSessionBeanLocal.createNewEmployee(new EmployeeEntity("Officer", "One", "Officer", "Password", employeeAccessRightEnum.GUESTRELOFFICER));

        List<String> amenities = new ArrayList<>();
        amenities.add("Wifi");
        amenities.add("Order-in");

        roomTypeSessionBeanLocal.createNewRoomType(new RoomTypeEntity("Deluxe", "Cozy", "32sqm", "single", 1, 30, amenities));
        roomTypeSessionBeanLocal.createNewRoomType(new RoomTypeEntity("Premier", "Modern", "38sqm", "1 queen", 2, 30, amenities));
        roomTypeSessionBeanLocal.createNewRoomType(new RoomTypeEntity("Family", "Great for fams!", "43sqm", "1 king", 3, 20, amenities));
        roomTypeSessionBeanLocal.createNewRoomType(new RoomTypeEntity("Junior Suite", "Luxurious", "50sqm", "2 king", 4, 20, amenities));
        roomTypeSessionBeanLocal.createNewRoomType(new RoomTypeEntity("Grand Suite", "Super Luxurious", "62sqm", "3 king", 5, 10, amenities));

//        SimpleDateFormat inputDateFormat = new SimpleDateFormat("d/M/y");
//        SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd/MM/yyyy");
//        Date validityStartDate;
//        Date validityEndDate;
//        
//        try {
//            roomRateSessionBeanLocal.createNewRoomRate(new RoomRateEntity("Deluxe Published", new BigDecimal("200"), rateTypeEnum.PUBLISHED, null , null, roomTypeSessionBeanLocal.retrieveRoomTypeByName("Deluxe")));
//        } catch (RoomTypeNotFoundException ex) {
//           
//        }
//        try {
//            roomRateSessionBeanLocal.createNewRoomRate(new RoomRateEntity("Deluxe Normal", new BigDecimal("150"), rateTypeEnum.NORMAL, null , null, roomTypeSessionBeanLocal.retrieveRoomTypeByName("Deluxe")));
//        } catch (RoomTypeNotFoundException ex) {
//            
//        }
//        try {
//            try {
//                roomRateSessionBeanLocal.createNewRoomRate(new RoomRateEntity("Deluxe Peak", new BigDecimal("250"), rateTypeEnum.PEAK, inputDateFormat.parse("19/11/2018") , inputDateFormat.parse("28/12/2018"), roomTypeSessionBeanLocal.retrieveRoomTypeByName("Deluxe")));
//            } catch (RoomTypeNotFoundException ex) {
//                
//            }
//        } catch (ParseException ex) {
//            
//        }
//        try {
//            try {
//                roomRateSessionBeanLocal.createNewRoomRate(new RoomRateEntity("Deluxe Promotion", new BigDecimal("130"), rateTypeEnum.PROMOTION, inputDateFormat.parse("09/11/2018") , inputDateFormat.parse("20/11/2018"), roomTypeSessionBeanLocal.retrieveRoomTypeByName("Deluxe")));
//            } catch (RoomTypeNotFoundException ex) {
//                
//            }
//        } catch (ParseException ex) {
//           
//        }
//        
//        try {
//            roomRateSessionBeanLocal.createNewRoomRate(new RoomRateEntity("Premier Published", new BigDecimal("400"), rateTypeEnum.PUBLISHED, null , null, roomTypeSessionBeanLocal.retrieveRoomTypeByName("Premier")));
//        } catch (RoomTypeNotFoundException ex) {
//         
//        }
//        try {
//            roomRateSessionBeanLocal.createNewRoomRate(new RoomRateEntity("Premier Normal", new BigDecimal("300"), rateTypeEnum.NORMAL, null , null, roomTypeSessionBeanLocal.retrieveRoomTypeByName("Premier")));
//        } catch (RoomTypeNotFoundException ex) {
//            
//        }
//        try {
//            try {
//                roomRateSessionBeanLocal.createNewRoomRate(new RoomRateEntity("Premier Peak", new BigDecimal("500"), rateTypeEnum.PEAK, inputDateFormat.parse("19/11/2018") , inputDateFormat.parse("28/12/2018"), roomTypeSessionBeanLocal.retrieveRoomTypeByName("Premier")));
//            } catch (RoomTypeNotFoundException ex) {
//               
//            }
//        } catch (ParseException ex) {
//      
//        }
//        try {
//            try {
//                roomRateSessionBeanLocal.createNewRoomRate(new RoomRateEntity("Premier Promotion", new BigDecimal("260"), rateTypeEnum.PROMOTION, inputDateFormat.parse("09/11/2018") , inputDateFormat.parse("20/11/2018"), roomTypeSessionBeanLocal.retrieveRoomTypeByName("Premier")));
//            } catch (RoomTypeNotFoundException ex) {
//               
//            }
//        } catch (ParseException ex) {
//            
//        }
    }
}
