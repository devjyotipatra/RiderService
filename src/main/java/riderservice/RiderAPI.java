package riderservice;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by 17349 on 24/01/17.
 */
@Path("/api")
public  class RiderAPI {
    @Path("/")
    @GET
    public Response hereIAm() {
        return Response.ok("{}", MediaType.APPLICATION_JSON).build();
    }


    @Path("/")
    @GET
    public Response whereIsMyDriver() {
        return Response.ok("{}", MediaType.APPLICATION_JSON).build();
    }
}
