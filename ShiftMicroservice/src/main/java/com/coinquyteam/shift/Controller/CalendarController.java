package com.coinquyteam.shift.Controller;

import com.coinquyteam.shift.OptaPlanner.CleaningSchedule;
import com.coinquyteam.shift.Service.CalendarService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
@Path("/calendar")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CalendarController
{
    @Autowired private CalendarService calendarService;

    @GET
    @Path("/getPlanning")
    public Response getPlanning(@HeaderParam("Authorization") String token, String houseId)
    {
        if(token == null || token.isEmpty()) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Authorization token is required").build();
        }

        if(houseId == null || houseId.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("House ID is required").build();
        }

        CleaningSchedule cleaningSchedule;
        try
        {
            cleaningSchedule = calendarService.getSchedule(houseId);
        }
        catch (Exception e)
        {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error retrieving cleaning schedule: " + e.getMessage()).build();
        }
        if (cleaningSchedule == null)
        {
            return Response.status(Response.Status.NOT_FOUND).entity("No cleaning schedule found for the provided house ID").build();
        }
        return Response.ok(cleaningSchedule).build();
    }
}