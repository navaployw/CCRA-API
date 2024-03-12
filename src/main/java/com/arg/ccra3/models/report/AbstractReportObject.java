
package com.arg.ccra3.models.report;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class AbstractReportObject {
    
    protected Set<String> skipKey = new HashSet<>();
    
    abstract void setNullToPropertiesIfNoValue();
    protected void setNullToMapPropertiesIfNoValue(Object map){
        if(map instanceof Map)
            setNullToMapPropertiesIfNoValue((Map<String, Object>)map);
    }
    protected void setNullToMapPropertiesIfNoValue(Map<String, Object> map){
        for(var ent : map.entrySet()){
            String key = ent.getKey();
            var val = ent.getValue();
            
            if(skipKey.contains(key))
                continue;
            if(val == null)
                continue;
            
            if(val instanceof BigDecimal bdVal){//switch pattern requires preview config and messing with pom and xml and this is java 17 can't use more modern switch pattern from java 19
                if(bdVal.equals(BigDecimal.ZERO))
                    map.put(key, null);
            }
            else if(val instanceof BigInteger biVal){
                if(biVal.equals(BigInteger.ZERO))
                    map.put(key, null);
            }
            else if(val instanceof Number nVal){
                if(nVal.doubleValue()== 0d)
                    map.put(key, null);
            }
            else if(val instanceof String str){
                if(str.isBlank())
                    map.put(key, null);
            }
            else if(val instanceof Map)
                setNullToMapPropertiesIfNoValue((Map<String, Object>)val);
            else if(val instanceof AbstractReportObject reVal)
                reVal.setNullToPropertiesIfNoValue();
            else if(val instanceof List)
                setNullToElementsIfZeroOrBlankString((List<Object>) val);
        }
    }
    protected void setNullToElementsIfZeroOrBlankString(List<Object> list){
        if(list == null)
            return;
        
        for(var ent : list){
            if(ent == null)
                continue;
            if(ent instanceof Map)
                setNullToMapPropertiesIfNoValue((Map<String, Object>)ent);
        }
    }
}
