package com.coiquyteam.rank.Service;

import com.coinquyteam.shift.OptaPlanner.CleaningAssignment;
import com.coiquyteam.rank.Data.CoinquyPoint;
import com.coiquyteam.rank.Repository.ICoiquyPointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RankService
{
    @Autowired
    private ICoiquyPointRepository coiquyPointRepository;

    public boolean updateRank(CleaningAssignment cleaningAssignment)
    {
        String username = cleaningAssignment.getAssignedRoommate().getUsernameRoommate();
        int point = cleaningAssignment.getTask().getTaskCategory().getPoints();
        String houseId = cleaningAssignment.getAssignedRoommate().getHouseId();

        try
        {
            coiquyPointRepository.insert(new CoinquyPoint(username, houseId, point));
            return true;
        }
        catch (Exception e)
        {
            return false;

        }
    }

    public List<CoinquyPoint> getClassificaByHouseId(String houseId) throws Exception
    {
        return coiquyPointRepository.findByHouseId(houseId);
    }
}
