
package com.arg.ccra3.dao.security.exception;

import com.arg.util.GenericException;


public class FunctionIDNotFoundException
    extends GenericException
{

    
    public FunctionIDNotFoundException()
    {
        super();
    }

    
    public FunctionIDNotFoundException(String message)
    {
        super(message);
    }

    
    public FunctionIDNotFoundException(String errCode, String message)
    {
        super(errCode, message);
    }

    
    public FunctionIDNotFoundException(String message, Throwable cause)
    {
        super(message, cause);
    }

    
    public FunctionIDNotFoundException(String errCode, String message,
        Throwable cause)
    {
        super(errCode, message, cause);
    }

    
    public FunctionIDNotFoundException(Throwable cause)
    {
        super(cause);
    }
}
