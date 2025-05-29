
const coinquiliniStr = localStorage.getItem("coinquilini");
const houseId = localStorage.getItem("houseId");

let coinquilini = [];
try {
    coinquilini = coinquiliniStr ? JSON.parse(coinquiliniStr) : [];
} catch (e) {
    console.error("Errore nel parsing di coinquilini da localStorage", e);
}


function unavailableForm()
{
    const form = document.querySelector(".form-container");

    form.addEventListener("submit", (e) => {
        e.preventDefault();

        const selectedTenantId = document.getElementById("coinquys-select").value;
        const startTime = document.getElementById("start-time").value;
        const endTime = document.getElementById("end-time").value;
        const task = document.getElementById("task-select").value;

        if (!selectedTenantId || !startTime || !endTime || !task) {
            alert("⚠️ Tutti i campi sono obbligatori!");
            return;
        }

        fetch(`https://localhost:8085/Shift/rest/shift/unAvailability?houseId=${houseId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': localStorage.getItem("token") || ""
            },
            body: JSON.stringify({
                start: startTime,
                end: endTime
            })
        })
            .then(response => {
                if (!response.ok) {
                    return response.text().then(text => { throw new Error(text); });
                }
                return response.json();
            })
            .then(data => {
                alert("✅ Indisponibilità salvata con successo!");
                form.reset();
            })
            .catch(error => {
                console.error("Errore durante la richiesta:", error);
                alert("⚠️ Errore: " + error.message);
            });
    });
}

document.getElementById("viewPlanningLink").addEventListener("click", (e) => {
    e.preventDefault()

    fetch("http://localhost:8085/Shift/rest/shif/calendar/getPlanning",{
        method: "GET",
        headers: {
            'Content-Type': 'application/json',
            'Authorization': localStorage.getItem("token") || ""
        },
        body: houseId
    })
    .then(response => {
        if (!response.ok) {
            return response.text().then(text => { throw new Error(text); });
        }
        return response.json();
    })
    .then(data => {
        renderCalendar(data)
    })
    .catch(error => {
        console.error("Errore durante la richiesta:", error);
        alert("⚠️ Errore: " + error.message);
    })
})

function retriveCoinquys()
{
    const select = document.getElementById('coinquys-select');
    const swapSelect = document.getElementById('swapWith');

    coinquilini.forEach(coinquilino => {
        const option = document.createElement('option');
        option.value = coinquilino.id;
        option.textContent = coinquilino.name;
        select.appendChild(option);

        const swapOption = option.cloneNode(true);
        swapSelect.appendChild(swapOption);
    });
}

function renderReceivedRequests(requests) {
    const list = document.getElementById("receivedRequests");
    list.innerHTML = "";

    if (requests.length === 0) {
        list.innerHTML = "<li>Nessuna richiesta ricevuta</li>";
        return;
    }

    requests.forEach(req => {
        const li = document.createElement("li");
        li.innerHTML = `
      <strong>Da:</strong> ${req.from}<br>
      <strong>Compito:</strong> ${req.task}<br>
      <strong>Data:</strong> ${req.date}<br>
      <strong>Stato:</strong> <span class="${req.status.toLowerCase()}">${req.status}</span>
      ${req.status === "PENDING" ? `
        <div style="margin-top: 5px;">
          <button class="accept-btn">✅</button>
          <button class="decline-btn">❌</button>
        </div>` : ""}
    `;
        list.appendChild(li);
    });
}

renderReceivedRequests(receivedRequests);


function renderCalendar(data)
{
    const container = document.getElementById("calendar");
    container.innerHTML = "";

    dat.forEach(cl)

    if (calendarData.length === 0) {
      emptyMessage.style.display = "block";
      return;
   }

    const stored = JSON.parse(localStorage.getItem("turni") || "[]");

    if (stored.length === 0) {
        container.innerHTML = "<p>Nessun turno salvato.</p>";
        return;
    }

    const list = document.createElement("ul");
    list.style.listStyle = "none";
    list.style.padding = "0";

    stored.forEach(turno => {
        const li = document.createElement("li");
        li.innerHTML = `🧹 <strong>${turno.tenantName}</strong> - ${turno.task} <br>
                        🕒 ${new Date(turno.startTime).toLocaleString()} → ${new Date(turno.endTime).toLocaleString()}`;
        li.style.marginBottom = "1em";
        list.appendChild(li);
    });

    container.appendChild(list);
}

// Modale scambio
const openModalBtn = document.getElementById('openSwapModal');
const modal = document.getElementById('swapModal');
const closeBtn = modal.querySelector('.close');

openModalBtn.addEventListener('click', () => {
    modal.style.display = 'block';
});

closeBtn.addEventListener('click', () => {
    modal.style.display = 'none';
});

window.addEventListener('click', (event) => {
    if (event.target == modal) {
        modal.style.display = 'none';
    }
});

document.getElementById("back-btn").addEventListener("click", async () => {
    try {
        const response = await fetch('https://localhost:8085/Shift/rest/client/backToHome', {
            method: 'GET',
            headers: { 'Content-Type': 'application/json' }
        });
        if (!response.ok) throw new Error("Errore nel ritorno alla home");

        const data = await response.json();
        if (data.path) {
            window.location.href = data.path;
        } else {
            console.error("Risposta senza path.");
        }
    } catch (error) {
        console.error("Errore:", error);
    }
});

document.addEventListener("DOMContentLoaded", () => {
    retriveCoinquys();
    renderCalendar();
});
