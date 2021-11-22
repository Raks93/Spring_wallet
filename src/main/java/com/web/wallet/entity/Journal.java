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

    private Boolean inOutMoney;

    @ManyToOne(fetch = FetchType.EAGER)
    private Users users;

    @ManyToOne(fetch = FetchType.EAGER)
    private Categories categories;

    @ManyToOne(fetch = FetchType.EAGER)
    private Cards cards;

    public Journal(Long amount, Date date, Boolean inOutMoney) {
        this.amount = amount;
        this.date = date;
        this.inOutMoney = inOutMoney;
    }

    public Journal() {
    }
}
