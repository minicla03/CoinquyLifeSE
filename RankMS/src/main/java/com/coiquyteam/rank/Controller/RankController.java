package com.coiquyteam.rank.Controller;

import com.coinquyteam.shift.OptaPlanner.CleaningAssignment;
import com.coiquyteam.rank.Service.RankService;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;

@Path("/rank")
public class RankController
{
    @Autowired
    private RankService rankService;

    @POST
    @Path("/done")
    public Response updateRank(@HeaderParam("Authorization") String auth, CleaningAssignment cleaningAssignment)
    {
        if (auth == null || auth.isEmpty())
        {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        if (cleaningAssignment == null || cleaningAssignment.getId() == null)
        {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid cleaning assignment").build();
        }

        try
        {
            rankService.updateRank(cleaningAssignment);
            return Response.ok().build();
        }
        catch (Exception e)
        {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error updating rank: " + e.getMessage()).build();
        }
    }
}
