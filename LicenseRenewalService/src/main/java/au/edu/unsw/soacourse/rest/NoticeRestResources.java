package au.edu.unsw.soacourse.rest;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;


@Path("/notices")
public class NoticeRestResources {
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public List<NoticeBean> getNotices(){
		return NoticeDao.getAllNotices();
	}
	
	@POST
	@Produces({MediaType.APPLICATION_JSON})
	public NoticeBean createNotice(String licenseNumber, String address, String email){
		return NoticeDao.createNotice(licenseNumber, address, email);
	}
}