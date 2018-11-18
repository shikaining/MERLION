package ejb.session.stateless;

import entity.EmployeeEntity;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.EmployeeNotFoundException;
import util.exception.InvalidLoginCredentialException;

@Stateless
@Local(EmployeeSessionBeanLocal.class)
@Remote(EmployeeSessionBeanRemote.class)
public class EmployeeSessionBean implements EmployeeSessionBeanRemote, EmployeeSessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    @Override
    public EmployeeEntity createNewEmployee(EmployeeEntity newEmployeeEntity) {
        em.persist(newEmployeeEntity);
        em.flush();

        return newEmployeeEntity;
    }

    @Override
    public List<EmployeeEntity> retrieveAllEmployees() {
        Query query = em.createQuery("SELECT e FROM EmployeeEntity e");

        return query.getResultList();
    }

    @Override
    public EmployeeEntity retrieveEmployeeByUsername(String username) throws EmployeeNotFoundException {
        Query query = em.createQuery("SELECT e FROM EmployeeEntity e WHERE e.userName = :inUsername");
        query.setParameter("inUsername", username);

        try {
            return (EmployeeEntity) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new EmployeeNotFoundException("Employee with Username " + username + "does not exist!");
        }
    }

    @Override
    public EmployeeEntity employeeLogin(String username, String password) throws InvalidLoginCredentialException {
        try {

            EmployeeEntity employeeEntity = retrieveEmployeeByUsername(username);

            if (employeeEntity.getUserName().equals(username) && employeeEntity.getPassword().equals(password)) {

                return employeeEntity;

            } else {

                throw new InvalidLoginCredentialException("Invalid login credential");

            }

        } catch (EmployeeNotFoundException ex) {
            throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
        }
    }
}
