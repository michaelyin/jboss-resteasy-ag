package ag;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.junit.Before;
import org.junit.Test;

import ag.domain.Employee;
import ag.login.client.BrowserSimulator;
import ag.login.client.CookieRequestFilter;

public class EmployeeControllerTest {
	
	private String baseUrl = "http://localhost:8080/resteasy-test-0.1.0";
	private BrowserSimulator simulator;
	Cookie cookie;
	
	@Before
	public void login() throws Exception {
		simulator = new BrowserSimulator();
		cookie = simulator.generateSessionCookie();
	}
	
	
	@Test
	public void testGetEmployee() throws IOException{
		if(null == cookie){
			fail("cannot login!!!");
		}
		
		Client client = new ResteasyClientBuilder().build();
		client.register(new CookieRequestFilter(cookie));
		WebTarget target = client.target(baseUrl + "/rest/ag/employee/100");
		Response response = target.request().get();
		
		//System.out.println( response.readEntity(String.class));
		int status = response.getStatus();
		System.out.println("response status code: " + status);
		boolean hasEntity = response.hasEntity();
		System.out.println("response has entity? " + hasEntity);
		
		Employee epl = response.readEntity(Employee.class);
		System.out.println(epl.toString());
		
		client.close();
		assertEquals("same first name", "Beyond", epl.getFirstName());
	}
	
	

	@Test
	public void testPostEmployee() {
		if(null == cookie){
			fail("cannot login!!!");
		}
		Employee ep1 = generateEmployee();
		
		try {
			ResteasyClient client = new ResteasyClientBuilder().build();
            
			WebTarget target = client.target(baseUrl + "/rest/ag/employee");
			client.register(new CookieRequestFilter(cookie));

			Response response = target.request().post(Entity.entity(ep1, "application/json"));

			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
			}

			Employee value = response.readEntity(Employee.class);
			System.out.println("Server response : \n");
			System.out.println(value.toString());
			
			assertEquals("same first name", "Special", value.getFirstName());
			
		    client.close();

		} catch (Exception e) {

			e.printStackTrace();

		}

	}
	
	private Employee generateEmployee(){
		Employee ep1 = new Employee();
		ep1.setCompany("VZ");;
		ep1.setFirstName("Special");
		ep1.setLastName("Lee");
		ep1.setId(99);
		ep1.setDepartment("IT");
		return ep1;
	}

}
