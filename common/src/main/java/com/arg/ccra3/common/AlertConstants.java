package com.arg.ccra3.common;

public class AlertConstants {

    private AlertConstants() {
    }

    public static class MESSAGE {

        private MESSAGE() {
        }
        public static final String YOU = "You";
    }

    public static class PRODUCT_TYPE {

        private PRODUCT_TYPE() {
        }
        public static final int MAILBOX = 0;
        public static final int FULL_FILE_REJECTION_REPORT = 6;
        public static final int VALIDATION_ERROR_REPORT = 7;
        public static final int INPUT_FILE_SUMMARY_REPORT = 8;
        public static final int CONFIMATION_REVOC_CONSENT_REPORT = 9;
        public static final int UNLOAD_REPORT = 10;
        public static final int MONITORING_ACCOUNT_AUTORENEW = 12;
        public static final int OMS_OUTSTANDING_RECORD = 13;
        public static final int OMS_AWAITING_APPROVAL = 14;
    }

    public static class USER {

        private USER() {
        }
        public static final int CCRA = 1;
    }
}
