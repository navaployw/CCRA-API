
package com.arg.ccra3.dao.security.exception;

import com.arg.util.GenericException;


public class AuthenticationException
    extends GenericException
{
    
    public AuthenticationException()
    {
        super();
    }

    
    public AuthenticationException(String message)
    {
        super(message);
    }

    
    public AuthenticationException(String errCode, String message)
    {
        super(errCode, message);
    }

    
    public AuthenticationException(String message, Throwable cause)
    {
        super(message, cause);
    }

    
    public AuthenticationException(String errCode, String message,
        Throwable cause)
    {
        super(errCode, message, cause);
    }

    
    public AuthenticationException(Throwable cause)
    {
        super(cause);
    }
}
