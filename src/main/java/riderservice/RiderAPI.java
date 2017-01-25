package riderservice;

import com.google.gson.Gson;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 17349 on 24/01/17.
 */
@Path("/drivers")
public  class RiderAPI {

    private static RiderAPIHandler handler = RiderAPIHandler.getHandlerInstance();

    @POST
    @Path("/{id}/location")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response hereIAm(@PathParam("id") int driverId, String location) {
        Map<String, Object> driverCoord = new Gson().fromJson(location, HashMap.class);
        driverCoord.put("id", driverId);

        handler.setDriverLocation(driverCoord);

        return Response.ok("{}", MediaType.APPLICATION_JSON).build();
    }


    @Path("/llb")
    @GET
    public Response whereIsMyDriver() {
        return Response.ok("{}", MediaType.APPLICATION_JSON).build();
    }
}
