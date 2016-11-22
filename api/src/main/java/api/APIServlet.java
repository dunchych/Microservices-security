package api;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Set;

import javax.security.auth.Subject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.ibm.websphere.security.WSSecurityException;
import com.ibm.websphere.security.auth.WSSubject;

/**
 * Servlet implementation class HelloServlet
 */
@WebServlet("/")
public class APIServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public APIServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//Allow CORS
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, DELETE, PUT");
		response.setHeader("Access-Control-Allow-Headers", "x-requested-with,Content-Type");
		response.setContentType("application/json");
		
		
		// Get security subjects
		// we were authenticated using OIDC, and we want to obtain the access_token that's part of this session.
		Subject s;
		try{
		     s = WSSubject.getRunAsSubject();
		}catch( WSSecurityException e){
		     throw new IOException(e);
		}
		   
		String json;

		if (s != null) {
			Set<Hashtable> privateHashtableCreds = s.getPrivateCredentials(Hashtable.class);		
			Gson gson = new Gson(); 
			json = gson.toJson(privateHashtableCreds); 
		} else {
			json = "{\"credentials\"=\"none\"}";
		}
		 
		//there could be many.. we'll just take the one with access_token.
		/*
		Hashtable theChosenOne = null;
		for(Hashtable test : privateHashtableCreds){
		 if(test.containsKey("access_token")){
		   theChosenOne = test;
		 }
		}		 
		//now we have found the credentials holding the current access_token
		//we will cache it locally so we can invoke the RS with it.
		String access_token = theChosenOne.get("access_token").toString();
		*/
		

		response.getWriter().append(json).append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//CORS for "preflighted" requests 
		// "preflighted" requests first send an HTTP request by the OPTIONS method to the resource on the other domain, 
		// in order to determine whether the actual request is safe to send
				response.setHeader("Access-Control-Allow-Origin", "*");
				response.setHeader("Access-Control-Allow-Methods", "POST, GET, DELETE, PUT");
				response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type");
				response.setContentType("application/json");
	}

}
