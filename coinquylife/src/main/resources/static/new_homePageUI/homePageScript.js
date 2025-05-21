
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

    const personeInCasa = [
    { nome: "Alice", cognome: "Rossi", img: "user-solid.svg" },
    { nome: "Luca", cognome: "Bianchi", img: "user-solid.svg" },
    { nome: "Giulia", cognome: "Verdi", img: "user-solid.svg" },
    { nome: "Marco", cognome: "Neri", img: "user-solid.svg" },
    { nome: "Sara", cognome: "Gialli", img: "user-solid.svg" }
    ];

    const lista = document.getElementById("lista-persone");

    personeInCasa.forEach(persona => {
    const li = document.createElement("li");
    li.className = "persona-item";

    li.innerHTML = `
      <img src="${persona.img}" alt="${persona.nome} ${persona.cognome}">
      <span>${persona.nome} ${persona.cognome}</span>
    `;

    lista.appendChild(li);
});


const eventi = [
    {
        titolo: "Oggi ospiti",
        autore: {
            nome: "Mario",
            cognome: "Rossi"
        },
        data: "2025-05-16"
    },
    {
        titolo: "Pulizie generali",
        autore: {
            nome: "Giulia",
            cognome: "Verdi"
        },
        data: "2025-05-15"
    },
    {
        titolo: "Compleanno di Luca",
        autore: {
            nome: "Anna",
            cognome: "Bianchi"
        },
        data: "2025-05-13"
    },
    {
        titolo: "Visita del padrone di casa",
        autore: {
            nome: "Marco",
            cognome: "Neri"
        },
        data: "2025-05-10"
    },
    {
        titolo: "Festa di fine sessione",
        autore: {
            nome: "Laura",
            cognome: "Gialli"
        },
        data: "2025-05-08"
    }
];

const listaEventi = document.getElementById("lista-eventi");
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
