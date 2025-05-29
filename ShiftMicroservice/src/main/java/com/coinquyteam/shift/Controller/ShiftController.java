package com.coinquyteam.shift.Controller;

import com.coinquyteam.shift.Data.SwapRequest;
import com.coinquyteam.shift.Data.TimeSlot;
import com.coinquyteam.shift.Service.ShiftService;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;

@Path("/shift")
public class ShiftController
{
    @Autowired
    private ShiftService shiftService;

    @POST
    @Path("/unavaible")
    public Response unavaibleShift(TimeSlot shiftRequest)
    {
        return shiftService.unavaibleShift(shiftRequest);
    }

    @POST
    @Path("swapShift")
    public Response swapShift(SwapRequest shiftRequest)
    {
        return shiftService.swapShift(shiftRequest);
    }

    /* TODO:
        🧹 Task di Pulizia
        GET /api/tasks Ottieni tutti i compiti di pulizia
        POST /api/tasks Crea un nuovo compito
        GET /api/tasks/:id Ottieni i dettagli di un compito
        PUT /api/tasks/:id Modifica un compito
        DELETE /api/tasks/:id Elimina un compito
        ⛔ Indisponibilità
        POST /api/availability
        Aggiungi indisponibilità di un coinquilino
        GET /api/availability/:roommateId
        Ottieni le indisponibilità per coinquilino
        🔄 Richieste di Scambio
        POST /api/swaps
        Crea una nuova richiesta di scambio
        GET /api/swaps/sent/:userId
        Ottieni le richieste inviate da un utente
        GET /api/swaps/received/:userId
        Ottieni le richieste ricevute da un utente
        PUT /api/swaps/:id/accept
        Accetta una richiesta di scambio
        PUT /api/swaps/:id/reject
        Rifiuta una richiesta di scambio
        🗓️ Calendario
        GET /api/calendar
        Ottieni i turni assegnati
        👤 Coinquilini
        GET /api/roommates
        Elenco coinquilini
        GET /api/roommates/:id
        Dettaglio coinquilino*/
}
