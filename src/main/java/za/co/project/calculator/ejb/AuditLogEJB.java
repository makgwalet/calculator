package za.co.project.calculator.ejb;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import za.co.project.calculator.model.AuditLog;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class AuditLogEJB {

    @PersistenceContext(unitName = "default")
    private EntityManager entityManager;


    public void recordCalculation(String type, String details) {
        AuditLog auditLog = new AuditLog();
        auditLog.setType(type);
        auditLog.setDetails(details);
        auditLog.setTimestamp(LocalDateTime.now());

        entityManager.persist(auditLog);
    }

    public List<AuditLog> queryAuditLogs(String username, LocalDate fromDate, LocalDate toDate) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<AuditLog> query = criteriaBuilder.createQuery(AuditLog.class);
        Root<AuditLog> root = query.from(AuditLog.class);
        query.select(root);

        List<Predicate> predicates = new ArrayList<>();
        if (username != null && !username.isEmpty()) {
            predicates.add(criteriaBuilder.equal(root.get("username"), username));
        }
        if (fromDate != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("timestamp"), fromDate.atStartOfDay()));
        }
        if (toDate != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("timestamp"), toDate.atTime(23, 59, 59)));
        }

        if (!predicates.isEmpty()) {
            query.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));
        }

        query.orderBy(criteriaBuilder.desc(root.get("timestamp")));

        return entityManager.createQuery(query).getResultList();
    }
}
