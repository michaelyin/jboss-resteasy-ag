/**
 * 
 */
package ag;
import javax.ws.rs.GET;  
import javax.ws.rs.Path;  
import javax.ws.rs.PathParam;

/**
 * @author michael
 *
 */
@Path("ag")
public class TimeController {
	
	@GET
    @Path("{name}")
    public String quickCheck(@PathParam("name") String name){
		
        StringBuilder stringBuilder = new StringBuilder("welcome to AG rest testing sandbox: ");
        stringBuilder.append(name).append("!");

        return stringBuilder.toString();
    }
	

}
