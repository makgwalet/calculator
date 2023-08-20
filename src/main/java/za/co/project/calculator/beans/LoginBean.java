package za.co.project.calculator.beans;

import jakarta.ejb.EJB;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.bean.ManagedBean;
import jakarta.faces.bean.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.security.enterprise.SecurityContext;
import za.co.project.calculator.ejb.UserEJB;
import za.co.project.calculator.model.User;

@ManagedBean(name = "loginBean")
@SessionScoped
public class LoginBean {

    @EJB
    private UserEJB userEJB;

    @Inject
    private SecurityContext securityContext; // Inject SecurityContext

    private String username;
    private String password;

    public String login() {
        User user = userEJB.authenticateUser(username, password);
        if (user != null) {
            if (isUserInRole("USER")) {
                // User is authenticated and authorized as a user
                return "user/dashboard.xhtml?faces-redirect=true";
            } else if (isUserInRole("ADMIN")) {
                // User is authenticated and authorized as an admin
                return "admin/dashboard.xhtml?faces-redirect=true";
            } else {
                // User is not authorized, show error message
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Access denied"));
                return null;
            }
        } else {
            // Invalid login, show error message
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Invalid credentials"));
            return null;
        }
    }

    private boolean isUserInRole(String role) {
        return securityContext.isCallerInRole(role);
    }
}