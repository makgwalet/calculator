package za.co.project.calculator.beans;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.faces.bean.ManagedBean;
import jakarta.faces.bean.SessionScoped;
import za.co.project.calculator.ejb.AuditLogEJB;
import za.co.project.calculator.model.AuditLog;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@ManagedBean
@SessionScoped
@RolesAllowed("ADMIN")
public class QueryBean implements Serializable {

    @EJB
    private AuditLogEJB auditLogEJB;

    private String username;
    private LocalDate fromDate;
    private LocalDate toDate;
    private List<AuditLog> auditLogs;

    public void queryAuditLogs() {
        auditLogs = auditLogEJB.queryAuditLogs(username, fromDate, toDate);
    }
}
