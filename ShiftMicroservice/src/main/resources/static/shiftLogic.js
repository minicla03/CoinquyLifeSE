
const coinquiliniStr = localStorage.getItem("coinquilini");
const houseId = localStorage.getItem("houseId");
const shift = localStorage.getItem("shift");

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

    fetch(`http://localhost:8085/Shift/rest/shif/calendar/getPlanning?houseId=${houseId}`, {
        method: "GET",
        headers: {
            'Content-Type': 'application/json',
            'Authorization': localStorage.getItem("token") || ""
        }
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

function handleRequestAction(idSwap, accept) {
    const action = accept ? "accept" : "decline";

    fetch(`https://localhost:8085/Shift/rest/swaps/${idSwap}/${action}`, {
        method: "PUT",
        headers: {
            'Content-Type': 'application/json',
            'Authorization': localStorage.getItem("token") || ""
        }
    })
        .then(response => {
            if (!response.ok) {
                return response.text().then(text => { throw new Error(text); });
            }
            alert(`✅ Richiesta ${accept ? "accettata" : "rifiutata"} con successo!`);
            window.location.reload(); // o aggiorna dinamicamente la lista
        })
        .catch(error => {
            console.error("Errore:", error);
            alert("⚠️ Errore: " + error.message);
        });
}

// Funzione per popolare entrambi i select dei turni (A e B)
function populateAssignmentSelects(shifts) {
    const currentTaskSelect = document.getElementById("currentTask");
    const assignmentBSelect = document.getElementById("assignmentB");

    // Svuota le select
    currentTaskSelect.innerHTML = '<option value="" disabled selected>Seleziona il tuo turno</option>';
    assignmentBSelect.innerHTML = '<option value="" disabled selected>Seleziona turno</option>';

    if (!shifts || shifts.length === 0) {
        fetch(`https://localhost:8085/Shift/rest/swaps/getSwapRequests?houseId=${localStorage.getItem("houseId")}`, {
            method: "GET",
            headers: {
                'Content-Type': 'application/json',
                'Authorization': localStorage.getItem("token") || ""
            }
        })
            .then(response => {
                if (!response.ok) {
                    return response.text().then(text => {
                        throw new Error(text);
                    });
                }
                return response.json();
            })
            .then(assignments => {
                renderReceivedSwapRequests(assignments, currentTaskSelect, assignmentBSelect);
            })
            .catch(error => {
                console.error("Errore durante il recupero delle richieste di scambio:", error);
                alert("⚠️ Errore: " + error.message);
            });
    } else {
        renderReceivedSwapRequests(shifts, currentTaskSelect, assignmentBSelect);
    }
}

// Funzione per riempire le select con i dati
function renderReceivedSwapRequests(assignments, currentTaskSelect, assignmentBSelect) {
    const list = document.getElementById("receivedRequests");
    list.innerHTML = "";

    if (assignments.length === 0) {
        list.innerHTML = "<li>Nessuna richiesta ricevuta</li>";
        return;
    }

    assignments.forEach(assignment => {
        // Aggiungi alle select
        const optionA = document.createElement("option");
        optionA.value = assignment.id;
        optionA.textContent = `${assignment.task.task} - ${new Date(assignment.task.timeSlot.start).toLocaleString()}`;
        currentTaskSelect.appendChild(optionA);

        const optionB = optionA.cloneNode(true);
        assignmentBSelect.appendChild(optionB);

        // Aggiungi alla lista
        const li = document.createElement("li");
        li.innerHTML = `
            <strong>Da:</strong> ${assignment.from}<br>
            <strong>Compito:</strong> ${assignment.task.task}<br>
            <strong>Data:</strong> ${new Date(assignment.task.timeSlot.start).toLocaleString()}<br>
            <strong>Stato:</strong> <span class="${assignment.status.toLowerCase()}">${assignment.status}</span>
            ${assignment.status === "PENDING" ? `
                <div style="margin-top: 5px;">
                    <button class="accept-btn" >✅</button>
                    <button class="decline-btn">❌</button>
                </div>` : ""}
        `;

        // Eventi su bottoni
        if (assignment.status === "PENDING")
        {
            const acceptBtn = li.querySelector(".accept-btn");
            const declineBtn = li.querySelector(".decline-btn");

            acceptBtn.addEventListener("click", () => handleRequestAction(assignment.id, true));
            declineBtn.addEventListener("click", () => handleRequestAction(assignment.id, false));
        }

        list.appendChild(li);
    });
}


function renderCalendar(data) {
    const container = document.getElementById("calendar");
    container.innerHTML = "";

   localStorage.setItem("shifts", data)

    if (!data || data.length === 0) {
        const emptyMessage = document.getElementById("emptyMessage");
        emptyMessage.style.display = "block";
        return;
    }

    data.forEach(cleaningAssignment => {
        const { assignedRoommate, task } = cleaningAssignment;
        const { task: taskName, description, timeSlot } = task;
        const { start, end } = timeSlot;

        const shiftDiv = document.createElement("div");
        shiftDiv.className = "shift";
        shiftDiv.innerHTML = `
            <strong>${assignedRoommate.usernameRoommate}</strong> - ${taskName} <br>
            - ${description} 🕒 ${new Date(start).toLocaleString()} → ${new Date(end).toLocaleString()}
        `;
        container.appendChild(shiftDiv);
    });
}

//Poupop dello scambio
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
    if (event.target === modal) {
        modal.style.display = 'none';
    }
});

//funzione per creare la richiesta di scambio
document.getElementById("sendSwapBtn").addEventListener("click", async function(e) {
    e.preventDefault();
    const currentTask = document.getElementById("currentTask").value;
    const assignmentB = document.getElementById("assignmentB").value;

    if (!currentTask || !assignmentB) {
        alert("⚠️ Seleziona entrambi i turni per lo scambio!");
        return;
    }

    try
    {
        const response = await fetch("https://localhost:8085/Shift/rest/shift/swapRequest", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": localStorage.getItem("token") || ""
            },
            body: JSON.stringify({
                fromAssignmentId: currentTask,
                toAssignmentId: assignmentB,
                houseId: localStorage.getItem("houseId")
            })
        });

        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(errorText);
        }

        alert("✅ Richiesta di scambio inviata!");
        document.getElementById("swapForm").reset();
        document.getElementById("swapModal").style.display = "none";
    } catch (error)
    {
        alert("⚠️ Errore nell'invio della richiesta: " + error.message);
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
    renderCalendar(shift);
    populateAssignmentSelects(shift)
});
