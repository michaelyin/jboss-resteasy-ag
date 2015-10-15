package ag.login.client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.ws.rs.core.Cookie;


public class BrowserSimulator {
	
	public final static String CP_USER = "ag";
	public final static String CP_PW = "ag";
	
	private final static String firstUrl = "http://localhost:8080/resteasy-test-0.1.0/";
	private final static String loginUrl = "http://localhost:8080/resteasy-test-0.1.0/j_security_check";
	
	/**
	 * 
	 */
	public BrowserSimulator() throws Exception {
		// make sure cookies is turn on
		CookieHandler.setDefault(new CookieManager());
		init();
	}

	private List<String> cookies;
	private HttpURLConnection conn;
	private final String USER_AGENT = "Mozilla/5.0";
	
	private void init() throws Exception{
		// 1. login
		System.out.println("initialization, login...");
		login();
	}
	
	public boolean isUserLoggedIn(){
		return isUserLoggedIn;
	}

	private boolean isUserLoggedIn = false;
	private void login() throws Exception {
		//1. Send a "GET" request, so that you can extract the form's data.
		String page = getPageContent(firstUrl);
		System.out.print(page + '\n');
		//FileHelper.save(page, "caipiao.html");
		
		String postParams = fillLoginForm(page, CP_USER, CP_PW);

		// 2. Construct above post's content and then send a POST request for
		// authentication
		isUserLoggedIn = sendLoginPost(loginUrl, postParams);
		System.out.println("login successfully? " + isUserLoggedIn);
	}

	private String getPageContent(String url) throws Exception {

		URL obj = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) obj.openConnection();

		// default is GET
		((HttpURLConnection) conn).setRequestMethod("GET");

		conn.setUseCaches(false);

		// act like a browser
		conn.setRequestProperty("User-Agent", USER_AGENT);
		conn.setRequestProperty("Accept",
				"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		// conn.setRequestProperty("Accept-Encoding", "gzip, deflate, sdch");
		conn.setRequestProperty("Accept-Language", "en-US,en;q=0.8,zh-CN;q=0.6");
		conn.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded;charset=UTF-8");

		if (cookies != null) {
			for (String cookie : this.cookies) {
				System.out.print(cookie + '\n');
				conn.addRequestProperty("Cookie", cookie.split(";", 1)[0]);
			}
		}
		int responseCode = conn.getResponseCode();
		System.out.println("Sending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		/*
		 * Well I'm thinking the problem is when you are reading from the
		 * stream. You should either call the readUTF method on the
		 * DataInputStream instead of calling readLine or, what I would do,
		 * would be to create an InputStreamReader and set the encoding, then
		 * you can read from the BufferedReader line by line (this would be
		 * inside your existing try/catch):
		 */
		Charset charset = Charset.forName("UTF8");
		BufferedReader in = new BufferedReader(new InputStreamReader(
				conn.getInputStream(), charset));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		// Get the response cookies
		setCookies(conn.getHeaderFields().get("Set-Cookie"));

		return response.toString();

	}
	
	public String fillLoginForm(String html, String username, String password)
			throws UnsupportedEncodingException {

		System.out.println("Extracting form's data...");

		List<String> paramList = new ArrayList<String>();
		paramList.add("j_username" + "=" + URLEncoder.encode(username, "UTF-8"));
		paramList.add("j_password" + "=" + URLEncoder.encode(password, "UTF-8"));

	//	String value = "http://localhost:8080/resteasy-test-0.1.0/j_security_check";
		//paramList.add("url" + "=" + URLEncoder.encode(value, "UTF-8"));
		//paramList.add("url2" + "=" + URLEncoder.encode(value, "UTF-8"));


		// build parameters list
		StringBuilder result = new StringBuilder();
		for (String param : paramList) {
			if (result.length() == 0) {
				result.append(param);
			} else {
				result.append("&" + param);
			}
		}
		return result.toString();
	}
	
	/**
	 *  used in login to the cp.163.com
	 */
	private boolean sendLoginPost(String url, String postParams) throws Exception {

		URL obj = new URL(url);
		conn = (HttpURLConnection) obj.openConnection();

		// Acts like a browser
		conn.setUseCaches(false);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Host", "localhost:8080");
		conn.setRequestProperty("User-Agent", USER_AGENT);
		conn.setRequestProperty("Accept",
				"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		for (String cookie : this.cookies) {
			conn.addRequestProperty("Cookie", cookie.split(";", 1)[0]);
		}
		conn.setRequestProperty("Connection", "keep-alive");
		conn.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded;charset=UTF-8");
		conn.setRequestProperty("Content-Length",
				Integer.toString(postParams.length()));

		conn.setDoOutput(true);
		conn.setDoInput(true);

		// Send post request
		DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
		wr.writeBytes(postParams);
		wr.flush();
		wr.close();

		int responseCode = conn.getResponseCode();
		System.out.println("Sending 'POST' request to URL : " + url);
		System.out.println("Post parameters : " + postParams);
		System.out.println("Response Code : " + responseCode);
		
		

		///*
		BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		System.out.print(response.toString() + '\n');
		//*/
		
		return responseCode==HttpsURLConnection.HTTP_OK;
	}
	
	public List<String> getCookies() {
		return cookies;
	}

	public void setCookies(List<String> cookies) {
		this.cookies = cookies;
		for(String c:cookies){
			System.out.println(c);
		}
	}
	
	
	private final static String JS_KEY = "JSESSIONID";
	/*
	 * JSESSIONID=8B6WtEuzvzzI4X7Ivt1kprhx.matrix; path=/resteasy-test-0.1.0
	 */
	public Cookie generateSessionCookie(){
		
		for(String c:cookies){
			if(c.contains(JS_KEY)){
				//handle strings to get Jsessionid
				String[] ss = c.split(";");
				for(String s:ss){
					if(s.contains(JS_KEY)){
						String[] keyValue = s.split("=");
						Cookie ck = new Cookie(keyValue[0], keyValue[1]);
						return ck;
					}
						
				}
			}
		}
		
		return null;
	}


}
