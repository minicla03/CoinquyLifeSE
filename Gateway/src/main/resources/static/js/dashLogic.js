const houseId = localStorage.getItem('houseId');

const monthYear = document.getElementById("monthYear");
const calendarDays = document.getElementById("calendarDays");
const prevBtn = document.getElementById("prevMonth");
const nextBtn = document.getElementById("nextMonth");

let date = new Date();

function renderCalendar() {
    const year = date.getFullYear();
    const month = date.getMonth();
    const firstDay = new Date(year, month, 1).getDay();
    const daysInMonth = new Date(year, month + 1, 0).getDate();
    const lastMonth_lastDay = new Date(year, month, 0).getDate();
    const lastMonth_DaysToAdd = firstDay === 0 ? 6 : firstDay - 1;
    const nextMonth_DaysToAdd = 42 - (daysInMonth + lastMonth_DaysToAdd);

    const monthNames = ["Gennaio", "Febbraio", "Marzo", "Aprile", "Maggio", "Giugno",
        "Luglio", "Agosto", "Settembre", "Ottobre", "Novembre", "Dicembre"];

    monthYear.textContent = `${monthNames[month]} ${year}`;
    calendarDays.innerHTML = "";

    for (let i = lastMonth_DaysToAdd - 1; i >= 0; i--) {
        calendarDays.innerHTML += `<div class="prev-days">${lastMonth_lastDay - i}</div>`;
    }

    for (let day = 1; day <= daysInMonth; day++) {
        calendarDays.innerHTML += `<div class="day">${day}</div>`;
    }

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

// Eventi menu
document.getElementById("btnHome").addEventListener("click", function () {
    window.location.reload();
});

document.getElementById("btnSpese").addEventListener("click", function () {
    fetch("http://localhost:8080/Dashboard/rest/clientDash/spese", {
        method: "GET",
        headers: {
            "Authorization": "Bearer " + localStorage.getItem("token"),
        }
    })
        .then(response => {
            if (!response.ok) throw new Error(`Errore ${response.status}`);
            return response.json();
        })
        .then(data => {
            if (data.path) window.location.href = data.path;
        })
        .catch(err => console.error("Errore nella navigazione:", err));
});

document.getElementById("btnTurni").addEventListener("click", function () {
    fetch("http://localhost:8080/Dashboard/rest/clientDash/turni", {
        method: "GET",
        headers: {
            "Authorization": "Bearer " + localStorage.getItem("token"),
        }
    })
        .then(response => {
            if (!response.ok) throw new Error(`Errore ${response.status}`);
            return response.json();
        })
        .then(data => {
            if (data.path) window.location.href = data.path;
        })
        .catch(err => console.error("Errore nella navigazione:", err));
});

document.getElementById("btnRegole").addEventListener("click", () => {
    window.location.href = "notYet.html";
});

document.getElementById("btnClassifica").addEventListener("click", () => {
    window.location.href = "notYet.html";
});

document.addEventListener("DOMContentLoaded", () => {
    retrieveCoinquy();
    retriveTurni();
    retrieveClassifica();
});


function retrieveCoinquy() {
    fetch(`http://localhost:8080/Dashboard/rest/dash/retrieveCoinquy?houseId=${houseId}`, {
        method: 'GET',
        headers: { 'Content-Type': 'application/json' }
    })
        .then(response => response.json())
        .then(data => {
            const coinquyList = document.getElementById("lista-persone");
            coinquyList.innerHTML = "";
            data.forEach(user => {
                const li = document.createElement("li");
                li.className = "coinquy-item";
                console.log(data);
                li.innerHTML = `
                <span><strong>${user.username}</strong> - ${user.name} ${user.surname}</span>`;
                coinquyList.appendChild(li);
            });
            localStorage.setItem("listCoiquy", JSON.stringify(data));
        })
        .catch(error => console.error('Errore:', error));
}

function retriveTurni() {
    fetch(`http://localhost:8080/Dashboard/rest/dash/retrieveTurni?houseId=${houseId}`, {
        method: 'GET',
        headers: { 'Content-Type': 'application/json' }
    })
        .then(response => {
            if (!response.ok) throw new Error("Errore nel recupero dei turni");
            return response.json();
        })
        .then(data => {
            console.log("Turni:", data);
            // TODO: mostra turni nel DOM
        })
        .catch(error => console.error("Errore:", error));
}

function retrieveClassifica() {
    fetch(`http://localhost:8080/Dashboard/rest/dash/retrieveClassifica?houseId=${houseId}`, {
        method: "GET",
        headers: { "Accept": "application/json" }
    })
        .then(response => response.json())
        .then(classificaList => {
            const rankList = document.getElementById("rank");
            rankList.innerHTML = "";

            if (!classificaList || classificaList.length === 0) {
                rankList.innerHTML = "<li>Nessun dato disponibile.</li>";
                return;
            }

            classificaList.sort((a, b) => b.punti - a.punti);
            classificaList.forEach(item => {
                const li = document.createElement("li");
                li.textContent = `${item.nome} - ${item.punti} punti`;
                rankList.appendChild(li);
            });
        })
        .catch(error => {
            console.error("Errore nel recupero classifica:", error);
            document.getElementById("rank").innerHTML = "<li>Errore nel caricamento.</li>";
        });
}