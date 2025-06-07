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

    public boolean associateUnavailabilityWithRoommate(String username, TimeSlot unavailability, String houseId) throws IllegalArgumentException
    {
        System.out.println(username);
        if (username == null)
        {
            throw new IllegalArgumentException("Invalid or expired token");
        }

        try
        {
            Roommate roommate = roommateRepository.findById(username).get();
            List<TimeSlot> currentUnavailableSlots = roommate.getUnavailableTimeSlots();
            currentUnavailableSlots.add(unavailability);
            roommateRepository.save(roommate);
            timeSlotRepository.save(unavailability);
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

    public void initializeUnavailability(List<Roommate> roommates) throws IllegalArgumentException
    {
        roommates.forEach(System.out::println);

        for (Roommate roommate : roommates)
        {
            if(!roommateRepository.existsById(roommate.getUsernameRoommate()))
            {
                Roommate newRoommate = new Roommate(roommate.getUsernameRoommate(), roommate.getHouseId(), List.of());
                roommateRepository.insert(newRoommate);
            }
        }
    }
}
