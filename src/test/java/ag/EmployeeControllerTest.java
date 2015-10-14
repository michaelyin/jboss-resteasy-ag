package ag;

import static org.junit.Assert.*;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.junit.Test;

import ag.domain.Employee;

public class EmployeeControllerTest {
	
	private String baseUrl = "http://localhost:8080/resteasy-test-0.1.0";
	
	
	@Test
	public void testGetEmployee(){
		Client client = new ResteasyClientBuilder().build();
		WebTarget target = client.target(baseUrl + "/rest/ag/employee/100");
		Response response = target.request().get();
		
		Employee epl = response.readEntity(Employee.class);
		System.out.println(epl.toString());
		
		assertEquals("same first name", "Beyond", epl.getFirstName());
	}
	
	

	@Test
	public void testPostEmployee() {
		
		Employee ep1 = new Employee();
		ep1.setCompany("VZ");;
		ep1.setFirstName("Special");
		ep1.setLastName("Lee");
		ep1.setId(99);
		ep1.setDepartment("IT");
		
		try {
			ResteasyClient client = new ResteasyClientBuilder().build();
            
			WebTarget target = client
					.target(baseUrl + "/rest/ag/employee");

			Response response = target.request().post(Entity.entity(ep1, "application/json"));

			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
			}

			Employee value = response.readEntity(Employee.class);
			System.out.println("Server response : \n");
			System.out.println(value.toString());
			
			
			assertEquals("same first name", "Special", value.getFirstName());
			
			response.close();

		    client.close();

		} catch (Exception e) {

			e.printStackTrace();

		}

	}

}
