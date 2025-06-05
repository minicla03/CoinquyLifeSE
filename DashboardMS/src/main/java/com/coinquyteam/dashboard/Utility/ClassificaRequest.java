package com.coinquyteam.dashboard.Utility;

import java.util.List;

public class ClassificaRequest {
    private String houseId;
    private List<String> coiquyList;

    // Getter e Setter
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
