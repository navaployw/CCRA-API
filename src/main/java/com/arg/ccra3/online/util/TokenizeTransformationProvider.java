package com.arg.ccra3.online.util;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenizeTransformationProvider {
    public final static String PROVIDER_SECRET_KEY = "AES/ECB/PKCS5Padding";
    public final static String USER_CIPHER_ALGOR = "AES";
    public final static String USER_KEY_ALGOR = "AES";
    public final static String USER_KEY = "Y9cFfapTWdmk+7VNqjS1zor7p65xVkdPVAXLmtdmMhVOeiOoWGYeQJtbU8p6bDQe0FRURG9x9oR7FPbpt2fDGP+ieEoKa0gjEqw9DX5pgvufikX1TImdN0PwCrie54YvgMqpgSOZvM6sFUwNrqENR5M8n0qKaWSHomhdaJJNPi7AnMmdfSy8hPK0eScz8CYpIFya0CaZuwo8L7Dieqgpx7PNx3+WlRX3Tyiq3T1u5jeXOtPpngVYuBTjBaqFdCjPY++ejIh39vuAe/I1qo0jOhEOdulQxVmwwbfVJ6U3MrE=";

}
