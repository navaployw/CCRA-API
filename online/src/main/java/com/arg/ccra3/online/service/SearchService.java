
package com.arg.ccra3.online.service;

import ch.qos.logback.classic.Logger;
import com.arg.ccra3.dao.SearchDAO;
import com.arg.ccra3.model.inquiry.data.SearchByNameData;
import com.arg.ccra3.model.User;
import static com.arg.ccra3.common.InquiryConstants.*;
import static com.arg.ccra3.dao.util.AlertLogicManager.updateAmountResult;
import com.arg.ccra3.model.inquiry.data.SearchByIdData;
import com.arg.cb2.inquiry.ReportDataUtility;
import com.arg.cb2.inquiry.bulk.BulkConstants;
import com.arg.ccra3.online.form.NameSearchForm;
import com.arg.util.GenericException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import com.arg.ccra3.online.form.IDSearchForm;
import com.arg.ccra3.online.rest.CcraapiController;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

//@PropertySource(value = "classpath:/com/arg/cb2/inquiry/report-statement.properties")
/*@PropertySources({
    @PropertySource(value = "classpath:/document.properties"),
    @PropertySource(value ="file:/conf/document.properties", ignoreResourceNotFound=true)
})*/
@Service
public class SearchService {

    private final int NUMERPERPAGE = 25;
    private final Logger logger = (Logger) LoggerFactory.getLogger(SearchService.class);
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<SearchByIdData> idSearch(
        final User user,
        IDSearchForm uform
    ){
        
        List<SearchByIdData> datas = null;

        String HKBRC = null;
        String HKCI = null;
        String otherPlace = null;
        String placeregist = null;

        String idtype = uform.getIdType();
        String idnum = uform.getSearchIdNumber();

        if (null == idnum)
        {
            logger.info("idnum null");
        }

        SearchDAO search = new SearchDAO(jdbcTemplate);
        if (BulkConstants.HKBRC.equalsIgnoreCase(idtype))
        {
            HKBRC = idnum;
            datas = search.searchByHKBRC(idnum.trim());
        }
        else if (BulkConstants.HKCI.equalsIgnoreCase(idtype))
        {
            HKCI = idnum;
            datas = search.searchByHKCI(idnum.trim());
        }
        else if (BulkConstants.OTHER.equalsIgnoreCase(idtype))
        {
            placeregist = uform.getPlaceRegist();
            if (placeregist != null)
                otherPlace = idnum.trim();
            else {
                logger.info("placeregist null");
            }

            int recperpge = null == uform.getRecordPerPage() ? NUMERPERPAGE : Integer.parseInt(uform.getRecordPerPage());

            datas = search.searchByOtherReg(idnum, placeregist, recperpge);
        }
        else
        {
            logger.info("BulkConstants else");
        }

        String backorder = null;

        if (!"true".equalsIgnoreCase(backorder))
        {
            /*final Expense expenseData = new Expense();

            Integer _uID = user.getUID();
            Integer _groupID = user.getGroupID();
            Integer _groupAIID = user.getAIID();
            Integer _session = user.getUniqueKey();


            expenseData.setTimeNow();
            expenseData.setUID(_uID);
            expenseData.setGroupAIID(_groupAIID);
            expenseData.setGroupID(_groupID);
            expenseData.setDisabled(false);
            expenseData.setProductUsage(new Integer(OBJECT_TYPE.SEARCH));
            expenseData.setUpdatedBy(_uID);
            expenseData.setCreatedBy(_uID);
            expenseData.setSessionID(_session);

            //create transaction id
            final Transaction tran = new Transaction();

            tran.setUpdatedBy(_uID);
            tran.setCreatedBy(_uID);
            tran.setupDate();
            tran.setGroupAIID(_groupAIID);
            tran.setGroupID(_groupID);
            tran.setObjectID(OBJECT_TYPE.SEARCH);
            tran.setSessionID(_session);
            tran.setuID(_uID);
            tran.setAmountResult(datas.size());
            tran.setChannel(REQUESTTYPE.ONLINE);

            final MailProductDeliver mail = new MailProductDeliver();

            mail.setExpenseData(expenseData);
            mail.setAIRefCode1(uform.getAiRefCode1());
            mail.setAIRefCode2(uform.getAiRefCode2());
            mail.setAIRefCode3(uform.getAiRefCode3());
            mail.setReasonCode(Integer.parseInt(uform.getReasonCode()));
            mail.setObjectID(OBJECT_TYPE.SEARCH);
            mail.setHKBRCNO(HKBRC);
            mail.setHKCINO(HKCI);
            mail.setOtherRegNO(otherPlace);
            mail.setPlaceOfReg(placeregist);
            mail.setRequestType(REQUESTTYPE.ONLINE);
            mail.setPreferedLanguage(user.getPreferLanguage().getLabel());
            mail.setuID(_uID);
            mail.setGroupID(_groupID);
            mail.setGroupAIID(_groupAIID);

            Long transactionId = search.createTransactionDeliver(tran, mail);
            uform.setTransactionId(transactionId.toString());

            //request.setAttribute("transactionId", transactionId);*/
        }

        return null;
    }

