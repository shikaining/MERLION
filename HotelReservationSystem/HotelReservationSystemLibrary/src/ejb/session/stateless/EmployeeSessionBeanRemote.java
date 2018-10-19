package ejb.session.stateless;

import entity.EmployeeEntity;
import java.util.List;
import util.exception.InvalidLoginCredentialException;

public interface EmployeeSessionBeanRemote {

    EmployeeEntity createNewEmployee(EmployeeEntity newEmployeeEntity);

    EmployeeEntity employeeLogin(String username, String password) throws InvalidLoginCredentialException;

    List<EmployeeEntity> retrieveAllEmployees();
    
}
