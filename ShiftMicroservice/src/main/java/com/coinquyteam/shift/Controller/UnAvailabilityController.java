package com.coinquyteam.shift.Controller;

import com.coinquyteam.shift.Data.TimeSlot;
import com.coinquyteam.shift.Service.UnAvailabilityService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Map;

@Path("/unAvailability")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UnAvailabilityController
{
    @Autowired
    private UnAvailabilityService unAvailabilityService;

    @GET
    @Path("/prova")
    public Response prova()
    {
        return Response.ok("Unavailability service is working!").build();
    }

    @POST
    @Path("/addAvailability")
    public Response addAvailability(@HeaderParam("Authorization") String auth, Map<String,String> body)
    {
        String houseId = body.get("houseId");
        LocalDateTime start = LocalDateTime.parse(body.get("start"));
        LocalDateTime end = LocalDateTime.parse(body.get("end"));
        TimeSlot ts= new TimeSlot(start, end);

        if (houseId == null)
        {
            return Response.status(Response.Status.BAD_REQUEST).entity("House ID, start time, and end time are required").build();
        }

        try
        {
            if(unAvailabilityService.associateUnavailabilityWithRoommate(auth, ts, houseId))
            {
                return Response.status(Response.Status.CREATED).entity("Unavailability added successfully").build();
            }
            else
            {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to add unavailability").build();
            }

        }
        catch (IllegalArgumentException e)
        {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }

    }
}