package com.wwbank.entity;

import com.wwbank.util.Criteriable;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "account")
public class Account implements Criteriable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "id_client")
    private Integer id_client;

    @Column(name = "money")
    private Double money;

    public Integer getId() {
        return id;
    }

    public Integer getId_client() {
        return id_client;
    }

    public Double getMoney() {
        return money;
    }

    public Account() {
    }

    public Account(Integer id_client, Double money) {
        this.id_client = id_client;
        this.money = money;
    }

    public Account(Integer id, Integer id_client, Double money) {
        this.id = id;
        this.id_client = id_client;
        this.money = money;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account)) return false;
        Account account = (Account) o;
        return Objects.equals(id, account.id) &&
                Objects.equals(id_client, account.id_client) &&
                Objects.equals(money, account.money);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, id_client, money);
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", id_client=" + id_client +
                ", money=" + money +
                '}';
    }
}

