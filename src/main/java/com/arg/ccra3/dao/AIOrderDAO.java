
package com.arg.ccra3.dao;

import com.arg.ccra3.dao.util.ReportResource;
import com.arg.cb2.spm.core.SPMConstants;
import com.arg.ccra3.common.InquiryConstants;
import com.arg.ccra3.dao.util.AIMergesUtil;
import static com.arg.ccra3.dao.util.InquiryUtilities.getPeriod;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;


public class AIOrderDAO {
    static int CCRA = SPMConstants.GroupContants.CCRA_ID;
    private String AI = "AI#";
    private final Map<String, String> aiNames = new HashMap<>();

    private final Map<String, AIOrder> aiHash = new HashMap<>();
    private final Map<String, AIOrder> unloadHash = new HashMap<>();

    private final List<String> groupIDs = new ArrayList<>();

    private final JdbcTemplate jdbcTemplate;

    public AIOrderDAO(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
        AI = ReportResource.getString("report.ai-prefix");
    }
    
    public void setCacheAIOrders(Integer CBUID,int years)
       
    {
        clearCache();
        
        String period = getPeriod(years);
        Object[] params = new Object[]{CBUID, period};

        jdbcTemplate.query(
            ReportResource.getString("reportcreator.preparedstatement.aiorderlist.credit"),
            (ResultSet rs) -> {
                do{
                    addAIIndex(
                        rs.getInt("groupid"),
                        rs.getString("aicode"),
                        rs.getString("group_name_en"),
                        rs.getString("group_name_lo"),
                        rs.getString("loc_branch_id"),
                        rs.getString("acc_mnger_code"),
                        rs.getString("customer_no"),
                        rs.getString("brc_no"),
                        rs.getString("ci_no"),
                        rs.getString("other_reg_no"),
                        rs.getString("place_of_reg"),
                        rs.getDate("revoc_effect_date")
                    );
                }while (rs.next());
            },
            params
        );

        jdbcTemplate.query(
            ReportResource.getString("reportcreator.preparedstatement.aiorderlist.unload"),
            (ResultSet rs) -> {
                do{
                    addAIIndexUnload(
                        rs.getInt("groupid"),
                        rs.getString("aicode"), rs.getString("group_name_en"),
                        rs.getString("group_name_lo")
                    );
                }while (rs.next()) ;
            },
            params
        );
    }
    private void clearCache(){
        groupIDs.clear();
        aiHash.clear();
        unloadHash.clear();
    }

    public void addAIIndex(Integer groupID, String aiCode, String nameEN,
        String nameLO, String locBranchID, String accMngerCode,
        String customerNO, String HKBRCNO, String HKCINO, String otherRegNO,
        String placeOfReg, Date revocEffect)
    {
        String orderID = AI + (groupIDs.size() + 1);

        groupIDs.add(groupID.toString());
        aiHash.put(groupID.toString(),
            new AIOrder(groupID, aiCode, orderID, nameEN, nameLO, locBranchID,
                accMngerCode, customerNO, HKBRCNO, HKCINO, otherRegNO,
                placeOfReg, revocEffect));
    }
    public void addAIIndexUnload(Integer groupID, String aiCode, String nameEN,
        String nameLO)
    {
        AIOrder ai = aiHash.get(groupID.toString());

        if(ai != null)
            unloadHash.put(groupID.toString(),
                new AIOrder(groupID, aiCode, ai.getOrderID(), nameEN, nameLO, null, null,
                    null, null, null, null, null, null));
        
        else{
            String orderID = AI + (groupIDs.size() + 1);
            groupIDs.add(groupID.toString());
            unloadHash.put(groupID.toString(),
                new AIOrder(groupID, aiCode, orderID, nameEN, nameLO, null, null,
                    null, null, null, null, null, null));
        }

    }
    public AIOrder getAI(Integer groupID)
    {
        if (null == groupID ||  groupIDs.isEmpty())
            return null;

        AIOrder order = aiHash.get(groupID.toString());
        if (null == order)
            order = unloadHash.get(groupID.toString());

        return order;
    }
    

