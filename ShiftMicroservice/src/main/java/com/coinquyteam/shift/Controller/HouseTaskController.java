package com.coinquyteam.shift.Controller;

import com.coinquyteam.shift.Data.HouseTask;
import com.coinquyteam.shift.Service.HouseTaskService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.Map;


@Path("/tasks")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class HouseTaskController
{
    @Autowired
    private HouseTaskService houseTaskService;

    @POST
    @Path("/create")
    public Response createTask(@HeaderParam("Authorization") String auth, HouseTask cleaningTask)
    {
        if (auth == null || auth.isEmpty()) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        if (cleaningTask == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("HouseTask cannot be null").build();
        }

        String houseId=cleaningTask.getHouseId();

        if (houseId == null || houseId.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("House ID is required").build();
        }

        try
        {
            houseTaskService.createTask(cleaningTask, houseId);
            return Response.status(Response.Status.CREATED).entity("Task created successfully").build();
        }
        catch (Exception e)
        {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error creating task: " + e.getMessage()).build();
        }

    }

}
