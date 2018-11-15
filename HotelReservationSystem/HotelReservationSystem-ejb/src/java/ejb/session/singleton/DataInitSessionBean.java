package ejb.session.singleton;

import ejb.session.stateless.EmployeeSessionBeanLocal;
import ejb.session.stateless.RoomRateSessionBeanLocal;
import ejb.session.stateless.RoomSessionBeanLocal;
import ejb.session.stateless.RoomTypeSessionBeanLocal;
import entity.EmployeeEntity;
import entity.RoomEntity;
import entity.RoomRateEntity;
import entity.RoomTypeEntity;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import util.enumeration.employeeAccessRightEnum;
import util.enumeration.rateTypeEnum;
import util.enumeration.roomStatusEnum;
import util.exception.EmployeeNotFoundException;

@Singleton
@LocalBean
@Startup
public class DataInitSessionBean {

    @EJB
    private RoomSessionBeanLocal roomSessionBeanLocal;
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

//        RoomTypeEntity deluxe = new RoomTypeEntity("Deluxe", "Cozy", "32sqm", "single", 1, 1, 30, amenities);
//        em.persist(deluxe);
//        RoomTypeEntity premier = new RoomTypeEntity("Premier", "Modern", "38sqm", "1 queen", 2, 2, 30, amenities);
//        em.persist(premier);
//        RoomTypeEntity family = new RoomTypeEntity("Family", "Great for fams!", "43sqm", "1 king", 3, 3, 20, amenities);
//        em.persist(family);
//        RoomTypeEntity junior = new RoomTypeEntity("Junior Suite", "Luxurious", "50sqm", "2 king", 4, 4, 20, amenities);
//        em.persist(junior);
//        RoomTypeEntity grand = new RoomTypeEntity("Grand Suite", "Super Luxurious", "62sqm", "3 king", 5, 5, 10, amenities);
//        em.persist(grand);
        RoomTypeEntity deluxe = roomTypeSessionBeanLocal.createNewRoomType(new RoomTypeEntity("Deluxe", "Cozy", "32sqm", "single", 1, 1, 30, amenities));
        RoomTypeEntity premier = roomTypeSessionBeanLocal.createNewRoomType(new RoomTypeEntity("Premier", "Modern", "38sqm", "1 queen", 2, 2, 30, amenities));
        RoomTypeEntity family = roomTypeSessionBeanLocal.createNewRoomType(new RoomTypeEntity("Family", "Great for fams!", "43sqm", "1 king", 3, 3, 20, amenities));
        RoomTypeEntity junior = roomTypeSessionBeanLocal.createNewRoomType(new RoomTypeEntity("Junior Suite", "Luxurious", "50sqm", "2 king", 4, 4, 20, amenities));
        RoomTypeEntity grand = roomTypeSessionBeanLocal.createNewRoomType(new RoomTypeEntity("Grand Suite", "Super Luxurious", "62sqm", "3 king", 5, 5, 10, amenities));

        RoomRateEntity deluxePublished = roomRateSessionBeanLocal.createNewRoomRate(new RoomRateEntity("Deluxe Published", new BigDecimal("200"), rateTypeEnum.PUBLISHED, null, null, deluxe));
        RoomRateEntity deluxeNormal = roomRateSessionBeanLocal.createNewRoomRate(new RoomRateEntity("Deluxe Normal", new BigDecimal("150"), rateTypeEnum.NORMAL, null, null, deluxe));

        RoomRateEntity premierPublished = roomRateSessionBeanLocal.createNewRoomRate(new RoomRateEntity("Premier Published", new BigDecimal("400"), rateTypeEnum.PUBLISHED, null, null, premier));
        RoomRateEntity premierNormal = roomRateSessionBeanLocal.createNewRoomRate(new RoomRateEntity("Premier Normal", new BigDecimal("350"), rateTypeEnum.NORMAL, null, null, premier));

        RoomRateEntity familyPublished = roomRateSessionBeanLocal.createNewRoomRate(new RoomRateEntity("Family Published", new BigDecimal("600"), rateTypeEnum.PUBLISHED, null, null, family));
        RoomRateEntity familyNormal = roomRateSessionBeanLocal.createNewRoomRate(new RoomRateEntity("Family Normal", new BigDecimal("550"), rateTypeEnum.NORMAL, null, null, family));

        RoomRateEntity juniorPublished = roomRateSessionBeanLocal.createNewRoomRate(new RoomRateEntity("Junior Published", new BigDecimal("800"), rateTypeEnum.PUBLISHED, null, null, junior));
        RoomRateEntity juniorNormal = roomRateSessionBeanLocal.createNewRoomRate(new RoomRateEntity("Junior Normal", new BigDecimal("750"), rateTypeEnum.NORMAL, null, null, junior));

        RoomRateEntity grandPublished = roomRateSessionBeanLocal.createNewRoomRate(new RoomRateEntity("Grand Published", new BigDecimal("1000"), rateTypeEnum.PUBLISHED, null, null, grand));
        RoomRateEntity grandNormal = roomRateSessionBeanLocal.createNewRoomRate(new RoomRateEntity("Grand Normal", new BigDecimal("950"), rateTypeEnum.NORMAL, null, null, grand));

