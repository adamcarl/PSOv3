package com.example.sydney.psov3;

/**
 * Created by PROGRAMMER2 on 6/15/2017.
 */

public class Report {
    private String reportNumber;
    private String reportDate;
    private String reportTime;

    public Report(String reportNumber, String reportDate, String reportTime) {
        this.reportNumber = reportNumber;
        this.reportDate = reportDate;
        this.reportTime = reportTime;
    }

    public Report(){}

    public String getReportNumber() {
        return reportNumber;
    }

    public void setReportNumber(String reportNumber) {
        this.reportNumber = reportNumber;
    }

    public String getReportDate() {
        return reportDate;
    }

    public void setReportDate(String reportDate) {
        this.reportDate = reportDate;
    }

    public String getReportTime() {
        return reportTime;
    }

    public void setReportTime(String reportTime) {
        this.reportTime = reportTime;
    }

}
