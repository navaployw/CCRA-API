package com.arg.ccra3.online.service;


//import com.arg.ccra3.dao.util.JdbcAPIDatasource;
import com.arg.ccra3.model.CdiToken;
import com.arg.ccra3.dao.repo.CdiTokenRepository;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class CdiTokenService {
    @Autowired
    private CdiTokenRepository cdiTokenRepo; 
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private final Logger logger = (Logger) LoggerFactory.getLogger(CdiTokenService.class);
    private String loggerText = "";
    public CdiToken saveCdiToken(CdiToken data){
//        String sql = "insert into api_token_cdi ( aid, aicode, cdi_token, create_time, expire_time, jti, payload, tran_time, uid) values ( ?, ?, ?, ?, ?, ?, ?, ?, ?)";
//         Object[] params = new Object[]{data.getaId(),data.getAiCode(),data.getCdiToken(),data.getCreateTime(),data.getExpireTime(),data.getJti(),data.getPayload(),data.getTranTime(),data.getuId()};
//        JdbcAPIDatasource.jdbcTemplateAPI.update(sql,params);
        loggerText = String.format("CdiToken:service: %s", data);
        logger.info(loggerText);
        return cdiTokenRepo.save(data);
    }
    
    public List<CdiToken> checkCdiTokenUsed(String cdiToken){
        return cdiTokenRepo.findByCdiToken(cdiToken);
    }
    
    public List<CdiToken> findCdiToken(String cdiToken,Long uId){
        String sql = "select top(1) * from API_TOKEN_CDI where UID= ? and CDI_TOKEN= ? order by TOKENCDIID desc";
        Object[] params = new Object[]{uId,cdiToken};
        List<CdiToken> results = jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(CdiToken.class), params);
        return results;
    }
}