        RoomEntity deluxe1 = roomSessionBeanLocal.createNewRoom(new RoomEntity("0101", roomStatusEnum.AVAILABLE), deluxe.getRoomTypeId());
        RoomEntity deluxe2 = roomSessionBeanLocal.createNewRoom(new RoomEntity("0201", roomStatusEnum.AVAILABLE), deluxe.getRoomTypeId());
        RoomEntity deluxe3 = roomSessionBeanLocal.createNewRoom(new RoomEntity("0301", roomStatusEnum.AVAILABLE), deluxe.getRoomTypeId());
        RoomEntity deluxe4 = roomSessionBeanLocal.createNewRoom(new RoomEntity("0401", roomStatusEnum.AVAILABLE), deluxe.getRoomTypeId());
        RoomEntity deluxe5 = roomSessionBeanLocal.createNewRoom(new RoomEntity("0501", roomStatusEnum.AVAILABLE), deluxe.getRoomTypeId());

        RoomEntity premier1 = roomSessionBeanLocal.createNewRoom(new RoomEntity("0102", roomStatusEnum.AVAILABLE), premier.getRoomTypeId());
        RoomEntity premier2 = roomSessionBeanLocal.createNewRoom(new RoomEntity("0202", roomStatusEnum.AVAILABLE), premier.getRoomTypeId());
        RoomEntity premier3 = roomSessionBeanLocal.createNewRoom(new RoomEntity("0302", roomStatusEnum.AVAILABLE), premier.getRoomTypeId());
        RoomEntity premier4 = roomSessionBeanLocal.createNewRoom(new RoomEntity("0402", roomStatusEnum.AVAILABLE), premier.getRoomTypeId());
        RoomEntity premier5 = roomSessionBeanLocal.createNewRoom(new RoomEntity("0502", roomStatusEnum.AVAILABLE), premier.getRoomTypeId());

        RoomEntity family1 = roomSessionBeanLocal.createNewRoom(new RoomEntity("0103", roomStatusEnum.AVAILABLE), family.getRoomTypeId());
        RoomEntity family2 = roomSessionBeanLocal.createNewRoom(new RoomEntity("0203", roomStatusEnum.AVAILABLE), family.getRoomTypeId());
        RoomEntity family3 = roomSessionBeanLocal.createNewRoom(new RoomEntity("0303", roomStatusEnum.AVAILABLE), family.getRoomTypeId());
        RoomEntity family4 = roomSessionBeanLocal.createNewRoom(new RoomEntity("0403", roomStatusEnum.AVAILABLE), family.getRoomTypeId());
        RoomEntity family5 = roomSessionBeanLocal.createNewRoom(new RoomEntity("0503", roomStatusEnum.AVAILABLE), family.getRoomTypeId());

        RoomEntity junior1 = roomSessionBeanLocal.createNewRoom(new RoomEntity("0104", roomStatusEnum.AVAILABLE), junior.getRoomTypeId());
        RoomEntity junior2 = roomSessionBeanLocal.createNewRoom(new RoomEntity("0204", roomStatusEnum.AVAILABLE), junior.getRoomTypeId());
        RoomEntity junior3 = roomSessionBeanLocal.createNewRoom(new RoomEntity("0304", roomStatusEnum.AVAILABLE), junior.getRoomTypeId());
        RoomEntity junior4 = roomSessionBeanLocal.createNewRoom(new RoomEntity("0404", roomStatusEnum.AVAILABLE), junior.getRoomTypeId());
        RoomEntity junior5 = roomSessionBeanLocal.createNewRoom(new RoomEntity("0504", roomStatusEnum.AVAILABLE), junior.getRoomTypeId());

        RoomEntity grand1 = roomSessionBeanLocal.createNewRoom(new RoomEntity("0105", roomStatusEnum.AVAILABLE), grand.getRoomTypeId());
        RoomEntity grand2 = roomSessionBeanLocal.createNewRoom(new RoomEntity("0205", roomStatusEnum.AVAILABLE), grand.getRoomTypeId());
        RoomEntity grand3 = roomSessionBeanLocal.createNewRoom(new RoomEntity("0305", roomStatusEnum.AVAILABLE), grand.getRoomTypeId());
        RoomEntity grand4 = roomSessionBeanLocal.createNewRoom(new RoomEntity("0405", roomStatusEnum.AVAILABLE), grand.getRoomTypeId());
        RoomEntity grand5 = roomSessionBeanLocal.createNewRoom(new RoomEntity("0505", roomStatusEnum.AVAILABLE), grand.getRoomTypeId());
//        SimpleDateFormat inputDateFormat = new SimpleDateFormat("d/M/y");
        //        SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        //        Date validityStartDate;
        //        Date validityEndDate;
        //        RoomRateEntity deluxePeak = roomRateSessionBeanLocal.createNewRoomRate(new RoomRateEntity("Deluxe Peak", new BigDecimal("250"), rateTypeEnum.PEAK, inputDateFormat.parse("19/11/2018") , inputDateFormat.parse("28/12/2018"), deluxe));
        //        RoomRateEntity deluxePromo= roomRateSessionBeanLocal.createNewRoomRate(new RoomRateEntity("Deluxe Promotion", new BigDecimal("130"), rateTypeEnum.PROMOTION, inputDateFormat.parse("19/11/2018") , inputDateFormat.parse("28/12/2018"), deluxe));
        //        
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
