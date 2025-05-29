package com.coinquyteam.shift.Controller;

import com.coinquyteam.shift.Data.TimeSlot;
import com.coinquyteam.shift.Service.UnAvailabilityService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;

@Path("/unAvailability")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UnAvailabilityController
{
    @Autowired
    private UnAvailabilityService unAvailabilityService;

    @POST
    @Path("/addAvailability")
    public Response addAvailability(@HeaderParam("Authorization") String auth, TimeSlot unavailability, String houseId)
    {
        if (unavailability == null || unavailability.getStart() == null || unavailability.getEnd() == null)
        {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid time slot data").build();
        }

        if (unavailability.getStart().isAfter(unavailability.getEnd()))
        {
            return Response.status(Response.Status.BAD_REQUEST).entity("Start time must be before end time").build();
        }

        try
        {
            if(unAvailabilityService.associateUnavailabilityWithRoommate(auth, unavailability, houseId))
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