package com.coiquyteam.rank.Controller;

import com.coiquyteam.rank.Service.RankService;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

@Path("/rank")
public class RankController
{
    @Autowired
    private RankService rankService;

    @POST
    @Path("/done")
    public Response updateRank(Map<String,String> body)
    {
        String cleaningAssignmentId = body.get("cleaningAssignmentId");
        if (cleaningAssignmentId == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid cleaning assignment").build();
        }

        try
        {
            rankService.updateRank(cleaningAssignmentId);
            return Response.ok("Punti assegnati").build();
        }
        catch (Exception e)
        {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error updating rank: " + e.getMessage()).build();
        }
    }
}
