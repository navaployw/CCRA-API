
package com.arg.ccra3.models.api;

import com.arg.cb2.inquiry.data.ReportData;
import java.util.ArrayList;
import java.util.List;

public class DataBlockStream extends ReportData{
    static final long serialVersionUID = -66666666666666666l;
    private List<Object> datas  = new ArrayList<>();
    
    public DataBlockStream(String documentLanguage){
       super(documentLanguage);
    }
    
    public void add(Object datablock) {
        datas.add(datablock);
    }

    public List<Object> getDatas() {
        return datas;
    }

    public void setDatas(List<Object> data) {
        datas = data;
    }

    @Override
    public String toString() {
        return "DataBlockStream{" + "datas=" + datas + '}';
    }
    
    
}
