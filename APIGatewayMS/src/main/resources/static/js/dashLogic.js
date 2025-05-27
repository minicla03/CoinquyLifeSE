
/*Logica per il profilo utente*/

const utente = {
    immagineProfilo: "user-solid.svg"
};

const userLink = document.getElementById('user');

// Crea l'immagine
const img = document.createElement('img');
img.src = utente.immagineProfilo;
img.className = 'profile-img';

// Pulisce il contenuto esistente (opzionale)
userLink.innerHTML = '';

// Inserisce immagine
userLink.appendChild(img);

/*Logica per il calendario*/

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


/*Logica per la classifica*/

const classifica = [
    {
        nome: "Alice",
        punti: 120,
        img: "user-solid.svg"
    },
    {
        nome: "Charlie",
        punti: 95,
        img: "user-solid.svg"
    },
    {
        nome: "Bob",
        punti: 110,
        img: "user-solid.svg"
    },
    {
        nome: "Dario",
        punti: 80,
        img: "user-solid.svg"
    },
];


function mostraClassifica(data) {
    const container = document.getElementById("classificaBox");

    const ul = document.createElement("ul");
    ul.classList.add("classifica-list");

    const topPlayers = data
        .sort((a, b) => b.punti - a.punti)
        .slice(0, 4);

    topPlayers.forEach((item, index) => {
        const li = document.createElement("li");
        li.innerHTML = `
      <img src="${item.img}" alt="${item.nome}" class="avatar">
      <div class="info">
        <strong>#${index + 1}</strong> ${item.nome} - ${item.punti} pts
      </div>
    `;
        ul.appendChild(li);
    });

    container.appendChild(ul);
}

mostraClassifica(classifica);

/*funzione per mostrare il riepilogo della casa*/

document.addEventListener("DOMContentLoaded", async () => {
    const navLinks = document.querySelectorAll('.nav_links a');

    navLinks.forEach(link => {
        link.addEventListener('click', (e) => {
            e.preventDefault(); // blocca comportamento standard

            const text = link.textContent.trim().toLowerCase();

            navLinks.forEach(link => {
                link.addEventListener('click', (e) => {
                    e.preventDefault();
                    const text = link.textContent.trim().toLowerCase();
                    switch (text) {
                        case 'spese': redirect("spese"); break;
                        case 'turni': redirect("turni"); break;
                        case 'regole': redirect("regole"); break;
                        case 'classifica': redirect("classifica"); break;
                        case 'profile': redirect("profilo"); break;
                        default: console.warn('Link non gestito:', text);
                    }
                });
            });
        });
    });
    await caricaEventi();
    await caricaPersoneInCasa();
});

async function redirect(path)
{
    const token = localStorage.getItem("token"); // o da sessionStorage, ecc.

    if (!token)
    {
        alert("Token non trovato. Assicurati di essere autenticato.");
        return;
    }

    try {
        const response = await fetch(`http://localhost:8083/DashBard/rest/client/${path}`, {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                "Authorization": "Bearer " + token,
            },
        });

        if (!response.ok)
        {
            throw new Error("Errore: " + response.statusText);
        }

        const data = await response.json();
        const link = data["path"];

        if (link)
        {
            window.location.href = link;
        }
        else
        {
            alert("Errore: 'path' non trovato nella risposta");
        }
    }
    catch (err)
    {
        alert(err.message);
    }
}

const coinquilini = [];
const houseId = localStorage.getItem("houseId");

async function caricaEventi()
{
    const listaEventi = document.getElementById("lista-eventi");
    listaEventi.innerHTML = "";

    try
    {
        const response = await fetch("http://localhost:8083/DashBard/rest/house/events");
        const eventi = await response.json();

        if (eventi.length === 0) {
            const li = document.createElement("li");
            li.textContent = "Nessun evento presente.";
            li.className = "messaggio-vuoto";
            listaEventi.appendChild(li);
            return;
        }

        eventi.forEach(evento => {
            const li = document.createElement("li");
            li.className = "events-item";

            li.innerHTML = `
                <strong>${evento.titolo}</strong>
                <span>Creato da: ${evento.autore.nome} ${evento.autore.cognome}</span>
                <span>Data: ${new Date(evento.data).toLocaleDateString()}</span>
            `;

            listaEventi.appendChild(li);
        });
    } catch (err) {
        console.error("Errore nel recupero eventi:", err);
        listaEventi.innerHTML = "<li class='messaggio-vuoto'>Errore nel caricamento degli eventi.</li>";
    }
}

async function caricaPersoneInCasa() {

    const lista = document.getElementById("lista-persone");
    lista.innerHTML = "";

    try{
        await fetch(`/auth/getuserByHouse?houseId=${encodeURIComponent(houseId)}`)
            .then(response => {
                if (!response.ok)
                {
                    throw new Error('Errore durante il recupero dei coinquilini.');
                }
                return response.json();
            })
            .then(data => {
                //console.log('Coinquilini recuperati con successo:', data);
                data.forEach(coinquilino => {
                    coinquilini.push(coinquilino);
                });
            })
            .catch(error => {
                console.error('Errore:', error);
                alert('Si è verificato un errore durante il recupero dei coinquilini.');
            });

        localStorage.setItem("coinquilini", JSON.stringify(coinquilini));

        if (coinquilini.length === 0)
        {
            const li = document.createElement("li");
            li.textContent = "Nessuna persona presente in casa.";
            li.className = "messaggio-vuoto";
            lista.appendChild(li);
            return;
        }

        coinquilini.forEach(coinquilino => {
            const li = document.createElement("li");
            li.className = "persona-item";

            li.innerHTML = `
                <img src="${coinquilino.img}" alt="${coinquilino.nome} ${coinquilino.cognome}">
                <span>${coinquilino.nome} ${coinquilino.cognome}</span>
            `;

            lista.appendChild(li);
        });
    }
    catch (err)
    {
        console.error("Errore nel recupero utenti:", err);
        lista.innerHTML = "<li class='messaggio-vuoto'>Errore nel caricamento delle persone.</li>";
    }
}