// Estrae houseId dai parametri della URL e lo salva in localStorage
const urlParams = new URLSearchParams(window.location.search);
const houseId = urlParams.get('houseId');
if (houseId) {
    localStorage.setItem('houseId', houseId);
}

const monthYear = document.getElementById("monthYear");
const calendarDays = document.getElementById("calendarDays");
const prevBtn = document.getElementById("prevMonth");
const nextBtn = document.getElementById("nextMonth");

let date = new Date();

function renderCalendar() {
    const year = date.getFullYear(); // restituisce l'anno
    const month = date.getMonth(); // restituisce il mese (0-11)
    const firstDay = new Date(year, month, 1).getDay(); // restituisce il primo giorno del mese
    const daysInMonth = new Date(year, month + 1, 0).getDate(); // restituisce il numero di giorni nel mese, month+1 perchè il giorno 0 del mese successivo è l'ultimo dell'attuale
    const lastDay = new Date(year, month + 1, 0).getDay(); // restituisce il giorno della settimana dell'ultimo giorno del mese

    const lastMonth_lastDay = new Date(year, month, 0).getDate(); // restituisce il numero di giorni del mese precedente
    const lastMonth_DaysToAdd = firstDay === 0 ? 6 : firstDay - 1; // calcola i giorni da aggiungere del mese precedente

    const nextMonth_DaysToAdd = 42 - (daysInMonth + lastMonth_DaysToAdd); // calcola i giorni da aggiungere del mese successivo per avere sempre 6 righe


    const monthNames = ["Gennaio", "Febbraio", "Marzo", "Aprile", "Maggio", "Giugno", "Luglio",
        "Agosto", "Settembre", "Ottobre", "Novembre", "Dicembre"];

    monthYear.textContent = `${monthNames[month]} ${year}`;
    calendarDays.innerHTML = "";

    // Aggiungi i giorni prima del primo giorno
    for (let i = lastMonth_DaysToAdd - 1; i >= 0; i--) {
        calendarDays.innerHTML += `<div class="prev-days">${lastMonth_lastDay - i}</div>`;
    }

    // Aggiungi i giorni del mese
    for (let day = 1; day <= daysInMonth; day++) {
        calendarDays.innerHTML += `<div class="day">${day}</div>`;
    }
    //aggiunge i giorni del mese successivo
    for (let i = 1; i <= nextMonth_DaysToAdd; i++) {
        calendarDays.innerHTML += `<div class="next-days">${i}</div>`;
    }

}

prevBtn.addEventListener("click", () => {
    date.setMonth(date.getMonth() - 1);
    renderCalendar();
});

nextBtn.addEventListener("click", () => {
    date.setMonth(date.getMonth() + 1);
    renderCalendar();
});

renderCalendar();

// per la navigazione tra le sezioni
document.querySelectorAll(".nav_links a").forEach(link => {
    link.addEventListener("click", async (e) => {

        const section = link.getAttribute("data-section"); // definisci subito section

        if (section === "home") {
            return;
        }

        if(section==="regole"){
            window.location.href = "notYet.html";
            return;
        }

        if(section==="spese") {
            fetch("http://localhost:8080/Dashboard/rest/client/spese", {
                method: "GET",
                headers: {
                    "Authorization": `Bearer ${localStorage.getItem("token")}`,
                    "Accept": "application/json"
                }
            }).then(
                response => {
                    if (!response.ok) {
                        throw new Error(`Errore ${response.status}`);
                    }
                    return response.json();
                }).then(
                data => {
                    if (data.path) {
                        window.location.href = data.path;
                    } else {
                        console.error("Nessuna URL di redirezione fornita.");
                    }
                }
            ).catch(
                err => {
                    console.error("Errore nella navigazione:", err);
                }
            )
            return
        }
        e.preventDefault();

        try {
            const response = await fetch(`http://localhost:8080/Dashboard/rest/client/${section}`, {
                method: "GET",
                headers: {
                    "Authorization": `Bearer ${localStorage.getItem("token")}`,
                    "Accept": "application/json"
                }
            });

            if (!response.ok) {
                throw new Error(`Errore ${response.status}`);
            }

            const data = await response.json();

            if (data.path) {
                window.location.href = data.path;
            } else {
                console.error("Nessuna URL di redirezione fornita.");
            }

        } catch (err) {
            console.error("Errore nella navigazione:", err);
        }
    });
});

document.addEventListener("DOMContentLoaded", () => {
    retrieveCoinquy();
    retriveTurni();
    retrieveClassifica();
});

//TODO: CONTROLLA IL MAPPING OBJ
async function retrieveCoinquy() {
    fetch(`http://localhost:8080/Dashboard/rest/dash/retrieveCoinquy?houseId=${houseId}`,
        {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        })
        .then(response => response.json())
        .then(data => {
            listCoiquy = data;
            localStorage.setItem("listCoiquy", JSON.stringify(listCoiquy));

            const coinquyList = document.getElementById("lista-persone");
            coinquyList.innerHTML = "";

            listCoiquy.forEach(coinquy => {
                const li = document.createElement("li");
                li.className = "coinquy-item";
                li.innerHTML = `
                    <img src="${coinquy.img}" alt="${coinquy.nome} ${coinquy.cognome}">
                    <span>${coinquy.nome} ${coinquy.cognome}</span>
                `;
                coinquyList.appendChild(li);
            });
        })
        .catch(error => console.error('Errore:', error));
}


function retriveTurni() {
    return fetch(`http://localhost:8080/Dashboard/rest/dash/retrieveTurni?houseId=${houseId}`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => {
            if (!response.ok) {
                throw new Error("Errore nel recupero dei turni");
            }
            return response.json();
        })
        .then(data => {
            console.log("Turni:", data);
            // TODO: aggiungi logica per mostrarli nel DOM
        })
        .catch(error => console.error("Errore:", error));
}

function retrieveClassifica(houseId) {
    return fetch(`http://localhost:8080/Dashboard/rest/dash/retrieveClassifica?houseId=${houseId}`, {
        method: "GET",
        headers: {
            "Accept": "application/json"
        }
    })
        .then(response => response.json())
        .then(classificaList => {
            // Ordino in base a punti, decrescente
            classificaList.sort((a, b) => b.punti - a.punti);

            const rankList = document.getElementById("rank");
            rankList.innerHTML = ""; // svuota contenuto precedente

            if (classificaList.length === 0) {
                // Messaggio di nessun dato come <li>
                rankList.innerHTML = "<li>Nessun dato disponibile.</li>";
                return;
            }

            classificaList.forEach(item => {
                const li = document.createElement("li");
                li.textContent = `${item.nome} - ${item.punti} punti`;
                rankList.appendChild(li);
            });
        })
        .catch(error => {
            console.error("Errore nel recupero classifica:", error);
            const rankList = document.getElementById("rank");
            rankList.innerHTML = "Errore nel caricamento della classifica.";
        });
}