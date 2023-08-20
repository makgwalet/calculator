package za.co.project.calculator.ejb;

import jakarta.annotation.Resource;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.security.enterprise.SecurityContext;
import za.co.project.calculator.model.User;

@Stateless
public class UserEJB {

    @PersistenceContext(unitName = "default")
    private EntityManager entityManager;

    @Resource
    private SecurityContext securityContext;

    public User authenticateUser(String username, String password) {
        TypedQuery<User> query = entityManager.createQuery(
                "SELECT u FROM User u WHERE u.username = :username AND u.password = :password", User.class);
        query.setParameter("username", username);
        query.setParameter("password", password);

        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public boolean isUserInRole(String role) {
        User user = (User) securityContext.getCallerPrincipal();
        return securityContext.isCallerInRole(role);
    }
}