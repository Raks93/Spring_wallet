package com.web.wallet.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class StartPage {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private Long amount;

    public StartPage(String name, Long amount) {
        this.name = name;
        this.amount = amount;
    }

    public StartPage() {

    }
}
