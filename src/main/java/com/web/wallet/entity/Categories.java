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

    @ManyToOne(fetch = FetchType.LAZY)
    private Users  users;

    @OneToMany(mappedBy = "categories", cascade = CascadeType.ALL)
    private List<Journal> journalList;

    public Categories(String name, Boolean income) {
        this.name = name;
        this.income = income;
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
