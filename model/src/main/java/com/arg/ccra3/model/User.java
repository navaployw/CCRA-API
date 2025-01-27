
package com.arg.ccra3.model;

import com.arg.cb2.inquiry.DateUtil;
import com.arg.cb2.inquiry.DocumentLanguage;
import com.arg.cb2.inquiry.PreferLanguage;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class User {
    private static final String[] NULL_STRING_ARRAY = new String[0];

    private Date loginTime = null;
    private Integer AIID = null;
    private Integer CCRAID = null;
    private Integer groupID = null;
    private Integer uID = null;
    private Integer uniqueKey = null;
    private Long comapnyID = null;
    private List reportSegmentList = null;
    private PreferLanguage preferLanguage = null;
    private String AIDescription = null;
    private String AINameEnglish = null;
    private String AINameLocal = null;
    private String addressEng1;
    private String addressEng2;
    private String addressEng3;
    private String addressLocal = null;
    private String areaCode = null;
    private String cityEnglish = null;
    private String cityLocal = null;
    private String contributorCode = null;
    private String country = null;
    private String countryCode = null;
    private String description = null;
    private String email = null;
    private String faxNO = null;
    private String groupDescription = null;
    private String groupNameEnglish = null;
    private String groupNameLocal = null;
    private String phoneNO = null;
    private String provinceEnglish = null;
    private String provinceLocal = null;
    private String url = null;
    private String userID = null;
    private String userNameEnglish = null;
    private String userNameLocal = null;
    private boolean adminFlag = false;
    private boolean cannotChangePassword = false;
    private boolean requiredChangePassword = false;
    private int idleTime = 0;
    private int mailbox = -1;
    private int rowPerPage = 10;
    private String password = null;
    private Date expireDate = null;
    private Boolean disabled = false;
    

    public String getAIDescription()
    {
        return AIDescription;
    }
    public Integer getAIID()
    {
        return AIID;
    }
    public String getAIName()
    {
        if (PreferLanguage.CHINESE.equals(preferLanguage))
        {
            return AINameLocal;
        }
        return AINameEnglish;
    }
    public String getAINameEnglish()
    {
        return AINameEnglish;
    }
    public String getAINameLocal()
    {
        return AINameLocal;
    }
    public String getAddress()
    {
        if (PreferLanguage.CHINESE.equals(preferLanguage))
        {
            return addressLocal;
        }
        return getAddressEnglish();
    }
    public String getAddressEng1()
    {
        return addressEng1;
    }
    public String getAddressEng2()
    {
        return addressEng2;
    }
    public String getAddressEng3()
    {
        return addressEng3;
    }
    public String getAddressEnglish()
    {
        StringBuffer buf = new StringBuffer();
        if ((null != addressEng1) && (0 < addressEng1.length()))
        {
            buf.append(addressEng1);
        }
        if ((null != addressEng2) && (0 < addressEng2.length()))
        {
            if (0 < buf.length())
            {
                buf.append(", ");
            }
            buf.append(addressEng2);
        }
        if ((null != addressEng3) && (0 < addressEng3.length()))
        {
            if (0 < buf.length())
            {
                buf.append(", ");
            }
            buf.append(addressEng3);
        }
        return buf.toString();
    }
    public String getAddressLocal()
    {
        return addressLocal;
    }
    public String getAreaCode()
    {
        return areaCode;
    }
    public Integer getCCRAID()
    {
        return CCRAID;
    }
    public String getCity()
    {
        if (PreferLanguage.CHINESE.equals(preferLanguage))
        {
            return cityLocal;
        }
        return cityEnglish;
    }
    public String getCityEnglish()
    {
        return cityEnglish;
    }
    public String getCityLocal()
    {
        return cityLocal;
    }
    public String getContributorCode()
    {
        return contributorCode;
    }
    public String getCountry()
    {
        return country;
    }
    public String getCountryCode()
    {
        return countryCode;
    }
    public String getDescription()
    {
        return description;
    }
    public String getEmail()
    {
        return email;
    }
    public String getFaxNO()
    {
        return faxNO;
    }
    public String getGroupDescription()
    {
        return groupDescription;
    }
    public Integer getGroupID()
    {
        return groupID;
    }
    public String getGroupNameEnglish()
    {
        return groupNameEnglish;
    }
    public String getGroupNameLocal()
    {
        return groupNameLocal;
    }
    public int getIdleTime()
    {
        return idleTime;
    }
    public Locale getLocale()
    {
        if (null == preferLanguage)
        {
            return Locale.ENGLISH;
        }
        return preferLanguage.getLocale();
    }
    public String getLoginFormatTime()
    {
        String time =
            DateUtil.convertToString(this.loginTime, DocumentLanguage.ENGLISH,
                DateUtil.FULL_FORMAT);
        if (null == time)
        {
            return null;
        }
        return time;
    }
    public Date getLoginTime()
    {
        return loginTime;
    }
    public int getMailbox()
    {
        return mailbox;
    }
    public String getPhoneNO()
    {
        return phoneNO;
    }
    public PreferLanguage getPreferLanguage()
    {
        return preferLanguage;
    }
    public String getProvince()
    {
        if (PreferLanguage.CHINESE.equals(preferLanguage))
        {
            return provinceLocal;
        }
        return provinceEnglish;
    }
    public String getProvinceEnglish()
    {
        return provinceEnglish;
    }
    public String getProvinceLocal()
    {
        return provinceLocal;
    }
    public String[] getReportSegmentList()
    {
        if (null == reportSegmentList)
        {
            return NULL_STRING_ARRAY;
        }
        return (String[]) reportSegmentList.toArray(new String[0]);
    }
    public int getRowPerPage()
    {
        return rowPerPage;
    }
    public Integer getUniqueKey()
    {
        return uniqueKey;
    }
    public String getUrl()
    {
        return url;
    }
    public String getUserID()
    {
        return userID;
    }
    public String getUserName()
    {
        return userID;
    }
    public String getUserNameEnglish()
    {
        return userNameEnglish;
    }
    public String getUserNameLocal()
    {
        return userNameLocal;
    }
    public boolean hasPrivilege(String segment)
    {
        if ((null == segment) || (segment.trim().length() == 0))
        {
            return false;
        }
        if (null == reportSegmentList)
        {
            return false;
        }
        return reportSegmentList.contains(segment);
    }
    public boolean isAdminFlag()
    {
        return adminFlag;
    }
    public boolean isCannotChangePassword()
    {
        return cannotChangePassword;
    }
    public boolean isRequiredChangePassword()
    {
        return requiredChangePassword;
    }
    public void reset()
    {
        uID = null;
        groupID = null;
        AIID = null;
        CCRAID = null;
        userID = null;
        adminFlag = false;
        uniqueKey = null;
        userNameEnglish = null;
        userNameLocal = null;
        description = null;
        contributorCode = null;
        addressEng1 = null;
        addressEng2 = null;
        addressEng3 = null;
        addressLocal = null;
        cityEnglish = null;
        cityLocal = null;
        provinceEnglish = null;
        provinceLocal = null;
        country = null;
        countryCode = null;
        areaCode = null;
        phoneNO = null;
        faxNO = null;
        url = null;
        email = null;
        loginTime = null;
        AINameEnglish = null;
        AINameLocal = null;
        AIDescription = null;
        groupNameEnglish = null;
        groupNameLocal = null;
        groupDescription = null;
        idleTime = 0;
        preferLanguage = null;
        requiredChangePassword = false;
        cannotChangePassword = false;
        reportSegmentList = null;
        rowPerPage = 10;
        mailbox = -1;
    }
    public void setAIDescription(String description)
    {
        AIDescription = description;
    }
    public void setAIID(int i)
    {
        if (0 != i)
        {
            AIID = i;
        }
    }
    public void setAIID(Integer integer)
    {
        AIID = integer;
    }
    public void setAINameEnglish(String string)
    {
        AINameEnglish = string;
    }
    public void setAINameLocal(String string)
    {
        AINameLocal = string;
    }
    public void setAddressEng1(final String addr)
    {
        addressEng1 = addr;
    }
    public void setAddressEng2(final String addr)
    {
        addressEng2 = addr;
    }
    public void setAddressEng3(final String addr)
    {
        addressEng3 = addr;
    }
    public void setAddressLocal(String string)
    {
        addressLocal = string;
    }
    public void setAdminFlag(boolean b)
    {
        adminFlag = b;
    }
    public void setAreaCode(String string)
    {
        areaCode = string;
    }
    public void setCCRAID(int i)
    {
        if (0 != i)
        {
            CCRAID = i;
        }
    }
    public void setCCRAID(Integer integer)
    {
        CCRAID = integer;
    }
    public void setCannotChangePassword(boolean b)
    {
        cannotChangePassword = b;
    }
    public void setCityEnglish(String string)
    {
        cityEnglish = string;
    }
    public void setCityLocal(String string)
    {
        cityLocal = string;
    }
    public void setContributorCode(String contributorCode)
    {
        this.contributorCode = contributorCode;
    }
    public void setCountry(String string)
    {
        country = string;
    }
    public void setCountryCode(String string)
    {
        countryCode = string;
    }
    public void setDescription(String string)
    {
        description = string;
    }
    public void setEmail(String string)
    {
        email = string;
    }
    public void setFaxNO(String string)
    {
        faxNO = string;
    }
    public void setGroupDescription(String groupDescription)
    {
        this.groupDescription = groupDescription;
    }
    public void setGroupID(int i)
    {
        if (0 != i)
        {
            groupID = i;
        }
    }
    public void setGroupID(Integer integer)
    {
        groupID = integer;
    }
    public void setGroupNameEnglish(String groupNameEnglish)
    {
        this.groupNameEnglish = groupNameEnglish;
    }
    public void setGroupNameLocal(String groupNameLocal)
    {
        this.groupNameLocal = groupNameLocal;
    }
    public void setIdleTime(int i)
    {
        idleTime = i;
    }
    public void setLoginTime(Date date)
    {
        loginTime = date;
    }
    public void setMailbox(int mailbox)
    {
        this.mailbox = mailbox;
    }
    public void setPhoneNO(String string)
    {
        phoneNO = string;
    }
    public void setPreferLanguage(String language)
    {
        preferLanguage = new PreferLanguage(language);
    }
    public void setPreferLanguage(PreferLanguage language)
    {
        preferLanguage = language;
    }
    public void setProvinceEnglish(String string)
    {
        provinceEnglish = string;
    }
    public void setProvinceLocal(String string)
    {
        provinceLocal = string;
    }
    public void setReportSegmentList(List list)
    {
        reportSegmentList = list;
    }
    public void setRequiredChangePassword(boolean b)
    {
        requiredChangePassword = b;
    }
    public void setRowPerPage(int i)
    {
        rowPerPage = i;
    }
    public void setUniqueKey(Integer sessionID)
    {
        uniqueKey = sessionID;
    }
    public void setUrl(String string)
    {
        url = string;
    }
    public void setUserID(String string)
    {
        userID = string;
    }
    public void setUserNameEnglish(String string)
    {
        userNameEnglish = string;
    }
    public void setUserNameLocal(String string)
    {
        userNameLocal = string;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }
    public Integer getuID() {
        return uID;
    }

    public void setuID(Integer uID) {
        this.uID = uID;
    }

    public Long getComapnyID() {
        return comapnyID;
    }

    public void setComapnyID(Long comapnyID) {
        this.comapnyID = comapnyID;
    }

    @Override
    public String toString() {
        return "User{" + "loginTime=" + loginTime + ", AIID=" + AIID + ", CCRAID=" + CCRAID + ", groupID=" + groupID + ", uID=" + uID + ", uniqueKey=" + uniqueKey + ", comapnyID=" + comapnyID + ", reportSegmentList=" + reportSegmentList + ", preferLanguage=" + preferLanguage + ", AIDescription=" + AIDescription + ", AINameEnglish=" + AINameEnglish + ", AINameLocal=" + AINameLocal + ", addressEng1=" + addressEng1 + ", addressEng2=" + addressEng2 + ", addressEng3=" + addressEng3 + ", addressLocal=" + addressLocal + ", areaCode=" + areaCode + ", cityEnglish=" + cityEnglish + ", cityLocal=" + cityLocal + ", contributorCode=" + contributorCode + ", country=" + country + ", countryCode=" + countryCode + ", description=" + description + ", email=" + email + ", faxNO=" + faxNO + ", groupDescription=" + groupDescription + ", groupNameEnglish=" + groupNameEnglish + ", groupNameLocal=" + groupNameLocal + ", phoneNO=" + phoneNO + ", provinceEnglish=" + provinceEnglish + ", provinceLocal=" + provinceLocal + ", url=" + url + ", userID=" + userID + ", userNameEnglish=" + userNameEnglish + ", userNameLocal=" + userNameLocal + ", adminFlag=" + adminFlag + ", cannotChangePassword=" + cannotChangePassword + ", requiredChangePassword=" + requiredChangePassword + ", idleTime=" + idleTime + ", mailbox=" + mailbox + ", rowPerPage=" + rowPerPage + ", password=" + password + '}';
    }

    
    
}
