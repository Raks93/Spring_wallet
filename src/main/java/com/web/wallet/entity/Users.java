package com.web.wallet.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "usr")
@Setter
@Getter
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String username;

    private String password;

    private Boolean active;

    @Override
    public String toString() {
        return "Users{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", active=" + active +
                ", roles=" + roles +
                '}';
    }

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL)
    private List<Cards> cardsList;

    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL)
    private List<Journal> journalList;

    @OneToMany(mappedBy = "users",cascade = CascadeType.ALL)
    private List<Categories> categoriesList;



    public Users(String username, String password, boolean active, Set<Role> roles) {
        this.username = username;
        this.password = password;
        this.active = active;
        this.roles = roles;
    }

    public Users() {

    }
}
