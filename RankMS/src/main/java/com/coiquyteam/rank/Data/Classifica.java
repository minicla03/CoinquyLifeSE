package com.coiquyteam.rank.Data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document("Classifica")
public class Classifica
{
    @Id @Field("ClassificaId") private String _id;
    @Field("idCoiquy") private String idCoinquy;
    @Field("houseId") private String houseId;
    @Field("totalPoint") private int totalPoint;

    public Classifica() { }

    public Classifica(String idCoinquy, String houseId, int point) {
        this.idCoinquy = idCoinquy;
        this.houseId = houseId;
        this.totalPoint = point;
    }

    public String getIdCoinquy() {
        return idCoinquy;
    }

    public void setIdCoinquy(String idCoinquy) {
        this.idCoinquy = idCoinquy;
    }

    public String getHouseId() {
        return houseId;
    }

    public void setHouseId(String houseId) {
        this.houseId = houseId;
    }

    public int getTotalPoint() {
        return totalPoint;
    }

    public void setTotalPoint(int totalPoint) {
        this.totalPoint = totalPoint;
    }

}
