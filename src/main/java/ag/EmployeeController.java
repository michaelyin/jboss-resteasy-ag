package ag;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import ag.domain.Employee;

@Path("/ag")
public class EmployeeController {
	
	
	private static final Map<Integer, Employee> employeeRepo = new HashMap<Integer, Employee>();
	static {
		Employee ep1 = new Employee();
		ep1.setCompany("VZ");;
		ep1.setFirstName("Andrew");
		ep1.setLastName("Lee");
		ep1.setId(99);
		ep1.setDepartment("IT");
		
		Employee ep2 = new Employee();
		ep2.setCompany("MS");;
		ep2.setFirstName("Beyond");
		ep2.setLastName("Shi");
		ep2.setId(100);
		ep2.setDepartment("SALES");
		
		employeeRepo.put(ep1.getId(), ep1);
		employeeRepo.put(ep2.getId(), ep2);
		
	}
	
	private static AtomicInteger count = new AtomicInteger(101);
	
	@GET
	@Path("/employee/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Employee getEmployee(@PathParam("id") int id){
		
		System.out.println("retrieve employee with id: " + id);
		return employeeRepo.get(id);
		
	}
	
	@POST
	@Path("/employee")
	@Consumes("application/json")
	@Produces(MediaType.APPLICATION_JSON)
	public Employee updateEmployee(Employee employee){
		
		//System.out.println("update employee with id: " + employee.getId());
		System.out.println(employee.getCompany());
		System.out.println(employee.getId());
		if(employee.getId() == 0){
			//new customer
			int id = count.incrementAndGet();
			employee.setId(id);
			employeeRepo.put(id, employee);
		}else{
			int id = employee.getId();
			employeeRepo.put(id, employee);
		}
		
		return employee;
		
	}
	
	

}
