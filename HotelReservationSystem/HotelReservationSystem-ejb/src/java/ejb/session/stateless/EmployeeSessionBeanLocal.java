package ejb.session.stateless;

import entity.EmployeeEntity;
import util.exception.EmployeeNotFoundException;


public interface EmployeeSessionBeanLocal {

    EmployeeEntity createNewEmployee(EmployeeEntity newEmployeeEntity);

    EmployeeEntity retrieveEmployeeByUsername(String username) throws EmployeeNotFoundException;
    
}
