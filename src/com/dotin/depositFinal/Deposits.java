package com.dotin.depositFinal;

import java.math.BigDecimal;

/**
 * Created by msaeedi on 12/10/14.
 */
public abstract class Deposits implements Comparable<Deposits>{
    protected int customerNumber;
    protected BigDecimal depositBalance;
    protected int durationInDays;
    protected int interest;
    protected BigDecimal interestValue;

    public BigDecimal getInterestValue() {
        return interestValue;
    }

    public void setInterestValue(BigDecimal interestValue) {
        this.interestValue = interestValue;
    }

    public int getInterest() {
        return interest;
    }

    public void setInterest(int interest) {
        this.interest = interest;
    }

    public void setInterest() {

    }

    public int getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(int customerNumber) {
        this.customerNumber = customerNumber;
    }

    public BigDecimal getDepositBalance() {
        return depositBalance;
    }

    public void setDepositBalance(BigDecimal depositBalance) {
        this.depositBalance = depositBalance;
    }

    public int getDurationInDays() {
        return durationInDays;
    }

    public void setDurationInDays(int durationInDays) {
        this.durationInDays = durationInDays;
    }

    public void calculateInterest(){
        final BigDecimal num = new BigDecimal("36500");
        BigDecimal bigDurationInDays = new BigDecimal(durationInDays);
        BigDecimal bigInterestRate = new BigDecimal(interest);

       interestValue = ((depositBalance.multiply(bigInterestRate)).multiply(bigDurationInDays)).divide(num,2);

    }

    @Override
    public int compareTo(Deposits deposits) {
        return this.interestValue.compareTo(deposits.interestValue);
    }
}
