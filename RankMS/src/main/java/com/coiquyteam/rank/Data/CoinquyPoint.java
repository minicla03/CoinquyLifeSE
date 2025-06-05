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
    @Field("point") private Integer point;

    public CoinquyPoint() { }

    public CoinquyPoint(String iCoinquy, String houseId, Integer point)
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

    public Integer getPoint()
    {
        return this.point;
    }

    public void setPoint(Integer point)
    {
        this.point = point;
    }
}
