package com.coinquyteam.shift.Controller;

import com.coinquyteam.shift.Data.SwapRequest;
import com.coinquyteam.shift.Repository.ISwapRequestRepository;
import com.coinquyteam.shift.Service.SwapService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@Path("/swaps")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SwapController
{
    @Autowired private SwapService swapService;
    @Autowired
    private ISwapRequestRepository iSwapRequestRepository;

    @POST
    @Path("/createSwapRequest")
    public Response createSwapRequest(@HeaderParam("Authorization") String auth, SwapRequest swapRequest)
    {
        if(auth==null)
        {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Unauthorized").build();
        }

        try
        {
            swapService.createSwapRequest(auth, swapRequest);
            return Response.ok("Swap request processed successfully.").build();
        }
        catch (Exception e)
        {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error processing swap request: " + e.getMessage()).build();
        }
    }

    @GET
    @Path("/getSwapRequests")
    public Response getSwapRequests(@HeaderParam("Authorization") String auth, String houseId)
    {
        if(auth==null)
        {
            throw new WebApplicationException("Unauthorized", Response.Status.UNAUTHORIZED);
        }

        if(houseId==null)
        {
            throw new WebApplicationException("Roommate ID is required", Response.Status.BAD_REQUEST);
        }

        List<SwapRequest> swapRequests = swapService.getSwapRequestsRelatedToHouse(auth,houseId);
        if(swapRequests == null || swapRequests.isEmpty())
        {
            return Response.status(Response.Status.NOT_FOUND).entity("No swap requests found for the specified roommate.").build();
        }
        return Response.ok(swapRequests).build();
    }

    @PUT
    @Path("/{idSwap}/accept")
    public Response acceptRequest(@HeaderParam("Authorization") String auth, @PathParam("idSwap") String idSwap)
    {
        if(auth==null)
        {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Unauthorized").build();
        }

        if(idSwap==null)
        {
            return Response.status(Response.Status.BAD_REQUEST).entity("Swap ID is required").build();
        }


        try
        {
            if(swapService.accept(idSwap))
            {
                return Response.ok("Swap request accepted successfully.").build();
            }
            else
            {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error accepting swap request.").build();

            }
        }
        catch (Exception e)
        {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error accepting swap request: " + e.getMessage()).build();
        }

    }

    @PUT
    @Path("/{idSwap}/reject")
    public Response rejectRequest(@HeaderParam("Authorization") String auth, @PathParam("idSwap") String idSwap)
    {
        if(auth==null)
        {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Unauthorized").build();
        }

        if(idSwap==null)
        {
            return Response.status(Response.Status.BAD_REQUEST).entity("Swap ID is required").build();
        }

        try
        {
            swapService.reject(idSwap);
            return Response.ok("Swap request processed successfully.").build();
        }
        catch (Exception e)
        {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error rejecting swap request: " + e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{idSwap}/delete")
    public Response deleteRequest(@HeaderParam("Authorization") String auth, @PathParam("idSwap") String idSwap)
    {
        if(auth==null)
        {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Unauthorized").build();
        }

        if(idSwap==null)
        {
            return Response.status(Response.Status.BAD_REQUEST).entity("Swap ID is required").build();
        }

        try
        {
            swapService.deleteSwapRequest(idSwap);
            return Response.ok("Swap request deleted successfully.").build();
        }
        catch (Exception e)
        {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error deleting swap request: " + e.getMessage()).build();
        }
    }
}
