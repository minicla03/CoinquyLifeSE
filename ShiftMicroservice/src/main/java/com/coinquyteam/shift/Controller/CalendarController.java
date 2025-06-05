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
import java.util.UUID;

@Path("/calendar")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CalendarController
{
    @Autowired private CalendarService calendarService;

    @POST
    @Path("/getPlanning")
    public Response getPlanning(Map<String,String> body)
    {
        String houseId = body.get("houseId");
        String problemId = body.get("problemId");

        if(problemId!=null)
        {
            if(calendarService.planningExists(problemId, houseId))
            {
                List<CleaningAssignment> existingPlanning=calendarService.retriveCleaningAssignments(UUID.fromString(problemId), houseId);
                return Response.status(Response.Status.FOUND).entity(existingPlanning).build();
            }
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
        return Response.ok(cleaningSchedule.getAssignmentList()).build();
    }

    @PUT
    @Path("/taskDone")
    public Response taskDone(Map<String,String> body)
    {
        String id = body.get("id");
        if (id == null || id.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid task ID").build();
        }

        try
        {
            calendarService.markTaskAsDone(id);
        }
        catch (Exception e)
        {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error marking task as done: " + e.getMessage()).build();
        }
        return Response.ok().entity("Task marked as done successfully").build();
    }

    @POST
    @Path("/getAllShifts")
    public Response retrieveAllShifts(Map<String,String> body)
    {
        String houseId = body.get("houseId");
        if (houseId == null || houseId.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("House ID is required").build();
        }

        try
        {
            List<CleaningAssignment> shifts = calendarService.getAllShifts(houseId);
            return Response.ok(shifts).build();
        }
        catch (Exception e)
        {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error retrieving shifts: " + e.getMessage()).build();
        }
    }
}