package com.wwbank.entity;

import com.wwbank.util.Criteriable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.Objects;

@Entity
@Table(name = "account")
public class Account implements Criteriable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "id_client")
    @NotNull @Positive
    private Integer idClient;

    @Column(name = "money")
    @NotNull
    private Double money;

    public Integer getId() {
        return id;
    }

    public Integer getIdClient() {
        return idClient;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public Account() {
    }

    public Account(Integer idClient, Double money) {
        this.idClient = idClient;
        this.money = money;
    }

    public Account(Integer id, Integer idClient, Double money) {
        this.id = id;
        this.idClient = idClient;
        this.money = money;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account)) return false;
        Account account = (Account) o;
        return Objects.equals(id, account.id) &&
                Objects.equals(idClient, account.idClient) &&
                Objects.equals(money, account.money);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, idClient, money);
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", idClient=" + idClient +
                ", money=" + money +
                '}';
    }
}

