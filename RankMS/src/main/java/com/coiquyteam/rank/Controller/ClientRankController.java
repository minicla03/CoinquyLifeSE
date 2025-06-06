package com.coiquyteam.rank.Controller;

import com.coiquyteam.rank.Data.Classifica;
import com.coiquyteam.rank.Service.RankService;
import com.coiquyteam.rank.Utility.ClassificaRequest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.LinkedHashMap;
import java.util.List;

@Path("/client")
public class ClientRankController
{
    @Autowired private RankService rankService;

    @POST
    @Path("/retrieveClassifica")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getClassifica(ClassificaRequest request)

    {
        String houseId = request.getHouseId();
        List<String> coinquyList = request.getCoiquyList();

        if (houseId == null || houseId.isEmpty())
        {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"Missing or empty houseId parameter\"}")
                    .build();
        }

        if (coinquyList == null || coinquyList.isEmpty())
        {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"Missing or empty coinquyList parameter\"}")
                    .build();
        }

        try
        {
            LinkedHashMap<String, Classifica> classifica = rankService.getClassifica(coinquyList,houseId);
            return Response.ok(classifica).build();
        }
        catch (Exception e)
        {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}
