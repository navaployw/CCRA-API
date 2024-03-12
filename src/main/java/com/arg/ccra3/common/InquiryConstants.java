package com.arg.ccra3.common;

public class InquiryConstants {
    
    private InquiryConstants(){
    }
    public static class ALERT_CONSTANTS
    {

        private ALERT_CONSTANTS() {
        }
        
        public static final String ENQUIRY_ALERT = "E";
        public static final String MONITORING_ALERT = "M";
    }

    public static class BASE_REASONCODE
    {
         private BASE_REASONCODE() {
        }
        public static final String DISCHARGECODE = "C";
        public static final String DELETECODE = "D";
        public static final String INQUIRYCODE = "I";
        public static class DISCHARGECODES
        {
            private DISCHARGECODES(){}
            public static final int DEFAULT = 100;
        }

    }

    public static class CHARSETS
    {
        private CHARSETS() {
        }
        public static final String UTF8 = "UTF-8";
        public static final String UTF16 = "UTF-16";
        public static final String ISO88591 = "ISO-8859-1";
    }

    public static class DOCUMENT_LANGUAGE
    {
        private DOCUMENT_LANGUAGE() {
        }
        public static final String ENGLISH = "E";
        public static final String LOCAL = "L";
    }

    public static class FLAGEXPENSE
    {
        private FLAGEXPENSE() {
        }
        public static final String CREATE_REPORT = "C";
        public static final String DROP_SEARCH = "D";
    }

    public static class OBJECT_TYPE
    {
         private OBJECT_TYPE() {
        }
        public static final int SEARCH = 47;
        public static final int BASIC_REPORT = 50;
        public static final int CHINESE_REPORT = 51;
        public static final int SEARCH_API = 202;
        public static final int BASIC_REPORT_API = 203;
        public static final int CHINESE_REPORT_API = 204;
        public static final int MONITORING_ALERT = 54;
        public static final int ENQUIRY_ALERT = 56;
        public static final int SUBSCRIBE_MONITORING_ALERT = 55;
        public static final int SUBSCRIBE_ENQUIRY_ALERT = 57;
    }

    public static class PRODUCT_DATA_CONSTANTS
    {
        private PRODUCT_DATA_CONSTANTS() {
        }
        public static final int EXECUTIVE_SUMMARY = 2;
        public static final int EXECUTIVE_SUMMARY_EN = 3;
        public static final int EXECUTIVE_SUMMARY_LO = 4;
        public static final int PROFILE = 5;
        public static final int PROFILE_EN = 6;
        public static final int PROFILE_LO = 7;
        public static final int COMPANY_GROUP_STRUCTURE = 8;
        public static final int COMPANY_GROUP_STRUCTURE_EN = 9;
        public static final int COMPANY_GROUP_STRUCTURE_LO = 10;
        public static final int COURT = 11;
        public static final int COURT_EN = 12;
        public static final int COURT_LO = 13;
        public static final int REVOCATION_OF_CONSENT = 14;
        public static final int REVOCATION_OF_CONSENT_EN = 15;
        public static final int REVOCATION_OF_CONSENT_LO = 16;
        public static final int POSITIVE_LOAN_DATA = 18;
        public static final int NEGATIVE_LOAN_DATA = 19;
        public static final int WRITEOFF = 20;
        public static final int AI_ORDERED_LIST = 23;
    }

    public static class REASON_CODE
    {
        private REASON_CODE() {
        }
        public static final int NEW_REASON = 1;
        public static final int ANNUAL_REVIEW_REASON = 2;
        public static final int REQUEST_ADJUSTMENT_REASON = 3;
        public static final int ACCOUNT_MONITORING_REASON = 4;
        public static final int GUANRANTOR_REASON = 5;
    }

    public static class REQUESTTYPE
    {
          private REQUESTTYPE() {
        }
        public static final String ONLINE = "O";
        public static final String BULK = "B";
        public static final String API = "A";
    }

    public static class SEARCH_ID_TYPE
    {
        private SEARCH_ID_TYPE() {
        }
        public static final int BRC = 1;
        public static final int CI = 2;
        public static final int OTHER_REG = 3;
    }

    public static class STR_SEPARATOR
    {
        private STR_SEPARATOR() {
        }
        public static final String FIELD_SEPARATOR = "|";
        public static final String ROW_SEPARATOR = "\t";
    }
}
