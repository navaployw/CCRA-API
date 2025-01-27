/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.arg.ccra3.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author kumpeep
 */

public class Duplicate {
   
private Boolean duplicate;
private String chkRemark;

    public Boolean getDuplicate() {
        return duplicate;
    }

    public void setDuplicate(Boolean duplicate) {
        this.duplicate = duplicate;
    }

    public String getChkRemark() {
        return chkRemark;
    }

    public void setChkRemark(String chkRemark) {
        this.chkRemark = chkRemark;
    }

    @Override
    public String toString() {
        return "Duplicate{" + "duplicate=" + duplicate + ", chkRemark=" + chkRemark + '}';
    }


    
    
}
