package pl.polsl.io.service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
/**
 *
 * @author Michal
 */
public class CookieService {
    
    public CookieService(){}
    
    public String getCookieValue(String cookieName, HttpServletRequest request){
        if(cookieName == null){
            return "";
        }
        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            for(Cookie cookie : cookies){
                if(cookie.getName().equals(cookieName)){
                    return cookie.getValue();
                }
            }
        }
        return "";
    }
    
}
