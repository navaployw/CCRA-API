package com.arg.ccra3.config;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import com.arg.ccra3.models.User;
import com.arg.ccra3.models.api.TokenAPI;
import com.arg.ccra3.models.api.UserAPI;
import com.arg.ccra3.online.service.BeforeGetReport;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;

/**
 * 
 * @author sitthichaim
 *
 */
//@Component
public class JwtTokenUtil implements Serializable {
	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

	private static final long serialVersionUID = 4920283549102498659L;
	
	@Value("${jwt.secret}")
	private String secret;

    private BeforeGetReport beforeGetReport;

	public JwtTokenUtil(BeforeGetReport beforeGetReport){
		this.beforeGetReport = beforeGetReport;
	}

    public Long getUserIdFormToken(HttpServletRequest request) {
    	String token = request.getHeader("Authorization").substring(7);
    	final Claims claims = getAllClaimsFromToken(token);
    	return claims.get("id", Long.class);
    }
    
	public Claims getAllClaimsFromToken(String token) {

		// List<TokenAPI> tokenApi = beforeGetReport.getTokenIdFromCCRAToken(token);
		// TokenAPI model = tokenApi.get(0);
		// logger.info("secret key by user ###### "+ model.getSecretKey());
		// // System.out.println(Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody());
		// logger.info("decode.....");
		// return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
		logger.info("secret .... "+ secret);
		logger.info("token .... "+ token);
		return Jwts.parser().setSigningKey(secret.getBytes()).parseClaimsJws(token).getBody();
	}
	

	public String deCodeCDIToken(String cdiToken) {
        try {
            Base64 base64Url = new Base64(true);
            String jwtToken = cdiToken;
            logger.info("------------ Decode JWT cdiToken ------------");
            String[] split_string = jwtToken.split("\\.");
            String base64EncodedHeader = split_string[0];
            String base64EncodedBody = split_string[1];
            String base64EncodedSignature = split_string[2];

            logger.info("~~~~~~~~~ JWT Header cdiToken ~~~~~~~");

            String header = new String(base64Url.decode(base64EncodedHeader));
            // loggerText = String.format("JWT Header : %s", header);
            // logger.info(loggerText);

            logger.info("~~~~~~~~~ JWT Body cdiToken~~~~~~~");
            String body = new String(base64Url.decode(base64EncodedBody));
            // loggerText = String.format("JWT Body : %s", body);
            // logger.info(loggerText);
            // loggerText = String.format("JWT Signature : %s", base64EncodedSignature);
            // logger.info(loggerText);
            return body;
        } catch (Exception e) {
            // loggerText = String.format("Error:deCodeCDIToken: %s", e);
            // logger.error(loggerText);
			logger.error(e.getMessage(), e);
            return "Error";
        }
    }
    // public String getLangFromHeader(HttpServletRequest request) {
    // 	String lang = request.getHeader("lang");
    // 	return StringUtils.isBlank(lang) ? DEFAULT_LANG : lang;
    // }
	
	private Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}
	
	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}
	
	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}
	
	public String generateToken(UserAPI user) {
		Map<String, Object> claims = new HashMap<>();
		return doGenerateToken(claims, user.getUserid());
	}
	
	private String doGenerateToken(Map<String, Object> claims, String subject) {

		return Jwts.builder()
				.setClaims(claims)
				.setSubject(subject)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 5l * 60 * 60 * 1000))
				.signWith(SignatureAlgorithm.HS256, secret).compact();
	}

	// public Boolean validateToken(String token, User autUser) {
	// 	if (null == autUser) return false;
	// 	final Long uid = getUserIdFromToken(token);
	// 	return (uid.equals(autUser.getuID().longValue()) && !isTokenExpired(token));
	// }
	
	public void authenticate(String user, String passwordEnc, UserAPI autUser) throws Exception{
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(passwordEnc);
       logger.info("Hash end .."+ hashedPassword);
        
        // if(!passwordEncoder.matches(passwordEnc, autUser.get))
        // {
        //     throw new Exception("Authen Fail");
        // }
    }
	
	public String getUserIdFromToken(String token) {
		// deCodeCDIToken(token);
		final Claims claims = getAllClaimsFromToken(token);
		return claims.get("ai_code", String.class);
	}

	public String getUsernameFromToken(String token) {
		final Claims claims = getAllClaimsFromToken(token);
		return claims.get("name", String.class);
	}

	

    
}
