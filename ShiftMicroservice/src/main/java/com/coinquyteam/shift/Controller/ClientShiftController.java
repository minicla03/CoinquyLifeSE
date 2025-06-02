package com.coinquyteam.shift.Controller;

import com.coinquyteam.shift.OptaPlanner.CleaningSchedule;
import com.coinquyteam.shift.Service.CalendarService;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
@Path("/client")
public class ClientShiftController
{
    @Autowired
    private CalendarService calendarService;

    @GET
    @Path("/retrieveShifts")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getShifts(@QueryParam("houseId") String houseId)
    {
        if (houseId == null || houseId.isEmpty())
        {
            return Response.status(Response.Status.BAD_REQUEST)
            .entity("{\"error\":\"Missing or empty houseId parameter\"}")
            .build();
        }

        try
        {
            CleaningSchedule cleaningSchedule = calendarService.getSchedule(houseId);
            if (cleaningSchedule == null)
            {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\":\"No cleaning schedule found for the provided house ID\"}")
                        .build();
            }
            return Response.ok(cleaningSchedule).build();
        }
        catch (Exception e)
        {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Error retrieving cleaning schedule: " + e.getMessage() + "\"}")
                    .build();
        }
    }
}
