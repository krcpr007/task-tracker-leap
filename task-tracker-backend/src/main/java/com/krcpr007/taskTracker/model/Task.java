package com.krcpr007.taskTracker.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.krcpr007.taskTracker.model.audit.UserDateAudit;

import java.time.LocalDate;

@Entity
@Table(name = "tasks")
public class Task extends UserDateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank   // name is required and it should not be NOT NULL and not empty
    @Size(max = 200)
    private String name; // name as title

    @Column
    private LocalDate date;  // due_date

    @Column
    private boolean complete = false; // complete or not complete

    @Column 
    private String description;  // description

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    // getters and setters omitted for brevity
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDescription(){
        return description;
    }
    public void setDescription(String description){
        this.description = description;
    }

}
