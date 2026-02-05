package fr.cnamts.cpam33.ordonnance.infrastructure.out.entities.ordonnances;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "error_catalog")
public class ErrorCatalogEntity {

    @Id
    @Column(name = "code", length = 150, nullable = false)
    private String code;

    @Column(name = "bounded_context", nullable = false, length = 100)
    private String boundedContext;

    @Column(name = "http_status", nullable = false)
    private int httpStatus;

    @Column(name = "message", nullable = false)
    private String message;

    @Column(name = "description")
    private String description;

    @Column(name = "active", nullable = false)
    private boolean active = true;

    public ErrorCatalogEntity() {
    }

    public ErrorCatalogEntity(String code, String boundedContext, int httpStatus, String message, String description, boolean active) {
        this.code = code;
        this.boundedContext = boundedContext;
        this.httpStatus = httpStatus;
        this.message = message;
        this.description = description;
        this.active = active;
    }

    public String getCode() {
        return code;
    }

    public String getBoundedContext() {
        return boundedContext;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }

    public boolean isActive() {
        return active;
    }

    public void activate() {
        this.active = true;
    }

    public void deactivate() {
        this.active = false;
    }

    public ErrorCatalogEntity setCode(String code) {
        this.code = code;
        return this;
    }

    public ErrorCatalogEntity setBoundedContext(String boundedContext) {
        this.boundedContext = boundedContext;
        return this;
    }

    public ErrorCatalogEntity setHttpStatus(int httpStatus) {
        this.httpStatus = httpStatus;
        return this;
    }

    public ErrorCatalogEntity setMessage(String message) {
        this.message = message;
        return this;
    }

    public ErrorCatalogEntity setDescription(String description) {
        this.description = description;
        return this;
    }

    public ErrorCatalogEntity setActive(boolean active) {
        this.active = active;
        return this;
    }
}
