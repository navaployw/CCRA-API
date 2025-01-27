package com.arg.ccra3.online.form;

public class RequestReportByIDForm {

    protected String idNumber = null;
    protected String idType = null;
    protected String placeRegist = null;

    public String getIdNumber()
    {
        return idNumber;
    }
    public String getIdType()
    {
        return idType;
    }
    public String getPlaceRegist()
    {
        return placeRegist;
    }
    /*public void reset(ActionMapping mapping, HttpServletRequest request)
    {
        super.reset(mapping, request);
        if ((null == (this.idType = (String) request.getAttribute("idType")))
                || (this.idType.trim().length() == 0))
        {
            this.idType = request.getParameter("idType");
        }
        if ((null == (this.idNumber = (String) request.getAttribute("idNumber")))
                || (this.idNumber.trim().length() == 0))
        {
            this.idNumber = request.getParameter("idNumber");
        }
        if ((null == (this.placeRegist = (String) request.getAttribute(
                        "placeRegist")))
                || (this.placeRegist.trim().length() == 0))
        {
            this.placeRegist = request.getParameter("placeRegist");
        }
    }*/
    public void setIdNumber(String string)
    {
        this.idNumber = string;
    }
    public void setIdType(String string)
    {
        idType = string;
    }
    public void setPlaceRegist(String string)
    {
        placeRegist = string;
    }
    /*public ActionErrors validate(ActionMapping mapping,
        HttpServletRequest request)
    {
        super.validate(mapping, request);
        if ((null == idType) || (idType.trim().length() == 0))
        {
            errors.add(ActionMessages.GLOBAL_MESSAGE,
                new ActionMessage("WBS-A00003"));
        }
        if ((null == idNumber) || (idNumber.trim().length() == 0))
        {
            errors.add(ActionMessages.GLOBAL_MESSAGE,
                new ActionMessage("WBS-A00004"));
        }
        if ((null != idType) && ("OTHER".equals(idType)))
        {
            if ((null == placeRegist) || (placeRegist.trim().length() == 0))
            {
                errors.add(ActionMessages.GLOBAL_MESSAGE,
                    new ActionMessage("WBS-A00005"));
            }
        }
        return errors;
    }*/
}
