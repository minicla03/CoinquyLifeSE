package com.coinquyteam.houseSelectionApplication.Data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.UUID;

@Document(collection = "Houses")
public class House {

    @Id
    @Field("idhouse")
    private String houseId;

    @Field("name")
    private String houseName;

    @Field("address")
    private String houseAddress;

    public House() {

    }

    public House(String houseCode, String houseName, String houseAddress )
    {
        this.houseId= houseCode;
        this.houseName = houseName;
        this.houseAddress = houseAddress;
    }

    public String getHouseId() {
        return houseId;
    }

    public void setHouseId(String houseId) {
        this.houseId = houseId;
    }

    public String getHouseName() {
        return houseName;
    }

    public void setHouseName(String houseName) {
        this.houseName = houseName;
    }

    public String getHouseAddress() {
        return houseAddress;
    }

    public void setHouseAddress(String houseAddress) {
        this.houseAddress = houseAddress;
    }
}