    public String getAccMngerCode(Integer uGroupID){
        AIOrder ai = aiHash.get(uGroupID.toString());
        return ai == null ? null : ai.getAccMngerCode();
    }

    public String getCustomerCode(Integer uGroupID){
        AIOrder ai = aiHash.get(uGroupID.toString());
        return ai == null ? null : ai.getCustomerNO();
    }
    public String getHKBRCNO(Integer uGroupID){
        AIOrder ai = aiHash.get(uGroupID.toString());
        return ai == null ? null : ai.getHKBRCNO();
    }
    public String getHKCINO(Integer uGroupID){
        AIOrder ai = aiHash.get(uGroupID.toString());
        return ai == null ? null : ai.getHKCINO();
    }
    public String getLocBranchID(Integer uGroupID){
        AIOrder ai = aiHash.get(uGroupID.toString());
        return ai == null ? null : ai.getLocBranchID();
    }
    public AIOrder[] getOrderAIList(){
        AIOrder[] aiOrder = new AIOrder[groupIDs.size()];
        int s = 0;
        for (String groupId : groupIDs){
            aiOrder[s++] = aiHash.get(groupId);
        }
        return aiOrder;
    }
    public String getOtherRegNO(Integer uGroupID){
        AIOrder ai = aiHash.get(uGroupID.toString());
        return ai == null ? null : ai.getOtherRegNO();
    }
    public String getPlaceOfReg(Integer uGroupID){
        AIOrder ai = aiHash.get(uGroupID.toString());
        return ai == null ? null : ai.getPlaceOfReg();
    }
    public Date getRevocEffectDate(Integer uGroupID){
        AIOrder ai = aiHash.get(uGroupID.toString());
        return ai == null ? null : ai.getRevocEffectDate();
    }
    public boolean getSubmissionFlag(Integer uGroupID)
    {//????????????????wtf??????????????????
        AIOrder ai = (AIOrder) aiHash.get(uGroupID.toString());

        if (null == ai)
        {
            return false;
        }

        return true;
    }


    

    public void clearAIName()
    {
          synchronized (aiNames)
          {
              aiNames.clear();
           }
    }

