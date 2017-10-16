package au.edu.unsw.soacourse.rest;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
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

import org.json.*;
import org.springframework.web.bind.annotation.CrossOrigin;


@Path("/notices")
public class NoticeRestResources {
	@Context
	UriInfo uriInfo;
	@Context
	Request request;

	@POST
	@Produces({MediaType.APPLICATION_JSON})
	@CrossOrigin(origins = "http://licenserenewalservice-env.2qcm7emnen.ap-southeast-2.elasticbeanstalk.com/")
	public Response createNotices(@HeaderParam("x-auth-token") String authToken){
		if(authToken == null){
			return Response.status(401).entity("{\"error\":\"You need to be logged in as an officer to use this endpoint.\"}").build();
		}
		List<NoticeBean> notices = NoticeDao.createNotices();
		GenericEntity entity = new GenericEntity<List<NoticeBean>>(notices){};
		if(notices.size()==0){
			return Response.status(409).entity("{\"error\":\"Resources have already been created.\"}").build();
		}
		return Response.created(uriInfo.getAbsolutePath()).entity(entity).build();
	}
	
	@GET
	@Path("/{token}/{id}")
	@Produces({MediaType.APPLICATION_JSON})
	@CrossOrigin(origins = "http://licenserenewalservice-env.2qcm7emnen.ap-southeast-2.elasticbeanstalk.com/")
	public Response getNotice(@PathParam("token") String token, @PathParam("id") int id){
		NoticeBean notice = NoticeDao.getNotice(token, id);
		if(!notice.getToken().equals(token)){
			return Response.status(404).entity("{\"error\":\"No notice with that Id found.\"}").build();
		}
		GenericEntity entity = new GenericEntity<NoticeBean>(notice){};
		return Response.ok().entity(notice).build();
	}
	
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@CrossOrigin(origins = "http://licenserenewalservice-env.2qcm7emnen.ap-southeast-2.elasticbeanstalk.com/")
	public Response getAllNotices(@HeaderParam("x-auth-token") String authToken){
		if(authToken == null){
			return Response.status(401).entity("{\"error\":\"You need to be logged in as an officer to use this endpoint.\"}").build();
		}
		List<NoticeBean> notices = NoticeDao.getAllNotices();
		GenericEntity entity = new GenericEntity<List<NoticeBean>>(notices){};
		if(notices.size() == 0){
			return Response.status(404).entity("{\"error\":\"No notices found.\"}").build();
		}
		return Response.ok().entity(entity).build();
	}
	
	@PUT
	@Path("/{token}/{id}")
	@Produces({MediaType.APPLICATION_JSON})
	@CrossOrigin(origins = "http://licenserenewalservice-env.2qcm7emnen.ap-southeast-2.elasticbeanstalk.com/")
	public Response updateNotice(@PathParam("id") int id, @PathParam("token") String token, String body){
		//TODO potential rejection reason
		JSONObject json = new JSONObject(body);
		String email = "";
		String address = "";
		String status = "";
		String reason = "";
		if(json.has("reason")){
			//TODO Handle it
		}
		if(json.has("email")){
			email = json.getString("email");
		}
		if(json.has("address")){
			address = json.getString("address");
		}
		if(json.has("status")){
			status = json.getString("status");
		}
		NoticeBean notice = NoticeDao.updateNotice(id, token, email, address, status);
		if(!notice.getToken().equals(token)){
			return Response.status(400).entity("{\"error\":\"No notice found.\"}").build();
		}
		GenericEntity entity = new GenericEntity<NoticeBean>(notice){};
		return Response.ok().entity(entity).build();
	}
	
	@DELETE
	@Path("/{token}/{id}")
	@Produces({MediaType.APPLICATION_JSON})
	@CrossOrigin(origins = "http://licenserenewalservice-env.2qcm7emnen.ap-southeast-2.elasticbeanstalk.com/")
	public Response deleteNotice(@PathParam("id") int id, @PathParam("token") String token){
		boolean deleted = NoticeDao.deleteNotice(id, token);
		if(!deleted){
			return Response.status(404).entity("{\"error\":\"No notice with that Id found.\"}").build();
		}
		return Response.ok().entity("{\"message\":\"Resource has been archived.\"}").build();
	}
}