    public List<SearchByNameData> searchName(
            final User user,
            NameSearchForm uForm,
            Map<String, Object> request
    ) throws GenericException, Exception{

        String expense = uForm.getExpenseID();
        if (expense == null || expense.trim().length() == 0)
            uForm.setExpenseID(request.get("expenseID").toString());
        /*else
            throwException(request, "WBS-R00017");*/

        ArrayList<SearchByNameData> dataNames = null;

        SearchDAO search = new SearchDAO(jdbcTemplate);
        String langType = uForm.getLangType();
        String name = uForm.getCriteria();
        int recordPerPage = null == uForm.getRecordPerPage() ? NUMERPERPAGE : Integer.parseInt(uForm.getRecordPerPage());

        if (DOCUMENT_LANGUAGE.ENGLISH.equals(langType))//interface constant lol!!
            dataNames = (ArrayList<SearchByNameData>) search.searchByEnglishName(name, recordPerPage);
        else if (DOCUMENT_LANGUAGE.LOCAL.equals(langType))
            dataNames = (ArrayList<SearchByNameData>) search.searchByLocalName(name, recordPerPage);
        /*/else
            throwException(request, "WBS-R00024");*/

        for(SearchByNameData it : dataNames)
            it.trimStringData();

        int totalResult = dataNames.size();

        String updateTransaction = request.get("updateTransaction").toString();
        if (updateTransaction != null
            && updateTransaction.trim().equalsIgnoreCase("true"))
        {
            if (uForm.getTransactionId() == null
                || uForm.getTransactionId().trim().length() == 0)
            {
                logger.info("uForm.getTransactionId() null");
            }

            if (null == name)
            {
                logger.info("name null");
            }
            int tranId = Integer.parseInt(uForm.getTransactionId());
            /*search.updateSearchData(
                    tranId,
                    uForm.getAiRefCode1(), uForm.getAiRefCode2(),
                    uForm.getAiRefCode3(), Integer.parseInt(uForm.getReasonCode()),
                    name);*/

            updateAmountResult(jdbcTemplate, String.valueOf(totalResult), String.valueOf(tranId));
        }
        else
        {
            Long exp = ReportDataUtility.getExpenseID(expense);
            String backorder = request.get("backorder").toString();
            if (!"true".equalsIgnoreCase(backorder))
            {
               /* Integer _uID = user.getUID();
                Integer _groupID = user.getGroupID();
                Integer _groupAIID = user.getAIID();

                final Transaction TRAN = new Transaction();
                TRAN.setExpenseID(exp);
                TRAN.setuID(_uID);
                TRAN.setGroupAIID(_groupAIID);
                TRAN.setGroupID(_groupID);
                TRAN.setObjectID(OBJECT_TYPE.SEARCH);
                TRAN.setSessionID(user.getUniqueKey());
                TRAN.setUpdatedBy(_uID);
                TRAN.setCreatedBy(_uID);
                TRAN.setAmountResult(totalResult);
                TRAN.setChannel(REQUESTTYPE.ONLINE);

                final MailProductDeliver MAIL = new MailProductDeliver();
                MAIL.setPreferedLanguage(user.getPreferLanguage().getLabel());
                MAIL.setuID(_uID);
                MAIL.setGroupID(_groupID);
                MAIL.setGroupAIID(_groupAIID);
                MAIL.setExpenseID(exp);
                MAIL.setAIRefCode1(uForm.getAiRefCode1());
                MAIL.setAIRefCode2(uForm.getAiRefCode2());
                MAIL.setAIRefCode3(uForm.getAiRefCode3());
                MAIL.setNameSearch(uForm.getCriteria());
                MAIL.setReasonCode(Integer.parseInt(uForm.getReasonCode()));
                MAIL.setRequestType(REQUESTTYPE.ONLINE);
                MAIL.setObjectID(OBJECT_TYPE.SEARCH);

                Long transactionId = search.createTransactionDeliver(TRAN, MAIL);

                uForm.setTransactionId(String.valueOf(transactionId));*/
            }
        }

        request.put("searchByNameData", dataNames);
        request.put("recordMatch", String.valueOf(totalResult));

        return null;
    }
    

}

