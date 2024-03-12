/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.arg.ccra3.models;

public class Duplicate {
   
private Boolean duplicateFlag;
private String chkRemark;

    public Boolean getDuplicate() {
        return duplicateFlag;
    }

    public void setDuplicate(Boolean duplicate) {
        this.duplicateFlag = duplicate;
    }

    public String getChkRemark() {
        return chkRemark;
    }

    public void setChkRemark(String chkRemark) {
        this.chkRemark = chkRemark;
    }

    @Override
    public String toString() {
        return "Duplicate{" + "duplicate=" + duplicateFlag + ", chkRemark=" + chkRemark + '}';
    }


    
    
}
