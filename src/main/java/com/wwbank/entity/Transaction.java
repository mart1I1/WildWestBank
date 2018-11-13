package com.wwbank.entity;

import com.wwbank.util.Criteriable;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "transaction")
public class Transaction implements Criteriable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "id_acc_sender")
    @NotNull @Positive
    private Integer idAccSender;

    @Column(name = "id_acc_receiver")
    @NotNull @Positive
    private Integer idAccReceiver;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date")
    private Date date;

    @Column(name = "money")
    @NotNull @Positive
    private Double money;

    public Transaction() {
    }

    public Transaction(Integer idAccSender, Integer idAccReceiver, Date date, Double money) {
        this.idAccSender = idAccSender;
        this.idAccReceiver = idAccReceiver;
        this.date = date;
        this.money = money;
    }

    public Integer getId() {
        return id;
    }

    public Integer getIdAccSender() {
        return idAccSender;
    }

    public Integer getIdAccReceiver() {
        return idAccReceiver;
    }

    public Date getDate() {
        return date;
    }

    public Double getMoney() {
        return money;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setIdAccSender(Integer idAccSender) {
        this.idAccSender = idAccSender;
    }

    public void setIdAccReceiver(Integer idAccReceiver) {
        this.idAccReceiver = idAccReceiver;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Transaction)) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(idAccSender, that.idAccSender) &&
                Objects.equals(idAccReceiver, that.idAccReceiver) &&
                Objects.equals(date, that.date) &&
                Objects.equals(money, that.money);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, idAccSender, idAccReceiver, date, money);
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", idAccSender=" + idAccSender +
                ", idAccReceiver=" + idAccReceiver +
                ", date=" + date +
                ", money=" + money +
                '}';
    }
}
