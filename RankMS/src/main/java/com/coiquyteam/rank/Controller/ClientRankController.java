package com.coiquyteam.rank.Controller;

import com.coiquyteam.rank.Data.CoinquyPoint;
import com.coiquyteam.rank.Service.RankService;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Path("/client")
public class ClientRankController
{
    @Autowired private RankService rankService;

    @GET
    @Path("/retrieveClassifica")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getClassifica(@QueryParam("houseId") String houseId)
    {
        if (houseId == null)
        {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("houseId mancante").build();
        }

        try
        {
            List<CoinquyPoint> classifica = rankService.getClassificaByHouseId(houseId);
            return Response.ok(classifica).build();
        }
        catch (Exception e)
        {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}
