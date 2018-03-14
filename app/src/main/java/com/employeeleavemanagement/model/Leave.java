package com.employeeleavemanagement.model;

/**
 * Created by VIKRAM R on 13/03/2018.
 */

public class Leave {
    int leaveID;
    int empID;
    String leaveFrom;
    int sessionFrom;
    String leaveTo;
    int sessionTo;

    public Leave() {
    }

    public Leave(int leaveID, int empID, String leaveFrom, String leaveTo) {
        this.leaveID = leaveID;
        this.empID = empID;
        this.leaveFrom = leaveFrom;
        this.leaveTo = leaveTo;
    }

    public Leave(int leaveID, int empID, String leaveFrom, int sessionFrom, String leaveTo, int sessionTo) {
        this.leaveID = leaveID;
        this.empID = empID;
        this.leaveFrom = leaveFrom;
        this.sessionFrom = sessionFrom;
        this.leaveTo = leaveTo;
        this.sessionTo = sessionTo;
    }

    public int getLeaveID() {
        return leaveID;
    }

    public void setLeaveID(int leaveID) {
        this.leaveID = leaveID;
    }

    public int getEmpID() {
        return empID;
    }

    public void setEmpID(int empID) {
        this.empID = empID;
    }

    public String getLeaveFrom() {
        return leaveFrom;
    }

    public void setLeaveFrom(String leaveFrom) {
        this.leaveFrom = leaveFrom;
    }

    public String getLeaveTo() {
        return leaveTo;
    }

    public void setLeaveTo(String leaveTo) {
        this.leaveTo = leaveTo;
    }

    public int getSessionFrom() {
        return sessionFrom;
    }

    public void setSessionFrom(int sessionFrom) {
        this.sessionFrom = sessionFrom;
    }

    public int getSessionTo() {
        return sessionTo;
    }

    public void setSessionTo(int sessionTo) {
        this.sessionTo = sessionTo;
    }
}
