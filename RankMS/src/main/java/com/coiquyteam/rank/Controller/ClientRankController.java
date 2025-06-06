package com.coiquyteam.rank.Controller;

import com.coiquyteam.rank.Data.Classifica;
import com.coiquyteam.rank.Service.RankService;
import com.coiquyteam.rank.Utility.ClassificaRequest;
import com.coiquyteam.rank.Utility.CoiquyDTO;
import com.coiquyteam.rank.Utility.CoiquyListDTO;
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
    public Response getClassifica(CoiquyListDTO request)
    {
        String houseId = request.getCoiquyList().getFirst().getHouseId();
        List<String> coiquyList = request.getCoiquyList().stream().map(CoiquyDTO::getUsername).toList();

        System.out.println(coiquyList);
        System.out.println(houseId);

        if (houseId == null || houseId.isEmpty())
        {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"Missing or empty houseId parameter\"}")
                    .build();
        }

        if (coiquyList.isEmpty())
        {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"Missing or empty coiquyList parameter\"}")
                    .build();
        }

        try
        {
            LinkedHashMap<String, Classifica> classifica = rankService.getClassifica(coiquyList,houseId);
            return Response.ok(classifica).build();
        }
        catch (Exception e)
        {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}
