package com.example.sydney.psov3;

/**
 * Created by Jen on 9/21/2017.
 */

public class Cashinout {
    private String cioTransNum;
    private String cioCashierNum;
    private String cioDateTime;
    private double cioAddCash;
    private double cioMinusCash;
    private double cioCurrentCash;
    private String cioXreport;
    private String cioZreport;

    public Cashinout() {
    }

    public String getCioTransNum() {
        return cioTransNum;
    }

    public void setCioTransNum(String cioTransNum) {
        this.cioTransNum = cioTransNum;
    }

    public String getCioCashierNum() {
        return cioCashierNum;
    }

    public void setCioCashierNum(String cioCashierNum) {
        this.cioCashierNum = cioCashierNum;
    }

    public String getCioDateTime() {
        return cioDateTime;
    }

    public void setCioDateTime(String cioDateTime) {
        this.cioDateTime = cioDateTime;
    }

    public double getCioAddCash() {
        return cioAddCash;
    }

    public void setCioAddCash(double cioAddCash) {
        this.cioAddCash = cioAddCash;
    }

    public double getCioMinusCash() {
        return cioMinusCash;
    }

    public void setCioMinusCash(double cioMinusCash) {
        this.cioMinusCash = cioMinusCash;
    }

    public double getCioCurrentCash() {
        return cioCurrentCash;
    }

    public void setCioCurrentCash(double cioCurrentCash) {
        this.cioCurrentCash = cioCurrentCash;
    }

    public String getCioXreport() {
        return cioXreport;
    }

    public void setCioXreport(String cioXreport) {
        this.cioXreport = cioXreport;
    }

    public String getCioZreport() {
        return cioZreport;
    }

    public void setCioZreport(String cioZreport) {
        this.cioZreport = cioZreport;
    }
}
