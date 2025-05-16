package Controller;

import Client.WebAuthClient;
import Utility.AuthResult;
import Utility.StatusAuth;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
@Path("/auth/external")
public class WebAuthController
{
    @Autowired
    private WebAuthClient webAuthClient;

    @POST
    @Path("/link-house")
    public Response linkHouseToUser(String token, String houseCode)
    {
        AuthResult authResult = webAuthClient.linkHouseToUser(token, houseCode);
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
