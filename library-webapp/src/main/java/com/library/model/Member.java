package com.library.model;

import java.math.BigDecimal;

public class Member {
    private int memberId;
    private String name;
    private String email;
    private String phoneNumber;
    private int activeLoanCount;
    private BigDecimal outstandingFine = BigDecimal.ZERO;
    private BigDecimal lifetimeFineTotal = BigDecimal.ZERO;

    public Member() {
    }

    public Member(int memberId, String name, String email, String phoneNumber) {
        this.memberId = memberId;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public Member(
            int memberId,
            String name,
            String email,
            String phoneNumber,
            int activeLoanCount,
            BigDecimal outstandingFine,
            BigDecimal lifetimeFineTotal) {
        this.memberId = memberId;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.activeLoanCount = activeLoanCount;
        this.outstandingFine = outstandingFine == null ? BigDecimal.ZERO : outstandingFine;
        this.lifetimeFineTotal = lifetimeFineTotal == null ? BigDecimal.ZERO : lifetimeFineTotal;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getActiveLoanCount() {
        return activeLoanCount;
    }

    public void setActiveLoanCount(int activeLoanCount) {
        this.activeLoanCount = activeLoanCount;
    }

    public BigDecimal getOutstandingFine() {
        return outstandingFine;
    }

    public void setOutstandingFine(BigDecimal outstandingFine) {
        this.outstandingFine = outstandingFine == null ? BigDecimal.ZERO : outstandingFine;
    }

    public BigDecimal getLifetimeFineTotal() {
        return lifetimeFineTotal;
    }

    public void setLifetimeFineTotal(BigDecimal lifetimeFineTotal) {
        this.lifetimeFineTotal = lifetimeFineTotal == null ? BigDecimal.ZERO : lifetimeFineTotal;
    }
}
