package com.web.wallet.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Entity
@Getter
@Setter
public class Cards {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private Long balance;

    @OneToMany(mappedBy = "cards", cascade = CascadeType.ALL)
    private List<Journal> journalList;

    @ManyToOne(fetch = FetchType.LAZY)
    private Users users;

    public Cards(String name, Long balance) {
        this.name = name;
        this.balance = balance;
    }

    public Cards() {
    }

    @Override
    public String toString() {
        return "Cards{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", balance=" + balance +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cards cards = (Cards) o;
        return Objects.equals(name, cards.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
