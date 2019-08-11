package com.shmigel.todo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Entity
public class User {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, length = 36)
    private String username;

    @Column(nullable = false, length = 36)
    @JsonIgnore
    private String password;

    @OneToMany(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            mappedBy = "user")
    @JsonIgnore
    private List<Task> tasks = new ArrayList<>();

    public User() {
    }

    public User(UserAuthDetails user) {
        this(user.getUsername(), user.getPassword());
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void addTask(Task task) {
        this.tasks.add(task);
        task.setUser(this);
    }

    public void removeTask(Task task) {
        this.tasks.remove(task);
        task.setUser(null);
    }
}
