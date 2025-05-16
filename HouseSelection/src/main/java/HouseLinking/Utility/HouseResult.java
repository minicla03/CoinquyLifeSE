package HouseLinking.Utility;

public class HouseResult
{
    private HouseStatus houseStatus;
    private String message;

    public HouseResult(HouseStatus houseStatus, String message) {
        this.houseStatus = houseStatus;
        this.message = message;
    }

    public HouseStatus getHouseStatus() {
        return houseStatus;
    }

    public void setHouseStatus(HouseStatus houseStatus) {
        this.houseStatus = houseStatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}