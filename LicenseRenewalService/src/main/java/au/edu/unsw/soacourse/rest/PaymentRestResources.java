package au.edu.unsw.soacourse.rest;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
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
import org.springframework.web.bind.annotation.CrossOrigin;

@Path("/notices/payments")
public class PaymentRestResources {
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	
	@POST
	@Path("/{token}/{id}")
	@Produces({MediaType.APPLICATION_JSON})
	@CrossOrigin(origins = "http://licenserenewalservice-env.2qcm7emnen.ap-southeast-2.elasticbeanstalk.com/")
	public Response createPayment(@PathParam("id") int id, @PathParam("token") String token,
			String body){
		JSONObject json = new JSONObject(body);
		String amount = json.getString("amount");
		String date = json.getString("payDate");
		PaymentBean payment = PaymentDao.createPayment(id, token, amount, date);
		if(payment.getAmount().isEmpty()){
			return Response.status(400).entity("{\"error\":\"Bad Request.\"").build();
		}
		GenericEntity entity = new GenericEntity<PaymentBean>(payment){};
		return Response.status(201).entity(payment).build();
	}
	
	@GET
	@Path("/{token}/{id}")
	@Produces({MediaType.APPLICATION_JSON})
	@CrossOrigin(origins = "http://licenserenewalservice-env.2qcm7emnen.ap-southeast-2.elasticbeanstalk.com/")
	public Response getPayment(@PathParam("id") int id, @PathParam("token") String token){
		PaymentBean payment;
		try{
			payment = PaymentDao.getPayment(id, token);
		}
		catch(NullPointerException e){
			return Response.status(404).entity("{\"error\":\"No payment found.\"}").build(); 
		}
		
		if(payment.getAmount().isEmpty()){
			return Response.status(404).entity("{\"error\":\"No payment found.\"}").build();
		}
		GenericEntity entity = new GenericEntity<PaymentBean>(payment){};
		return Response.ok(entity).build();
	}
	
	@PUT
	@Path("/{token}/{id}")
	@Produces({MediaType.APPLICATION_JSON})
	@CrossOrigin(origins = "http://licenserenewalservice-env.2qcm7emnen.ap-southeast-2.elasticbeanstalk.com/")
	public Response updatePayment(@PathParam("id") int id, @PathParam("token") String token, String body){
		String amount = "";
		String date = "";
		JSONObject json = new JSONObject(body);
		if(json.has("amount")){
			amount = json.getString("amount");
		}
		if(json.has("payDate")){
			date = json.getString("payDate");
		}
		PaymentBean payment;
		try{
			payment = PaymentDao.updatePayment(id, token, amount, date);
		}
		catch(NullPointerException e){
			return Response.status(404).entity("{\"error\":\"No payment found.\"}").build(); 
		}
		GenericEntity entity = new GenericEntity<PaymentBean>(payment){};
		return Response.ok(entity).build();
	}
	

}
