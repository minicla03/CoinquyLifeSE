package com.coinquyteam.shift.Controller;

import com.coinquyteam.shift.OptaPlanner.CleaningAssignment;
import com.coinquyteam.shift.OptaPlanner.CleaningSchedule;
import com.coinquyteam.shift.Service.CalendarService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

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
            List<CleaningAssignment> shifts = calendarService.getAllShifts(houseId);
            if (shifts == null)
            {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\":\"No cleaning schedule found for the provided house ID\"}")
                        .build();
            }
            return Response.ok(shifts).build();
        }
        catch (Exception e)
        {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Error retrieving cleaning schedule: " + e.getMessage() + "\"}")
                    .build();
        }
    }

    @POST
    @Path("/toRank")
    public Response toRank(@HeaderParam("Authorization") String token, Map<String,String> body)
    {
        if (token == null || token.isEmpty())
        {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"error\":\"Missing or invalid authorization token\"}")
                    .build();
        }

        String cleaningAssignmentId = body.get("cleaningAssignmentId");

        try {
            String response=calendarService.toRank(token,cleaningAssignmentId);
            if(response == null || response.isEmpty()) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity("{\"error\":\"Failed to process the cleaning assignment\"}")
                        .build();
            }
            return Response.ok(response).build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
