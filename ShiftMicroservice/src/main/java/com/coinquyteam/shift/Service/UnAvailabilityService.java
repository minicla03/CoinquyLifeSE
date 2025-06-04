package com.coinquyteam.shift.Service;

import com.coinquyteam.shift.Data.Roommate;
import com.coinquyteam.shift.Data.TimeSlot;
import com.coinquyteam.shift.Repository.IRoommateRepository;
import com.coinquyteam.shift.Repository.ITimeSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
public class UnAvailabilityService
{
    @Autowired private ITimeSlotRepository timeSlotRepository;
    @Autowired private IRoommateRepository roommateRepository;
    private final WebClient webClient;

    public UnAvailabilityService(WebClient webClient)
    {
        this.webClient = webClient;
    }

    public boolean associateUnavailabilityWithRoommate(String auth, TimeSlot unavailability, String houseId) throws IllegalArgumentException
    {
        String username = getUsernameFromTokenViaRest(auth.substring(7));
        System.out.println(username);
        if (username == null)
        {
            throw new IllegalArgumentException("Invalid or expired token");
        }

        try
        {
            if(roommateRepository.existsById(username))
            {
                Roommate roommate = roommateRepository.findById(username).get();
                List<TimeSlot> currentUnavailableSlots = (roommateRepository.findById(username)) .get()
                        .getUnavailableTimeSlots();
                currentUnavailableSlots.add(unavailability);
                roommateRepository.save(roommate);
                timeSlotRepository.save(unavailability);
            }
            else
            {
                Roommate newRoommate = new Roommate(username, houseId, List.of(unavailability));
                roommateRepository.save(newRoommate);
            }
        }
        catch (Exception e)
        {
            throw new IllegalArgumentException("Error associating unavailability with roommate: " + e.getMessage());
        }
        return true;
    }

    private String getUsernameFromTokenViaRest(String token) {
        try {
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
}
