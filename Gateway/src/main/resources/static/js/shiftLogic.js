
const coinquilini = localStorage.getItem("listCoiquy");
//const shift = localStorage.getItem("shift");
const houseId = localStorage.getItem("houseId");

document.querySelector('.nav_links li:nth-child(1) a').href = 'http://localhost:8080/dashPage.html?houseId=' + houseId;
document.querySelector('.nav_links li:nth-child(2) a').href = 'http://localhost:8080/expensePage.html?houseId=' + houseId;
document.querySelector('.nav_links li:nth-child(3) a').href = 'http://localhost:8080/shiftPage.html?houseId=' + houseId;
document.querySelector('.nav_links li:nth-child(4) a').href = 'http://localhost:8080/notYet.html';
document.querySelector('.nav_links li:nth-child(5) a').href = 'http://localhost:8080/notYet.html';

function unavailableForm()
{
    const form = document.querySelector(".form-container");

    form.addEventListener("submit", (e) => {
        e.preventDefault();

        const selectedTenantId = document.getElementById("coinquys-select").value;
        const startTime = document.getElementById("start-time").value;
        const endTime = document.getElementById("end-time").value;

        if (!selectedTenantId || !startTime || !endTime) {
            alert("⚠️ Tutti i campi sono obbligatori!");
            return;
        }

        fetch(`http://localhost:8080/Shift/rest/shift/unAvailability?houseId=${houseId}`, {
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

    fetch(`http://localhost:8080/Shift/rest/shif/calendar/getPlanning?houseId=${houseId}`, {
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

//funzione che gestisce l'accettazione o il rifiuto di una richiesta di scambio
function handleRequestAction(idSwap, accept) {
    const action = accept ? "accept" : "decline";

    fetch(`http://localhost:8080/Shift/rest/swaps/${idSwap}/${action}`, {
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
        })
        .catch(error => {
            console.error("Errore:", error);
            alert("⚠️ Errore: " + error.message);
        });

    if(action==="accept")
    {
        window.location.reload();

        /*fetch(`https://localhost:8085/Shift/rest/shif/calendar/getPlanning?houseId=${localStorage.getItem("houseId")}`, {
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
            })*/
    }
}

// Funzione per popolare entrambi i select dei turni (A e B)
function populateAssignmentSelects(shifts) {
    const currentTaskSelect = document.getElementById("currentTask");
    const assignmentBSelect = document.getElementById("assignmentB");

    // Svuota le select
    currentTaskSelect.innerHTML = '<option value="" disabled selected>Seleziona il tuo turno</option>';
    assignmentBSelect.innerHTML = '<option value="" disabled selected>Seleziona turno</option>';

    if (!shifts || shifts.length === 0) {
        fetch(`http://localhost:8080/Shift/rest/swaps/getSwapRequests?houseId=${localStorage.getItem("houseId")}`, {
            method: "GET",
            headers: { //TODO: body mancante
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
        const { task: taskName, description, timeSlot ,point } = task;
        const { start, end } = timeSlot;

        const shiftDiv = document.createElement("div");
        shiftDiv.className = "shift";
        shiftDiv.innerHTML = `
            <strong>${assignedRoommate.usernameRoommate}</strong> - ${taskName} <br>
            - ${description} 🕒 ${new Date(start).toLocaleString()} → ${new Date(end).toLocaleString()}
            -${task.point}
            <div style="margin-top: 5px;">
                    <button class="done-btn" >✅</button>
            </div>
        `;

        shiftDiv.querySelector(".done-btn").addEventListener("click", ()=>handleDoneButton(cleaningAssignment))
        container.appendChild(shiftDiv);
    });
}

async function handleDoneButton(cleaningAssignment) {
    try {
        const response = await fetch("http://localhost:8080/Shift/rest/calendar/taskDone", {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': localStorage.getItem("token") || ""
            },
            body: JSON.stringify(cleaningAssignment)
        });

        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(errorText);
        }

        alert("✅ Compito segnato come completato!");
        assignedPoint(cleaningAssignment)
        window.location.reload();
    } catch (error) {
        console.error("Errore durante il completamento del compito:", error);
        alert("⚠️ Errore: " + error.message);
    }
}

function assignedPoint(cleaningAssignments)
{
    fetch("http://localhost:8080/Rank/rest/client/toRank", {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': localStorage.getItem("token") || ""
        },
        body: JSON.stringify({cleaningAssignments})
    })
    .then(response => {
        if (!response.ok) {
            return response.text().then(text => { throw new Error(text); });
        }
        return response.json();
    })
    .then(data => {
        console.log("Punti assegnati correttamente:", data);
    })
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
        const response = await fetch("http://localhost:8080/Shift/rest/shift/swapRequest", {
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
        const response = await fetch('http://localhost:8080/Shift/rest/client/backToHome', {
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

const form = document.getElementById("createTaskForm");
form.addEventListener("submit", async function (e) {
    e.preventDefault();

    const description = form.querySelector("textarea").value;
    const type = form.querySelector("#taskType").value;
    const dateTime = form.querySelector("input[type='datetime-local']").value;

    if (!description || !type || !dateTime) {
        alert("⚠️ Compila tutti i campi!");
        return;
    }

    try {
        const response = await fetch("http://localhost:8080/Shift/rest/shift/createTask", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": localStorage.getItem("token") || ""
            },
            body: JSON.stringify({
                description: description,
                type: type,
                dateTime: dateTime,
                houseId: localStorage.getItem("houseId")
            })
        });

        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(errorText);
        }

        alert("✅ Compito creato con successo!");
        form.reset();
    } catch (error) {
        alert("⚠️ Errore nella creazione del compito: " + error.message);
    }
});

document.addEventListener("DOMContentLoaded", () => {
    retriveCoinquys();
    //if(shift.length!==0){
    //    renderCalendar(shift);
    //}
    populateAssignmentSelects(shift)
});
