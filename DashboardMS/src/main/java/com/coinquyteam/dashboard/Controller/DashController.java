package com.coinquyteam.dashboard.Controller;

import com.coinquyteam.dashboard.Service.DashService;
import com.coinquyteam.dashboard.Utility.Classifica;
import com.coinquyteam.dashboard.Utility.ClassificaRequest;
import com.coinquyteam.dashboard.Utility.CoiquyListDTO;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Path("/dash")
@Produces(MediaType.APPLICATION_JSON)
public class DashController
{
    @Autowired
    private DashService dashService;

    @GET
    @Path("/retrieveCoinquy")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCoinquy(@QueryParam("houseId") String houseId) {
        return Response.ok(dashService.getCoinquy(houseId)).build();
    }

    @GET
    @Path("/retrieveTurni")
    public Response getTurni(@QueryParam("houseId") String houseId)
    {
        return Response.ok(dashService.getTurni(houseId)).build();
    }

    @POST
    @Path("/retrieveClassifica")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getClassifica(CoiquyListDTO coiquyListDTO)
    {
        System.out.println(coiquyListDTO);

        try{
            Map<String, Classifica> classifica = dashService.getClassifica(coiquyListDTO);
            return Response.ok(classifica).build();
        }
        catch (Exception e)
        {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Errore: " + e.getMessage())
                    .build();
        }
    }
}
