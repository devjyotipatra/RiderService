package riderservice;

import com.google.gson.Gson;
import com.sun.istack.internal.NotNull;
import riderservice.util.DriverCoordinate;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 17349 on 24/01/17.
 */
@Path("/drivers")
public  class RiderAPI {

    private static final double EARTHDIAM = 6371000.0;

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


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response whereIsMyDriver(@NotNull @QueryParam("latitude") double latitude,
                                    @NotNull @QueryParam("longitude") double longitude,
                                    @DefaultValue("500")  @QueryParam("radius") int radius,
                                    @DefaultValue("10")  @QueryParam("latitude") int limit) {

        List<String> listOfDrivers = handler.getDriverCoordinate(latitude, longitude, radius/EARTHDIAM, limit);
        System.out.println(listOfDrivers);
        return Response.ok(new Gson().toJson(listOfDrivers), MediaType.APPLICATION_JSON).build();
    }
}
