package com.byrybdyk.lb1.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "admin_requests")
public class AdminRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private boolean approved = false;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date requestDate;

    public AdminRequest() {
        this.requestDate = new Date();
    }

    public AdminRequest(String username, String password) {
        this.username = username;
        this.password = password;
        this.requestDate = new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }
}
