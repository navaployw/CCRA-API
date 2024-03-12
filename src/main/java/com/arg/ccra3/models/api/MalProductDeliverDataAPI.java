package com.arg.ccra3.models.api;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "API_PRODUCTDELIVER_DATA")
public class MalProductDeliverDataAPI {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PRODUCTDELIVERID")
    private long productDeliverId;
    @Column(name = "TRANSACTIONID")
    private long transactionId;
    @Column(name = "EXPENSEID")
    private long expenseId;
    @Column(name = "OBJECTTYPE")
    private int objectType;
    @Column(name = "OBJECTNUMBER")
    private int objectNumber;
    @Column(name = "PRODUCTOBJECT", nullable = false)
    private String productObject;

    public long getProductDeliverId() {
        return productDeliverId;
    }

    public void setProductDeliverId(long productDeliverId) {
        this.productDeliverId = productDeliverId;
    }

    public long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(long transactionId) {
        this.transactionId = transactionId;
    }

    public long getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(long expenseId) {
        this.expenseId = expenseId;
    }

    public int getObjectType() {
        return objectType;
    }

    public void setObjectType(int objectType) {
        this.objectType = objectType;
    }

    public int getObjectNumber() {
        return objectNumber;
    }

    public void setObjectNumber(int objectNumber) {
        this.objectNumber = objectNumber;
    }

    public String getProductObject() {
        return productObject;
    }

    public void setProductObject(String productObject) {
        this.productObject = productObject;
    }

        
}
