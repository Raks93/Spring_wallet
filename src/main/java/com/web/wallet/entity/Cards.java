package com.web.wallet.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class Cards {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long balance;

    @OneToMany(mappedBy = "cards", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Journal> journalList;

    @ManyToOne(fetch = FetchType.LAZY)
    private Users users;

    public Cards(Long balance) {
        this.balance = balance;
    }

    public Cards() {
    }
}
