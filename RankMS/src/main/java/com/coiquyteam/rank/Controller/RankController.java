package com.coiquyteam.rank.Controller;

import com.coiquyteam.rank.Service.RankService;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

@Path("/rank")
@Consumes("application/json")
public class RankController
{
    @Autowired
    private RankService rankService;

    @POST
    @Path("/done")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateRank(Map<String,String> body)
    {
        String cleaningAssignmentId = body.get("cleaningAssignmentId");

        if (cleaningAssignmentId == null || cleaningAssignmentId.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"Invalid cleaning assignment ID\"}")
                    .build();
        }

        try {
            rankService.updateRank(cleaningAssignmentId);
            return Response.ok("{\"message\": \"Punti assegnati\"}").build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Error updating rank: " + e.getMessage() + "\"}")
                    .build();
        }
    }
}
