package com.byrybdyk.lb1.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "session_mapping")
public class SessionMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "spring_session_id")
    private String springSessionId;

    @Column(name = "keycloak_sid")
    private String keycloakSid;


    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    @PrePersist
    protected void onCreate() {
        this.setCreationDate(new Date());
    }

    public void setSpringSessionId(String springSessionId) {
        this.springSessionId = springSessionId;
    }

    public void setKeycloakSid(String keycloakSid) {
        this.keycloakSid = keycloakSid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSpringSessionId() {
        return springSessionId;
    }

    public String getKeycloakSid() {
        return keycloakSid;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}
