package com.arg.ccra3.dao.util;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;

public class AIMergesUtil {
    private AIMergesUtil(){throw new AssertionError("nah");}
    public static List<Integer> getListAIOrder(JdbcTemplate jdbcTemplate, String sql, Integer uGroupAIID)//wtf is this algorithm
    {
        final List<Integer> result = new LinkedList<>();
        List<Integer> gid = new ArrayList<>();
        List<Integer> tmpgid = new ArrayList<>();

        result.add(uGroupAIID);
        gid.add(uGroupAIID);
        
        for (Iterator i = gid.iterator(); i.hasNext();)
        {
            for (int index : gid)
                tmpgid.add(index);
            
            gid.clear();

            for (int index : tmpgid){
                jdbcTemplate.query(
                    sql,
                    (ResultSet rs) -> {
                        do{
                            int gAI_id = rs.getInt("groupaiid");
                            result.add(gAI_id);
                            gid.add(gAI_id);
                        }while (rs.next());
                    },
                    index
                );
            }
            tmpgid.clear();
        }
        
        return result;
    }
}
