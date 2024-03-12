/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arg.ccra3.online.service;

import ch.qos.logback.classic.Logger;
import jakarta.persistence.EntityManagerFactory;

import static com.arg.ccra3.common.InquiryConstants.*;
import com.arg.ccra3.dao.BeforeGetReportDAO;
import com.arg.ccra3.models.MessageError;
import com.arg.ccra3.models.TrnJson;
import com.arg.ccra3.models.api.TokenAPI;
import com.arg.ccra3.models.security.ChkThreshold;
import com.arg.ccra3.models.security.Company;
import com.arg.ccra3.models.security.SpmTransaction;
import com.arg.ccra3.models.security.ViewApiUser;
import com.arg.ccra3.online.form.ReportAPIRequest;
import java.math.BigDecimal;
import java.util.List;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class BeforeGetReport {

    private static EntityManagerFactory factory;
    private final Logger logger = (Logger) LoggerFactory.getLogger(BeforeGetReport.class);
    String loggerText = "";
    private MessageError msgErr = new MessageError();
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public MessageError process(ViewApiUser request, ReportAPIRequest requestJson, String accessToken) {
        logger.info(">>start process");
        msgErr = new MessageError();
        BigDecimal uid = new BigDecimal(request.getuID());//2965
        BigDecimal companyId = new BigDecimal(requestJson.getCompanyID());//686565722 
        BigDecimal groupAiId = new BigDecimal(request.getGroupAIID());//1 
        BigDecimal groupId = new BigDecimal(request.getGroupID());//1 
        loggerText = String.format(">>uid:: %s", uid);
        logger.info(loggerText);
        loggerText = String.format(">>companyId:: %s", companyId);
        logger.info(loggerText);
        loggerText = String.format(">>groupAiId:: %s", groupAiId);
        logger.info(loggerText);
        loggerText = String.format(">>groupId:: %s", groupId);
        logger.info(loggerText);
        String dunsNo = companyId.toString();
        String reasonCodeAll = "1,2,3,4,5";
        String aiRefCode1 = requestJson.getAiRefCode1();
        String aiRefCode2 = requestJson.getAiRefCode2();
        String aiRefCode3 = requestJson.getAiRefCode3();
        BeforeGetReportDAO beforeGetReportDAO = new BeforeGetReportDAO(jdbcTemplate);
        String reOrder7Day = requestJson.getReorderIn7Days();
        String token = accessToken;
        List<ChkThreshold> chkThreshold = beforeGetReportDAO.checkThreshold(uid);
        List<Company> company = beforeGetReportDAO.getCompanyByDunsNo(companyId);
        List<TokenAPI> tokenApi = beforeGetReportDAO.getTokenIdFromCCRAToken(token);
        Integer tokenId = tokenApi.get(0).getTokenId();
        Integer objectId = 0;
        if (reasonCodeAll.contains(requestJson.getReasonCode())) {
            Integer reasonCode = Integer.parseInt(requestJson.getReasonCode());

            if (!requestJson.getReportType().equals("1") && !requestJson.getReportType().equals("2")) {
                objectId = OBJECT_TYPE.SEARCH_API;
                logger.info("Status Code::403");
                logger.info("Error Code::00051");
                msgErr = beforeGetReportDAO.getErrorMessageByErrorCodeNative("00051").get(0);
                loggerText = String.format("msgErr>>> %s", msgErr);
                logger.info(loggerText);
                loggerText = String.format("ErrorCode>>> %s", msgErr.getErrorCode());
                logger.info(loggerText);
                loggerText = String.format("ErrorMessage>>> %s", msgErr.getErrorMessage());
                logger.info(loggerText);
                SpmTransaction spmTransaction = beforeGetReportDAO.insertApiSpmTransaction(tokenId, uid, groupAiId, groupId, 1).get(0);
                beforeGetReportDAO.insertApiProductDeliver(spmTransaction.getTransactionId(), dunsNo, reasonCode, aiRefCode1, aiRefCode2, aiRefCode3, objectId, uid, groupAiId, groupId);
                loggerText = String.format("spmTransaction.getTransactionId():: %s", spmTransaction.getTransactionId());
                logger.info(loggerText);
                loggerText = String.format("tokenApi.get(0).getTokenId():: %s", tokenApi.get(0).getTokenId());
                logger.info(loggerText);
                msgErr.setTransactionId(spmTransaction.getTransactionId());
                msgErr.setTokenId(tokenApi.get(0).getTokenId());
            } else {
                Integer reportType = Integer.parseInt(requestJson.getReportType());
                objectId = switch (reportType) {
                    case 1 -> OBJECT_TYPE.BASIC_REPORT_API;
                    case 2 -> OBJECT_TYPE.CHINESE_REPORT_API;
                    default -> OBJECT_TYPE.BASIC_REPORT_API;
                };

                requestJson.setTokenId(tokenId.longValue());
                if (chkThreshold.get(0).getChk() != null && chkThreshold.get(0).getChk()) {
                    logger.info("Status Code::429");
                    logger.info("Error Code::00050");

                    msgErr = beforeGetReportDAO.getErrorMessageByErrorCodeNative("00050").get(0);
                    loggerText = String.format("msgErr>>> %s", msgErr);
                    logger.info(loggerText);
                    loggerText = String.format("ErrorCode>>> %s", msgErr.getErrorCode());
                    logger.info(loggerText);
                    loggerText = String.format("ErrorMessage>>> %s", msgErr.getErrorMessage());
                    logger.info(loggerText);

                } else {
                    logger.info("Status Code::200");
                    logger.info("SUCCESS ORDER REPORT");
                    if (company.isEmpty()) {
                        logger.info("Status Code::403");
                        logger.info("Error Code::00012");

                        msgErr = beforeGetReportDAO.getErrorMessageByErrorCodeNative("00012").get(0);
                        loggerText = String.format("msgErr>>> %s", msgErr);
                        logger.info(loggerText);
                        loggerText = String.format("ErrorCode>>> %s", msgErr.getErrorCode());
                        logger.info(loggerText);
                        loggerText = String.format("ErrorMessage>>> %s", msgErr.getErrorMessage());
                        logger.info(loggerText);

                        SpmTransaction spmTransaction = beforeGetReportDAO.insertApiSpmTransaction(tokenId, uid, groupAiId, groupId, 0).get(0);
                        beforeGetReportDAO.insertApiProductDeliver(spmTransaction.getTransactionId(), dunsNo, reasonCode, aiRefCode1, aiRefCode2, aiRefCode3, objectId, uid, groupAiId, groupId);
                        loggerText = String.format("spmTransaction.getTransactionId():: %s", spmTransaction.getTransactionId());
                        logger.info(loggerText);
                        loggerText = String.format("tokenApi.get(0).getTokenId():: %s", tokenApi.get(0).getTokenId());
                        logger.info(loggerText);
                        msgErr.setTransactionId(spmTransaction.getTransactionId());
                        msgErr.setTokenId(tokenApi.get(0).getTokenId());
                    } else {
                        List<ChkThreshold> order7day = beforeGetReportDAO.checkOrder7Day(new BigDecimal(company.get(0).getCbuId()), groupAiId, objectId);
                        List<ChkThreshold> orderChinese = beforeGetReportDAO.checkOrderChinese(new BigDecimal(company.get(0).getCbuId()), groupAiId, objectId);
                        logger.info("else::companynotempty");
                        loggerText = String.format("reOrder7Day:: %s", reOrder7Day);
                        logger.info(loggerText);
                        if (reOrder7Day == null || !reOrder7Day.trim().equals("Y")) {
                            logger.info("!reOrder7Day.trim().equals(Y)");
                            loggerText = String.format("order7day.get(0).getChk():: %s", order7day.get(0).getChk());
                            logger.info(loggerText);
                            if (order7day.get(0).getChk() != null && order7day.get(0).getChk()) {
                                logger.info("Status Code::403");
                                logger.info("Error Code::00004");

                                msgErr = beforeGetReportDAO.getErrorMessageByErrorCodeNative("00004").get(0);
                                loggerText = String.format("msgErr>>> %s", msgErr);
                                logger.info(loggerText);
                                loggerText = String.format("ErrorCode>>> %s", msgErr.getErrorCode());
                                logger.info(loggerText);
                                loggerText = String.format("ErrorMessage>>> %s", msgErr.getErrorMessage());
                                logger.info(loggerText);
                                SpmTransaction spmTransaction = beforeGetReportDAO.insertApiSpmTransaction(tokenId, uid, groupAiId, groupId, 1).get(0);
                                beforeGetReportDAO.insertApiProductDeliver(spmTransaction.getTransactionId(), dunsNo, reasonCode, aiRefCode1, aiRefCode2, aiRefCode3, objectId, uid, groupAiId, groupId);
                                loggerText = String.format("spmTransaction.getTransactionId():: %s", spmTransaction.getTransactionId());
                                logger.info(loggerText);
                                loggerText = String.format("tokenApi.get(0).getTokenId():: %s", tokenApi.get(0).getTokenId());
                                logger.info(loggerText);
                                msgErr.setTransactionId(spmTransaction.getTransactionId());
                                msgErr.setTokenId(tokenApi.get(0).getTokenId());
                            } else if (order7day.get(0).getChk() == null || !order7day.get(0).getChk()) {
                                logger.info("else:!order7day.get(0).getChk() || order7day.get(0).getChk()==null");
                                if (reportType == 2) {
                                    logger.info("reportType ==2");
                                    if (orderChinese.get(0).getChk() == null || !orderChinese.get(0).getChk()) {
                                        loggerText = String.format("orderChinese.get(0).getChk(): %s", orderChinese.get(0).getChk());
                                        logger.info(loggerText);
                                        logger.info("Status Code::403");
                                        logger.info("Error Code::00004");
                                        msgErr = beforeGetReportDAO.getErrorMessageByErrorCodeNative("00004").get(0);
                                        loggerText = String.format("msgErr>>> %s", msgErr);
                                        logger.info(loggerText);
                                        loggerText = String.format("ErrorCode>>> %s", msgErr.getErrorCode());
                                        logger.info(loggerText);
                                        loggerText = String.format("ErrorMessage>>> %s", msgErr.getErrorMessage());
                                        logger.info(loggerText);
                                        SpmTransaction spmTransaction = beforeGetReportDAO.insertApiSpmTransaction(tokenId, uid, groupAiId, groupId, 1).get(0);
                                        beforeGetReportDAO.insertApiProductDeliver(spmTransaction.getTransactionId(), dunsNo, reasonCode, aiRefCode1, aiRefCode2, aiRefCode3, objectId, uid, groupAiId, groupId);
                                        loggerText = String.format("spmTransaction.getTransactionId():: %s", spmTransaction.getTransactionId());
                                        logger.info(loggerText);
                                        loggerText = String.format("tokenApi.get(0).getTokenId():: %s", tokenApi.get(0).getTokenId());
                                        logger.info(loggerText);
                                        msgErr.setTransactionId(spmTransaction.getTransactionId());
                                        msgErr.setTokenId(tokenApi.get(0).getTokenId());
                                    } else if (orderChinese.get(0).getChk() != null && orderChinese.get(0).getChk()) {
                                        logger.info("else:orderChinese.get(0).getChk():" + orderChinese.get(0).getChk());
                                        loggerText = String.format("tokenApi.get(0).getTokenId():: %s", tokenApi.get(0).getTokenId());
                                        logger.info(loggerText);
                                        SpmTransaction spmTransaction = beforeGetReportDAO.insertApiSpmTransaction(tokenId, uid, groupAiId, groupId, 1).get(0);
                                        logger.info("spmTransaction.getTransactionId()::" + spmTransaction.getTransactionId());
                                        logger.info("tokenApi.get(0).getTokenId()::" + tokenApi.get(0).getTokenId());
                                        msgErr.setTransactionId(spmTransaction.getTransactionId());
                                        msgErr.setTokenId(tokenApi.get(0).getTokenId());
                                    }
                                } else if (reportType == 1) {
                                    logger.info("else:else:orderChinese.get(0).getChk():" + orderChinese.get(0).getChk());
                                    SpmTransaction spmTransaction = beforeGetReportDAO.insertApiSpmTransaction(tokenId, uid, groupAiId, groupId, 1).get(0);
                                    logger.info("spmTransaction.getTransactionId()::" + spmTransaction.getTransactionId());
                                    logger.info("tokenApi.get(0).getTokenId()::" + tokenApi.get(0).getTokenId());
                                    msgErr.setTransactionId(spmTransaction.getTransactionId());
                                    msgErr.setTokenId(tokenApi.get(0).getTokenId());
                                } else {
                                    logger.info("Status Code::403");
                                    logger.info("Error Code::00051");
                                    objectId = OBJECT_TYPE.SEARCH_API;
                                    msgErr = beforeGetReportDAO.getErrorMessageByErrorCodeNative("00051").get(0);
                                    loggerText = String.format("msgErr>>> %s", msgErr);
                                    logger.info(loggerText);
                                    loggerText = String.format("ErrorCode>>> %s", msgErr.getErrorCode());
                                    logger.info(loggerText);
                                    loggerText = String.format("ErrorMessage>>> %s", msgErr.getErrorMessage());
                                    logger.info(loggerText);
                                    SpmTransaction spmTransaction = beforeGetReportDAO.insertApiSpmTransaction(tokenId, uid, groupAiId, groupId, 1).get(0);
                                    beforeGetReportDAO.insertApiProductDeliver(spmTransaction.getTransactionId(), dunsNo, reasonCode, aiRefCode1, aiRefCode2, aiRefCode3, objectId, uid, groupAiId, groupId);
                                    logger.info("spmTransaction.getTransactionId()::" + spmTransaction.getTransactionId());
                                    logger.info("tokenApi.get(0).getTokenId()::" + tokenApi.get(0).getTokenId());
                                    msgErr.setTransactionId(spmTransaction.getTransactionId());
                                    msgErr.setTokenId(tokenApi.get(0).getTokenId());
                                }
                            }

                        } else if (reOrder7Day.trim().equals("Y")) {
                            if (reportType == 2) {
                                logger.info("reportType ==2");
                                if (orderChinese.get(0).getChk() == null || !orderChinese.get(0).getChk()) {
                                    logger.info("orderChinese.get(0).getChk():" + orderChinese.get(0).getChk());
                                    logger.info("Status Code::403");
                                    logger.info("Error Code::00004");
                                    msgErr = beforeGetReportDAO.getErrorMessageByErrorCodeNative("00004").get(0);
                                    loggerText = String.format("msgErr>>> %s", msgErr);
                                    logger.info(loggerText);
                                    loggerText = String.format("ErrorCode>>> %s", msgErr.getErrorCode());
                                    logger.info(loggerText);
                                    loggerText = String.format("ErrorMessage>>> %s", msgErr.getErrorMessage());
                                    logger.info(loggerText);
                                    SpmTransaction spmTransaction = beforeGetReportDAO.insertApiSpmTransaction(tokenId, uid, groupAiId, groupId, 1).get(0);
                                    beforeGetReportDAO.insertApiProductDeliver(spmTransaction.getTransactionId(), dunsNo, reasonCode, aiRefCode1, aiRefCode2, aiRefCode3, objectId, uid, groupAiId, groupId);
                                    logger.info("spmTransaction.getTransactionId()::" + spmTransaction.getTransactionId());
                                    logger.info("tokenApi.get(0).getTokenId()::" + tokenApi.get(0).getTokenId());
                                    msgErr.setTransactionId(spmTransaction.getTransactionId());
                                    msgErr.setTokenId(tokenApi.get(0).getTokenId());
                                } else if (orderChinese.get(0).getChk() != null && orderChinese.get(0).getChk()) {
                                    logger.info("else:orderChinese.get(0).getChk():" + orderChinese.get(0).getChk());
                                    SpmTransaction spmTransaction = beforeGetReportDAO.insertApiSpmTransaction(tokenId, uid, groupAiId, groupId, 1).get(0);
                                    logger.info("spmTransaction.getTransactionId()::" + spmTransaction.getTransactionId());
                                    logger.info("tokenApi.get(0).getTokenId()::" + tokenApi.get(0).getTokenId());
                                    msgErr.setTransactionId(spmTransaction.getTransactionId());
                                    msgErr.setTokenId(tokenApi.get(0).getTokenId());
                                }
                            } else if (reportType == 1) {
                                logger.info("else:else:orderChinese.get(0).getChk():" + orderChinese.get(0).getChk());
                                SpmTransaction spmTransaction = beforeGetReportDAO.insertApiSpmTransaction(tokenId, uid, groupAiId, groupId, 1).get(0);
                                logger.info("spmTransaction.getTransactionId()::" + spmTransaction.getTransactionId());
                                logger.info("tokenApi.get(0).getTokenId()::" + tokenApi.get(0).getTokenId());
                                msgErr.setTransactionId(spmTransaction.getTransactionId());
                                msgErr.setTokenId(tokenApi.get(0).getTokenId());
                            } else {
                                objectId = OBJECT_TYPE.SEARCH_API;
                                logger.info("Status Code::403");
                                logger.info("Error Code::00051");
                                msgErr = beforeGetReportDAO.getErrorMessageByErrorCodeNative("00051").get(0);
                                loggerText = String.format("msgErr>>> %s", msgErr);
                                logger.info(loggerText);
                                loggerText = String.format("ErrorCode>>> %s", msgErr.getErrorCode());
                                logger.info(loggerText);
                                loggerText = String.format("ErrorMessage>>> %s", msgErr.getErrorMessage());
                                logger.info(loggerText);
                                SpmTransaction spmTransaction = beforeGetReportDAO.insertApiSpmTransaction(tokenId, uid, groupAiId, groupId, 1).get(0);
                                beforeGetReportDAO.insertApiProductDeliver(spmTransaction.getTransactionId(), dunsNo, reasonCode, aiRefCode1, aiRefCode2, aiRefCode3, objectId, uid, groupAiId, groupId);
                                logger.info("spmTransaction.getTransactionId()::" + spmTransaction.getTransactionId());
                                logger.info("tokenApi.get(0).getTokenId()::" + tokenApi.get(0).getTokenId());
                                msgErr.setTransactionId(spmTransaction.getTransactionId());
                                msgErr.setTokenId(tokenApi.get(0).getTokenId());
                            }
                        }
                    }
                }
            }
        } else {
            objectId = OBJECT_TYPE.SEARCH_API;
            logger.info("Status Code::403");
            logger.info("Error Code::00051");
            msgErr = beforeGetReportDAO.getErrorMessageByErrorCodeNative("00051").get(0);
            loggerText = String.format("msgErr>>> %s", msgErr);
            logger.info(loggerText);
            loggerText = String.format("ErrorCode>>> %s", msgErr.getErrorCode());
            logger.info(loggerText);
            loggerText = String.format("ErrorMessage>>> %s", msgErr.getErrorMessage());
            logger.info(loggerText);
            SpmTransaction spmTransaction = beforeGetReportDAO.insertApiSpmTransaction(tokenId, uid, groupAiId, groupId, 1).get(0);
            beforeGetReportDAO.insertApiProductDeliver(spmTransaction.getTransactionId(), dunsNo, null, aiRefCode1, aiRefCode2, aiRefCode3, objectId, uid, groupAiId, groupId);
            logger.info("spmTransaction.getTransactionId()::" + spmTransaction.getTransactionId());
            logger.info("tokenApi.get(0).getTokenId()::" + tokenApi.get(0).getTokenId());
            msgErr.setTransactionId(spmTransaction.getTransactionId());
            msgErr.setTokenId(tokenApi.get(0).getTokenId());
        }
        return msgErr;
    }

    public MessageError processHtml(Long tokenCdiId, String userId, List<TokenAPI> tokenApi) {
        logger.info(">>start process html");
        msgErr = new MessageError();
        loggerText = String.format(">>userId:: %s", userId);
        logger.info(loggerText);
        loggerText = String.format(">>tokenCdiId:: %s", tokenCdiId);
        logger.info(loggerText);
        BeforeGetReportDAO beforeGetReportDAO = new BeforeGetReportDAO(jdbcTemplate);
        loggerText = String.format("tokenApi.get(0).getuID():: %s", tokenApi.get(0).getuID());
        logger.info(loggerText);
        loggerText = String.format("tokenApi.get(0).getaID():: %s", tokenApi.get(0).getaID());
        logger.info(loggerText);
        String statusUpdate = beforeGetReportDAO.updateTresholdApiUser(tokenApi.get(0).getaID());
        if (statusUpdate.equals("Success")) {
            List<ChkThreshold> chkThreshold = beforeGetReportDAO.checkThreshold(new BigDecimal(tokenApi.get(0).getuID()));
            if (chkThreshold.get(0).getChk() != null && chkThreshold.get(0).getChk()) {
                logger.info("Status Code::429");
                logger.info("Error Code::00050");
                msgErr = beforeGetReportDAO.getErrorMessageByErrorCodeNative("00050").get(0);
                loggerText = String.format("msgErr>>> %s", msgErr);
                logger.info(loggerText);
                loggerText = String.format("ErrorCode>>> %s", msgErr.getErrorCode());
                logger.info(loggerText);
                loggerText = String.format("ErrorMessage>>> %s", msgErr.getErrorMessage());
                logger.info(loggerText);
            } else {
                logger.info("Status Code::200");
                logger.info("threshold not over limit ");
            }
        } else {
            msgErr.setErrorCode("500");
            msgErr.setStatusCode(500);
            msgErr.setErrorMessage("Cannot update treshold");
        }
        return msgErr;
    }

    public List<TokenAPI> getTokenIdFromCCRAToken(String accessToken) {
        return new BeforeGetReportDAO(jdbcTemplate).getTokenIdFromCCRAToken(accessToken);
    }


    public List<TrnJson> getDataFromTokenCDIID(Long tokenCDIID) {
        return new BeforeGetReportDAO(jdbcTemplate).getDataFromTokenCDIID(tokenCDIID);
    }

    public List<TrnJson> getDataFromTokenCDIIDandUIDandModule(Long tokenCDIID, Integer uID, String module) {
        return new BeforeGetReportDAO(jdbcTemplate).getDataFromTokenCDIIDandUIDandModule(tokenCDIID, uID, module);
    }

}
