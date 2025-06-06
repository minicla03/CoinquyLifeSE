package com.coiquyteam.rank.Utility;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ClassificaRequest
{
    @JsonProperty("houseId")
    private String houseId;

    @JsonProperty("coiquyList") 
    private List<String> coiquyList;

    public ClassificaRequest() { }

    public ClassificaRequest(String houseId, List<String> coiquyList) {
        this.houseId = houseId;
        this.coiquyList = coiquyList;
    }

    public String getHouseId() {
        return houseId;
    }

    public void setHouseId(String houseId) {
        this.houseId = houseId;
    }

    public List<String> getCoiquyList() {
        return coiquyList;
    }

    public void setCoiquyList(List<String> coiquyList) {
        this.coiquyList = coiquyList;
    }
}
