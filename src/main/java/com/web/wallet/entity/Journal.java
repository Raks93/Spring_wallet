package com.web.wallet.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Getter
@Setter
public class Journal {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long amount;

    private Date date;

    @ManyToOne(fetch = FetchType.LAZY)
    private Users users;

    @ManyToOne(fetch = FetchType.LAZY)
    private Categories categories;

    @ManyToOne(fetch = FetchType.LAZY)
    private Cards cards;

    public Journal(Long amount, Date date, Boolean inOutMoney) {
        this.amount = amount;
        this.date = date;
    }

    public Journal() {
    }
}