    public void delUGroupAIID(Integer uGroupAIID)
    {
        Iterator<String> itGrouopAIID = aiNames.keySet().iterator();
        List<String> list = new ArrayList<String>();
        while(itGrouopAIID.hasNext())
        {
             String gKey = (String)itGrouopAIID.next();
             Boolean delKey = gKey.startsWith(uGroupAIID+":",0);
             if(delKey)
             {

                 list.add(gKey);

             }
        }
        for (Object gKey : list)
        {
             aiNames.remove(gKey);
        }
    }
  public String getAIName(
        Integer uGroupAIID, Integer groupID,
        String documentLanguage , Integer cbuid
    ){
        final String gKey = uGroupAIID + ":" + cbuid + ":" + groupID + ":" + documentLanguage;

        if (!aiNames.containsKey(gKey)){//The "Double-Checked Locking idiom is Broken"

            AIOrder ai = aiHash.get(groupID.toString());
            if (null == ai)
                ai = unloadHash.get(groupID.toString());
            if (null == ai){
                aiNames.put(gKey, "");
                return null;
            }

            String sql = ReportResource.getString(
                    "reportcreator.preparedstatement.aiorderlist.credit.merge");
            List<Integer> groupAIIds = AIMergesUtil.getListAIOrder(jdbcTemplate, sql, uGroupAIID);

            if (null == uGroupAIID && CCRA != uGroupAIID)
                aiNames.put(gKey, ai.getOrderID());

            if (CCRA == uGroupAIID){
                if (InquiryConstants.DOCUMENT_LANGUAGE.LOCAL.equals(documentLanguage))
                    aiNames.put(gKey, ai.getNameLO());
                else
                    aiNames.put(gKey, ai.getNameEN());
            }

            for (Integer groupAIId : groupAIIds){
                if (groupID.equals(groupAIId)){//https://stackoverflow.com/questions/1514910/how-can-i-properly-compare-two-integers-in-java
                    if (InquiryConstants.DOCUMENT_LANGUAGE.LOCAL.equals(documentLanguage))
                        aiNames.put(gKey, ai.getNameLO());
                    else
                        aiNames.put(gKey, ai.getNameEN());
                }
            }

            if (null == aiNames.get(gKey))
                aiNames.put(gKey, ai.getOrderID());
        }

        return (String) aiNames.get(gKey);
    }
    private static class AIOrder
    {
        private Date revocEffectDate;
        private Integer groupID;
        private String AICode;
        private String HKBRCNO;
        private String HKCINO;
        private String accMngerCode;
        private String customerNO;
        private String locBranchID;
        private String nameEN;
        private String nameLO;
        private String orderID;
        private String otherRegNO;
        private String placeOfReg;
        public AIOrder(Integer groupID, String AICode, String orderID,
            String nameEN, String nameLO, String locBranchID, String accMngerCode,
            String customerNO, String HKBRCNO, String HKCINO, String otherRegNO,
            String placeOfReg, Date revocEffectDate) {
            this.groupID = groupID;
            this.AICode = AICode;
            this.orderID = orderID;
            this.nameEN = nameEN;
            this.nameLO = nameLO;
            this.locBranchID = locBranchID;
            this.accMngerCode = accMngerCode;
            this.customerNO = customerNO;
            this.HKBRCNO = HKBRCNO;
            this.HKCINO = HKCINO;
            this.otherRegNO = otherRegNO;
            this.placeOfReg = placeOfReg;
            this.revocEffectDate = revocEffectDate;
        }
        public String getAICode(){
            return AICode;
        }
        public String getAccMngerCode(){
            return accMngerCode;
        }
        public String getCustomerNO(){
            return customerNO;
        }
        public Integer getGroupID(){
            return groupID;
        }
        public String getHKBRCNO(){
            return HKBRCNO;
        }
        public String getHKCINO(){
            return HKCINO;
        }
        public String getLocBranchID(){
            return locBranchID;
        }
        public String getNameEN(){
            return nameEN;
        }
        public String getNameLO(){
            return nameLO;
        }
        public String getOrderID(){
            return orderID;
        }
        public String getOtherRegNO(){
            return otherRegNO;
        }
        public String getPlaceOfReg(){
            return placeOfReg;
        }
        public Date getRevocEffectDate(){
            return revocEffectDate;
        }
        public void setAICode(String code){
            AICode = code;
        }
        public void setAccMngerCode(String accMngerCode){
            this.accMngerCode = accMngerCode;
        }
        public void setCustomerNO(String customerNO){
            this.customerNO = customerNO;
        }
        public void setGroupID(Integer groupID){
            this.groupID = groupID;
        }
        public void setHKBRCNO(String hkbrcno){
            HKBRCNO = hkbrcno;
        }
        public void setHKCINO(String hkcino){
            HKCINO = hkcino;
        }
        public void setLocBranchID(String locBranchID){
            this.locBranchID = locBranchID;
        }
        public void setNameEN(String nameEN){
            this.nameEN = nameEN;
        }
        public void setNameLO(String nameLO){
            this.nameLO = nameLO;
        }
        public void setOrderID(String orderID){
            this.orderID = orderID;
        }
        public void setOtherRegNO(String otherRegNO){
            this.otherRegNO = otherRegNO;
        }
        public void setPlaceOfReg(String placeOfReg){
            this.placeOfReg = placeOfReg;
        }
        public void setRevocEffectDate(Date revocEffectDate){
            this.revocEffectDate = revocEffectDate;
        }
    }

}
