package com.employeeleavemanagement.model;

import java.io.Serializable;

/**
 * Created by VIKRAM R on 13/03/2018.
 */

public class Employee implements Serializable {
    int empID;
    String empName;
    String empImage;
    int empAge;
    String empGender;
    String empDesignation;

    public Employee() {
    }


    public Employee(int empID, String empName, String empImage, int empAge, String empGender, String empDesignation) {
        this.empID = empID;
        this.empName = empName;
        this.empImage = empImage;
        this.empAge = empAge;
        this.empGender = empGender;
        this.empDesignation = empDesignation;
    }

    public int getEmpID() {
        return empID;
    }

    public void setEmpID(int empID) {
        this.empID = empID;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getEmpImage() {
        return empImage;
    }

    public void setEmpImage(String empImage) {
        this.empImage = empImage;
    }

    public int getEmpAge() {
        return empAge;
    }

    public void setEmpAge(int empAge) {
        this.empAge = empAge;
    }

    public String getEmpGender() {
        return empGender;
    }

    public void setEmpGender(String empGender) {
        this.empGender = empGender;
    }

    public String getEmpDesignation() {
        return empDesignation;
    }

    public void setEmpDesignation(String empDesignation) {
        this.empDesignation = empDesignation;
    }
}
