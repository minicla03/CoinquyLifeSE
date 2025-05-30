package com.coiquyteam.rank.Service;

import com.coinquyteam.shift.OptaPlanner.CleaningAssignment;
import com.coiquyteam.rank.Data.CoinquyPoint;
import com.coiquyteam.rank.Repository.ICoiquyPointRepository;

public class RankService
{
    private ICoiquyPointRepository coiquyPointRepository;

    public boolean updateRank(CleaningAssignment cleaningAssignment)
    {
        String username = cleaningAssignment.getAssignedRoommate().getUsernameRoommate();
        int point = cleaningAssignment.getTask().getTaskCategory().getPoints();

        try
        {
            coiquyPointRepository.insert(new CoinquyPoint(username, point));
            return true;
        }
        catch (Exception e)
        {
            return false;

        }
    }
}
