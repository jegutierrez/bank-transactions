package ar.com.olx.entity;

import javax.persistence.*;

/**
 * Created by jegutierrez on 08/10/16.
 */

@Entity
public class Account {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="accountId")
    @SequenceGenerator(name="accountId", initialValue=1, allocationSize=100000)
    private long id;

    private String bank;
    private String country;
    private double balance;

    public Account() {
    }

    public Account(String bank, String country, double balance) {
        this.bank = bank;
        this.country = country;
        this.balance = balance;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
