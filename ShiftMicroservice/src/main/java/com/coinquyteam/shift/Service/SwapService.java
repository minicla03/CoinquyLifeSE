package com.coinquyteam.shift.Service;

import com.coinquyteam.shift.Data.StatusSwap;
import com.coinquyteam.shift.Data.SwapRequest;
import com.coinquyteam.shift.Repository.ISwapRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class SwapService
{
    @Autowired private ISwapRequestRepository swapRequestRepository;
    @Autowired private WebClient webClient;

    public void createSwapRequest(String auth, SwapRequest swapRequest) throws Exception {
        String token = auth.substring(7);
        String username = getUsernameFromTokenViaRest(token);

        if (username == null) {
            throw new Exception("Invalid token");
        }

        try
        {
            swapRequestRepository.insert(swapRequest);
        }
        catch (Exception e)
        {
            throw new Exception("Error creating swap request: " + e.getMessage());
        }
    }

    private String getUsernameFromTokenViaRest(String token)
    {
        try
        {
            Map<String, String> response = webClient.post()
                    .uri("http://localhost:8080/gateway/verify-token")
                    .bodyValue(Map.of("token", token))
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            return response != null ? response.get("username") : null;
        } catch (Exception e) {
            return null;
        }
    }


    public List<SwapRequest> getSwapsByHouseId(String houseId)
    {
        try
        {
            return swapRequestRepository.findAllByHouseId(houseId);
        }
        catch (Exception e)
        {
            System.err.println("Error retrieving swaps for house ID " + houseId + ": " + e.getMessage());
            return List.of();
        }
    }

    public void acceptSwap(String idSwap) throws Exception
    {
        SwapRequest swapRequest = swapRequestRepository.findById(idSwap).orElseThrow(() -> new Exception("Swap request not found"));

        var assignmentA = swapRequest.getAssignmentA();
        var assignmentB = swapRequest.getAssignmentB();

        var tempRoommate = assignmentA.getAssignedRoommate();
        assignmentA.setAssignedRoommate(assignmentB.getAssignedRoommate());
        assignmentB.setAssignedRoommate(tempRoommate);

        swapRequest.setAcceptedByB(StatusSwap.ACCEPTED);

        swapRequestRepository.save(swapRequest);
    }


    public void rejectSwap(String idSwap)
    {
        try
        {
            Objects.requireNonNull(swapRequestRepository.findById(idSwap).orElse(null)).setAcceptedByB(StatusSwap.REJECTED);
        }
        catch (Exception e)
        {
            throw new RuntimeException("Error accepting swap ID " + idSwap + ": " + e.getMessage());

        }
    }

    public void deleteSwapRequest(String idSwap)
    {
        try
        {
            swapRequestRepository.deleteById(idSwap);
        }
        catch (Exception e)
        {
            System.err.println("Error deleting swap request by ID " + idSwap + ": " + e.getMessage());
        }
    }

    public List<SwapRequest> getSwapRequestsRelatedToHouse(String auth, String houseId)
    {
        String token = auth.substring(7);
        String usernameRoommate = getUsernameFromTokenViaRest(token);

        try
        {
            return swapRequestRepository.findAllByHouseId(houseId).stream()
                    .filter(swapRequest -> swapRequest.getAssignmentA().getAssignedRoommate().getUsernameRoommate().equals(usernameRoommate) ||
                            swapRequest.getAssignmentB().getAssignedRoommate().getUsernameRoommate().equals(usernameRoommate))
                    .toList();
        }
        catch (Exception e)
        {
            System.err.println("Error retrieving swap requests related to roommate ID " + usernameRoommate + ": " + e.getMessage());
            return List.of();
        }
    }
}
