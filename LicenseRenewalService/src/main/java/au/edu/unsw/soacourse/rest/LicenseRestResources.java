package au.edu.unsw.soacourse.rest;

import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.json.JSONObject;
//import org.springframework.web.bind.annotation.CrossOrigin;


@Path("/licenses")
public class LicenseRestResources {
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	
	@PUT
	@Path("/{token}/{licensenumber}")
	@Produces({MediaType.APPLICATION_JSON})
	//@CrossOrigin(origins = {"http://licenserenewalservice-env.2qcm7emnen.ap-southeast-2.elasticbeanstalk.com", "http://localhost:8080"})
	public Response updateLicense(@PathParam("licensenumber") String licensenumber, @PathParam("token") String token,
			String body){
		JSONObject json = new JSONObject(body);
		String address = json.getString("address");
		String email = json.getString("email");
		String expiryDate = json.getString("expiryDate");
		LicenseBean license;
		try{
			license = LicenseDao.updateLicense(licensenumber, token, address, email, expiryDate);
		}
		catch(NullPointerException e){
			return Response.status(404).entity("{\"error\":\"No notice with that Id found.\"}").build();
		}
		GenericEntity entity = new GenericEntity<LicenseBean>(license){};
		return Response.ok(entity).build();
	}
}
