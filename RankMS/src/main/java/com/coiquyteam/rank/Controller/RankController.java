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
        String username = body.get("username");
        String typeTask = body.get("typeTask");
        String houseId = body.get("houseId");
        String dateComplete= body.get("dateComplete");
        String endTime = body.get("endTime");

        if(username == null || username.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"Username is required\"}")
                    .build();
        }

        if(houseId == null || houseId.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"House ID is required\"}")
                    .build();
        }

        try {
            System.out.println("[RankController] Updating rank for user: " + username);
            rankService.updateRank(username, typeTask, houseId, dateComplete, endTime);
            System.out.println("[RankController] Rank updated successfully");

            return Response.ok("{\"message\": \"Punti assegnati\"}").build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Error updating rank: " + e.getMessage() + "\"}")
                    .build();
        }
    }
}
