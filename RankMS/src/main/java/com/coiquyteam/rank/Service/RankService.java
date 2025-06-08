package com.coiquyteam.rank.Service;

import com.coiquyteam.rank.Data.Classifica;
import com.coiquyteam.rank.Data.CoinquyPoint;
import com.coiquyteam.rank.Data.TaskCategory;
import com.coiquyteam.rank.Repository.ICoiquyPointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RankService
{
    @Autowired private ICoiquyPointRepository coiquyPointRepository;

    public boolean updateRank(String username, String typeTask, String houseId, String dateComplete, String endTime)
    {
        try
        {
            LocalDateTime dateTimeComplete = LocalDateTime.parse(dateComplete);
            LocalDateTime endTimeParsed = LocalDateTime.parse(endTime);
            int points=TaskCategory.fromString(typeTask).getPoints();
            System.out.println("POINTS"+points);
            if (dateTimeComplete.isAfter(endTimeParsed))
            {
                points= TaskCategory.fromString(typeTask).getPenalityPoints();
            }
            System.out.println("POINTS"+points);
            coiquyPointRepository.insert(new CoinquyPoint(username, houseId, points));
            System.out.println("DOPO INSERT");
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
