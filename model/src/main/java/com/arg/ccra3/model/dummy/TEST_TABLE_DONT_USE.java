package com.arg.ccra3.model.dummy;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TEST_TABLE_DONT_USE")
public class TEST_TABLE_DONT_USE {
    
    /*
    
        conversionNotSupported Failed to convert property value of type 'java.util.Date' to required type 'java.time.ZonedDateTime'
    
    */
    @Id
    private int dummyid;
    private Instant instant;
    private LocalDate localdate;
    private ZonedDateTime zonedDateTime;

    public int getDummyid() {
        return dummyid;
    }

    public void setDummyid(int dummyid) {
        this.dummyid = dummyid;
    }

    public Instant getInstant() {
        return instant;
    }

    public void setInstant(Instant instant) {
        this.instant = instant;
    }

    public LocalDate getLocaldate() {
        return localdate;
    }

    public void setLocaldate(LocalDate localdate) {
        this.localdate = localdate;
    }

    public ZonedDateTime getZonedDateTime() {
        return zonedDateTime;
    }

    public void setZonedDateTime(ZonedDateTime zonedDateTime) {
        this.zonedDateTime = zonedDateTime;
    }
    
}
