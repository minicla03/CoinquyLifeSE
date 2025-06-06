package com.coinquyteam.shift.Data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Document("Roommates")
public class Roommate
{
    @Id private String usernameRoommate;
    @Field("houseId") private String houseId;
    @Field("UnavaibleTimeSlots") private List<TimeSlot> unavailableTimeSlots;

    public Roommate() {}

    public Roommate(String usernameRoommate, String houseId,List<TimeSlot> unavailableTimeSlots) {
        this.usernameRoommate = usernameRoommate;
        this.houseId = houseId;
        this.unavailableTimeSlots = unavailableTimeSlots;
    }

    public String getUsernameRoommate() { return this.usernameRoommate; }
    public void setUsernameRoommate(String idRoommate) { this.usernameRoommate = idRoommate; }

    public String getHouseId() { return this.houseId; }
    public void setHouseId(String houseId) { this.houseId = houseId; }

    public List<TimeSlot> getUnavailableTimeSlots() { return unavailableTimeSlots; }
    public void setUnavailableTimeSlots(List<TimeSlot> unavailableTimeSlots) { this.unavailableTimeSlots = unavailableTimeSlots; }

    @Override
    public String toString() {
        return "Roommate{" +
                ", usernameRoommate='" + usernameRoommate + '\'' +
                ", houseId='" + houseId + '\'' +
                ", unavailableTimeSlots=" + unavailableTimeSlots +
                '}';
    }
}
