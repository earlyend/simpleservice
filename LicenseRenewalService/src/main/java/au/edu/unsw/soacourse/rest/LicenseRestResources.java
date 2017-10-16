package au.edu.unsw.soacourse.rest;

import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.json.JSONObject;


@Path("/licenses")
public class LicenseRestResources {
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	
	/*@PUT
	@Path("/{token}/{licensenumber}")
	@Produces({MediaType.APPLICATION_JSON})
	public Response updateLicense(@PathParam("licensenumber") String licensenumber, @PathParam("token") String token,
			String body){
		JSONObject json = new JSONObject(body);
		String address = json.getString("address");
		String email = json.getString("email");
		LicenseBean license = LicenseDao.updateLicense(licensenumber, token, address, email);
		
	}*/
}
