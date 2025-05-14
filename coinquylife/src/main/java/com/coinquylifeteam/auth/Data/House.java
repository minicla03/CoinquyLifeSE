package com.coinquylifeteam.auth.Data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "Houses")
public class House {

    @Id
    private String houseId;

    @Field("name")
    private String houseName;

    @Field("address")
    private String houseAddress;

    @Field("code")
    private String houseCode;

    public House() {

    }

    public House( String houseName, String houseAddress ) {
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

    public String getHouseCode() {
        return houseCode;
    }

    public void setHouseCode(String houseCode) {
        this.houseCode = houseCode;
    }




}
