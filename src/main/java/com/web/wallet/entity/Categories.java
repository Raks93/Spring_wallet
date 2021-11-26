package com.web.wallet.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class Categories {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private Boolean income;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Users>  usersList;

    @OneToMany(mappedBy = "categories", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Journal> journalList;

    public Categories(String name) {
        this.name = name;
    }

    public Categories() {
    }

    @Override
    public String toString() {
        return "Categories{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", income=" + income +
                '}';
    }
}
