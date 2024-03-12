
package com.arg.ccra3.online.service;

import ch.qos.logback.classic.Logger;
import com.arg.ccra3.dao.SearchDAO;
import com.arg.ccra3.models.inquiry.data.SearchByNameData;
import com.arg.ccra3.models.User;
import static com.arg.ccra3.common.InquiryConstants.*;
import static com.arg.ccra3.dao.util.AlertLogicManager.updateAmountResult;
import com.arg.ccra3.models.inquiry.data.SearchByIdData;
import com.arg.cb2.inquiry.ReportDataUtility;
import com.arg.cb2.inquiry.bulk.BulkConstants;
import com.arg.ccra3.online.form.NameSearchForm;
import com.arg.util.GenericException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import com.arg.ccra3.online.form.IDSearchForm;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
@Service
public class SearchService {

    private final int NUMERPERPAGE = 25;
    private final Logger logger = (Logger) LoggerFactory.getLogger(SearchService.class);
    @Autowired
    private JdbcTemplate jdbcTemplate;


    public List<SearchByNameData> searchName(
            NameSearchForm uForm,
            Map<String, Object> request
    ) throws GenericException{

        String expense = uForm.getExpenseID();
        if (expense == null || expense.trim().length() == 0)
            uForm.setExpenseID(request.get("expenseID").toString());

        ArrayList<SearchByNameData> dataNames = null;

        SearchDAO search = new SearchDAO(jdbcTemplate);
        String langType = uForm.getLangType();
        String name = uForm.getCriteria();
        int recordPerPage = null == uForm.getRecordPerPage() ? NUMERPERPAGE : Integer.parseInt(uForm.getRecordPerPage());

        if (DOCUMENT_LANGUAGE.ENGLISH.equals(langType))//interface constant lol!!
            dataNames = (ArrayList<SearchByNameData>) search.searchByEnglishName(name, recordPerPage);
        else if (DOCUMENT_LANGUAGE.LOCAL.equals(langType))
            dataNames = (ArrayList<SearchByNameData>) search.searchByLocalName(name, recordPerPage);

        for(SearchByNameData it : dataNames)
            it.trimStringData();

        int totalResult = 0;

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

            updateAmountResult(jdbcTemplate, String.valueOf(totalResult), String.valueOf(tranId));
        }
        

        request.put("searchByNameData", dataNames);
        request.put("recordMatch", String.valueOf(totalResult));

        return new ArrayList<>();
    }
    

}

