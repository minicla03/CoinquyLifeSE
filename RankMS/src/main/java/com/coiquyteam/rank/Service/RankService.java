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
            System.out.println("INSIDE UPDATE RANK");
            System.out.println("USERNAME: " + username);
            System.out.println("TYPE TASK: " + typeTask);
            System.out.println("HOUSE ID: " + houseId);
            System.out.println("DATE COMPLETE: " + dateComplete);
            System.out.println("END TIME: " + endTime);
            LocalDateTime dateTimeComplete;
            LocalDateTime endTimeParsed;
            try {
                dateTimeComplete = LocalDateTime.parse(dateComplete);
                endTimeParsed = LocalDateTime.parse(endTime);
            } catch (Exception e) {
                System.err.println("Errore nel parsing delle date: " + e.getMessage());
                return false;
            }

            TaskCategory taskCategory = TaskCategory.fromString(typeTask);
            int points = dateTimeComplete.isAfter(endTimeParsed)
                    ? taskCategory.getPenalityPoints()
                    : taskCategory.getPoints();
            System.out.println("Punti assegnati: " + points);
            CoinquyPoint cp= new CoinquyPoint(username, houseId, points);
            coiquyPointRepository.insert(cp);
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
