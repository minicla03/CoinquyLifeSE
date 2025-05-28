
const shift = [];
const coinquiliniStr = localStorage.getItem("coinquilini");
const houseId = localStorage.getItem("houseId");

let coinquilini = [];
try {
    coinquilini = coinquiliniStr ? JSON.parse(coinquiliniStr) : [];
} catch (e) {
    console.error("Errore nel parsing di coinquilini da localStorage", e);
}

// Inizializza la pagina
function init()
{
    retriveCoinquys();

    const form = document.querySelector(".form-container");
    form.addEventListener("submit", (e) => {
        e.preventDefault();

        // Prendi valori dal form
        //id?
        const selectedTenant = tenants.find(t => t.id == selectedTenantId)?.name || "Sconosciuto";
        const startTime = form.querySelector('input[type="datetime-local"]:nth-of-type(1)').value;
        const endTime = form.querySelector('input[type="datetime-local"]:nth-of-type(2)').value;
        const task = form.querySelector('select:nth-of-type(2)').value;

        if (new Date(endTime) <= new Date(startTime)) {
            alert("La data di fine deve essere successiva alla data di inizio.");
            return;  // blocca il submit
        }

        // Qui puoi aggiungere logica per salvare i dati, inviarli a un server, aggiornare calendario ecc.
    });
}

async function retriveCoinquys()
{
    const container = document.getElementById('coinquys-select');

    coinquilini.forEach(coinquilino => {
        const checkbox = document.createElement('input');
        checkbox.type = 'checkbox';
        checkbox.name = 'participants';
        checkbox.value = coinquilino.name;
        checkbox.id = `participant-${coinquilino.name}`;

        const label = document.createElement('label');
        label.htmlFor = checkbox.id;
        label.textContent = coinquilino.name;

        container.appendChild(checkbox);
        container.appendChild(label);
        container.appendChild(document.createElement('br'));
    });
}

//per inizializzazione dei contenuti della pagina nel caso ci sono
//delle informazioni salvate nel DB
document.addEventListener('DOMContentLoaded', async () => {

    await retriveCoinquys()
    await  init()
});

document.getElementById("back-btn").addEventListener("click", async () => {
    try {
        const response = await fetch('https://localhost:8085/Shift/rest/client/backToHome', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        });
        if (!response.ok) {
            throw new Error("Errore");
        }
        const data = await response.json();

        if (data.path)
        {
            window.location.href = data.path;
        } else {
            console.error('La risposta non contiene il campo "path".');
        }

    } catch (error) {
        console.error('Si è verificato un errore:', error);
    }
})