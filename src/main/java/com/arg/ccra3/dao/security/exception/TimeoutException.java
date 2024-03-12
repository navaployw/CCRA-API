
package com.arg.ccra3.dao.security.exception;

import com.arg.util.GenericException;


public class TimeoutException
    extends GenericException
{

    
    public TimeoutException()
    {
        super();
    }

    
    public TimeoutException(String message)
    {
        super(message);
    }

    
    public TimeoutException(String errCode, String message)
    {
        super(errCode, message);
    }

    
    public TimeoutException(String message, Throwable cause)
    {
        super(message, cause);
    }

    
    public TimeoutException(String errCode, String message, Throwable cause)
    {
        super(errCode, message, cause);
    }

    
    public TimeoutException(Throwable cause)
    {
        super(cause);
    }
}
