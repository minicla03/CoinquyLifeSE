package com.coinquyteam.authApplication.Controller;

import com.coinquyteam.authApplication.Service.WebAuthClientService;
import com.coinquyteam.authApplication.Utility.AuthResult;
import com.coinquyteam.authApplication.Utility.StatusAuth;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Controller
@Path("/auth/external")
@Consumes("application/json")
public class WebAuthController
{
    @Autowired
    private WebAuthClientService webAuthClientService;

    @POST
    @Path("/link-house")
    public Response linkHouseToUser(@HeaderParam("Authorization") String auth, Map<String, String> body)
    {
        String token = auth.substring(7);
        String houseCode = body.get("houseCode");

        if (houseCode == null)
        {
            return Response.status(Response.Status.BAD_REQUEST).entity("Token and house code are required").build();
        }

        AuthResult authResult = webAuthClientService.linkHouseToUser(token, houseCode);
        if (authResult.getStatusAuth() == StatusAuth.SUCCESS)
        {
            return Response.ok("{\"message\":\"House linked successfully\"}").build();
        }
        else if (authResult.getStatusAuth() == StatusAuth.USER_NOT_FOUND)
        {
            return Response.status(Response.Status.NOT_FOUND).entity("User not found").build();
        }
        else
        {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Server error").build();
        }

    }
}
