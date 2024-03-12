package com.arg.ccra3.online.config;

import com.arg.ccra3.dao.AIOrderDAO;
import com.arg.ccra3.dao.ExpenseAPIDAO;
import com.arg.ccra3.dao.HTMLReportCreatorDAO;
import com.arg.ccra3.dao.MalProductDeliverAPIDAO;
import com.arg.ccra3.dao.ReportCreatorDAO;
import com.arg.ccra3.dao.SearchDAO;
import com.arg.ccra3.dao.TransactionAPIDAO;
import com.arg.ccra3.dao.UserDAO;
import com.arg.ccra3.dao.repo.ExpenseAPIRepo;
import com.arg.ccra3.dao.repo.MalProductDeliverAPIRepo;
import com.arg.ccra3.dao.repo.MalProductDeliverDataAPIRepo;
import com.arg.ccra3.dao.repo.TransactionAPIRepo;
import com.arg.ccra3.dao.util.ValidateReportRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service//spring-boot config are in these annotation scopes
@Configuration
public class ReportServiceDaoConfig {
    @Bean
    public ReportCreatorDAO getReportCreatorDAO(JdbcTemplate jdbc, MalProductDeliverDataAPIRepo malDataRepo){
        return new ReportCreatorDAO(jdbc, malDataRepo);
    }
    @Bean
    public HTMLReportCreatorDAO getHTMLReportCreatorDAO(JdbcTemplate jdbc){
        return new HTMLReportCreatorDAO(jdbc);
    }
    @Bean
    public ExpenseAPIDAO getExpenseAPIDAO(JdbcTemplate jdbc, ExpenseAPIRepo expenseRepo){
        return new ExpenseAPIDAO(jdbc, expenseRepo);
    }
    @Bean
    public SearchDAO getSearchDAO(JdbcTemplate jdbc){
        return new SearchDAO(jdbc);
    }
    @Bean
    public TransactionAPIDAO getTransactionAPIDAO(TransactionAPIRepo tranRepo){
        return new TransactionAPIDAO(tranRepo);
    }
    @Bean
    public UserDAO getUserDAO(JdbcTemplate jdbc){
        return new UserDAO(jdbc);
    }
    @Bean
    public MalProductDeliverAPIDAO getMalProductDeliverAPIDAO(JdbcTemplate jdbc, MalProductDeliverAPIRepo malRepo){
        return new MalProductDeliverAPIDAO(malRepo);
    }
    @Bean
    public ValidateReportRule getValidateReportRule(JdbcTemplate jdbc){
        return new ValidateReportRule(jdbc);
    }
    @Bean
    public AIOrderDAO getAIOrderDAO(JdbcTemplate jdbc){
        return new AIOrderDAO(jdbc);
    }
}
