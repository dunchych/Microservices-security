package test;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Set;

import javax.security.auth.Subject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import com.ibm.websphere.security.WSSecurityException;
import com.ibm.websphere.security.auth.WSSubject;

/**
 * Servlet Filter implementation class JwsFilter
 */
@WebFilter("/")
public class JwtFilter implements Filter {

    /**
     * Default constructor. 
     */
    public JwtFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		// place your code here
		String idToken = getIdToken();
		if (idToken != null) {	
			HttpServletResponse httpServletResponse = ((HttpServletResponse) response);
			Cookie c = new Cookie("X-Id-Token", idToken);
			c.setHttpOnly(false);
			c.setMaxAge(-1);
			httpServletResponse.addCookie(c);
		}

		// pass the request along the filter chain
		chain.doFilter(request, response);
	}
	
	private String getIdToken() {
		//we were authenticated using OIDC, and we want to obtain the access_token that's part of this session.
		Subject s;
		try{
		     s = WSSubject.getRunAsSubject();
		}catch( WSSecurityException e){
		     System.err.println(e);
		     return null;
		}
		
		if (s==null)
			return null;
		   
		Set<Hashtable> privateHashtableCreds = s.getPrivateCredentials(Hashtable.class);
		 
		//there could be many.. we'll just take the one with access_token.
		Hashtable theChosenOne = null;
		for(Hashtable test : privateHashtableCreds){
		 //if(test.containsKey("access_token")){
		 if(test.containsKey("id_token")){
		   theChosenOne = test;
		 }
		}
		
		//now we have found the credentials holding the current id_token
		//we will cache it locally so we can invoke the RS with it.
		String id_token = theChosenOne.get("id_token").toString();
		
		System.out.println("JWT token: "+id_token);
		//String id_token = theChosenOne.get("access_token").toString();
		return id_token;
							
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
