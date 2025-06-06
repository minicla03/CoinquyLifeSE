package com.coiquyteam.rank.Service;

import com.coinquyteam.shift.OptaPlanner.CleaningAssignment;
import com.coinquyteam.shift.Repository.ICleaningAssignmentRepository;
import com.coiquyteam.rank.Data.Classifica;
import com.coiquyteam.rank.Data.CoinquyPoint;
import com.coiquyteam.rank.Repository.ICoiquyPointRepository;
import com.coiquyteam.rank.Utility.CoiquyListDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RankService
{
    @Autowired private ICoiquyPointRepository coiquyPointRepository;
    @Autowired private ICleaningAssignmentRepository cleaningAssignmentRepository;

    public boolean updateRank(String cleaningAssignmentId)
    {
        if (cleaningAssignmentId == null || cleaningAssignmentId.isEmpty()) {
            return false;
        }

        try
        {
            CleaningAssignment cleaningAssignment= cleaningAssignmentRepository.findById(cleaningAssignmentId).orElse(null);
            assert cleaningAssignment != null;
            int points= cleaningAssignment.getTask().getTask().getPoints();
            coiquyPointRepository.insert(new CoinquyPoint(cleaningAssignment.getAssignedRoommate().getUsernameRoommate(),
                    cleaningAssignment.getAssignedRoommate().getHouseId(), points));
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    public LinkedHashMap<String, Classifica> getClassifica(List<String> coiquyList, String houseId) throws Exception
    {
        List<CoinquyPoint> coinquyPoints = coiquyPointRepository.findByHouseId(houseId);

        Map<String, Integer> puntiPerUtente = new HashMap<>();

        if (coinquyPoints != null)
        {
            for (CoinquyPoint cp : coinquyPoints)
            {
                puntiPerUtente.put(cp.getICoinquy(),
                        puntiPerUtente.getOrDefault(cp.getICoinquy(), 0) + cp.getPoint());
            }
        }

        Map<String, Classifica> classificaMap = new HashMap<>();

        for (String utente : coiquyList)
        {
            int punti = puntiPerUtente.getOrDefault(utente, 0);
            Classifica c = new Classifica(utente, houseId, punti);
            classificaMap.put(utente, c);
        }

        LinkedHashMap<String, Classifica> sorted = classificaMap.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.comparingInt(Classifica::getTotalPoint).reversed()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
        return sorted;
    }
}
