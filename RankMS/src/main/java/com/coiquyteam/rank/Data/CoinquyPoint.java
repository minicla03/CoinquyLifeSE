package com.coiquyteam.rank.Data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document("CoiquyPoint")
public class CoinquyPoint
{
    @Id
    @Field("CoiquyPointId") private String _id;
    @Field("idCoinquy") private String iCoinquy;
    @Field("houseId") private String houseId;
    @Field("point") private int point;

    public CoinquyPoint() { }

    public CoinquyPoint(String iCoinquy, String houseId, int point)
    {
        this.iCoinquy = iCoinquy;
        this.point = point;
        this.houseId = houseId;
    }

    public String getId()
    {
        return this._id;
    }

    public void setId(String id)
    {
        this._id = id;
    }

    public String getICoinquy()
    {
        return this.iCoinquy;
    }

    public String getHouseId()
    {
        return this.houseId;
    }

    public void setHouseId(String houseId)
    {
        this.houseId = houseId;
    }

    public void setICoinquy(String iCoinquy)
    {
        this.iCoinquy = iCoinquy;
    }

    public int getPoint()
    {
        return this.point;
    }

    public void setPoint(int point)
    {
        this.point = point;
    }
}
