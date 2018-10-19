package ejb.session.singleton;

import ejb.session.stateless.EmployeeSessionBeanLocal;
import entity.EmployeeEntity;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import util.enumeration.employeeAccessRightEnum;
import util.exception.EmployeeNotFoundException;


@Singleton
@LocalBean
@Startup
public class DataInitSessionBean {
   
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
           employeeSessionBeanLocal.createNewEmployee(new EmployeeEntity("Default","Default","Admin","Password",employeeAccessRightEnum.SYSTEMADMIN));
//           
       }
}

