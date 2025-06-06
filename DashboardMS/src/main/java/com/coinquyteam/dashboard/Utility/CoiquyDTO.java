package com.coinquyteam.dashboard.Utility;

public class CoiquyDTO
{
    private String username;
    private String houseId;

    // Getter e Setter
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getHouseId() {
        return houseId;
    }
    public void setHouseId(String houseId) {
        this.houseId = houseId;
    }

    @Override
    public String toString() {
        return "CoiquyDTO{" +
                "username='" + username + '\'' +
                ", houseId='" + houseId + '\'' +
                '}';
    }
}
