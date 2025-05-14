// Funzione per mostrare il form di registrazione
function showHouseCreation() {
    document.getElementById("loginForm").classList.add("hidden");
    document.getElementById("registerForm").classList.remove("hidden");
    document.getElementById("formTitle").textContent = "Creazione";
}

// Funzione per mostrare il form di login
function showHouseLogin() {
    document.getElementById("registerForm").classList.add("hidden");
    document.getElementById("loginForm").classList.remove("hidden");
    document.getElementById("formTitle").textContent = "CoinquiHouse";
}


// Aggiungi un listener per il click sul link "Registrati"
document.querySelector("#registerForm button").addEventListener("click", async () => {
    const inputs = document.querySelectorAll("#registerForm input");
    const token = localStorage.getItem("token");
    const data = {
        houseName: inputs[0].value,
        houseAddress: inputs[1].value,
    };

    try {
        const res = await fetch("http://localhost:8080/rest/house/create", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`,
            },
            body: JSON.stringify(data),
        });

        const result = await res.text();
        if (res.ok) {
            alert("Registrazione completata!");
            var out = document.getElementById("output");
            out.innerHTML = result;
            //showLogin();
        } else {
            alert("Errore: " + result);
        }
    } catch (error) {
        alert("Errore di rete: " + error.message);
    }
});

// Aggiungi un listener per il click sul link "Hai già un account?"
document.querySelector("#loginForm").addEventListener("submit", async (event) => {
    event.preventDefault(); // Previene il comportamento predefinito del form
    const inputs = document.querySelectorAll("#loginForm input");
    const token = localStorage.getItem("token");
    const data = {
        houseCode: inputs[0].value,
    };

    try {
        const res = await fetch(`/rest/house/loginHouse`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`,
            },
            body: JSON.stringify(data),
        });

        const result = await res.text();
        if (res.ok) {
            alert("Login effettuato!");
            window.location.href = "/home";
        } else {
            alert("Errore: " + result);
        }
    } catch (error) {
        alert("Errore di rete: " + error.message);
    }
});

// Aggiungi un listener per il click sul link "Hai già un account?"
document.querySelector("#loginForm button").addEventListener("click", async (event) => {
    event.preventDefault(); // Previene il doppio invio del form
    document.querySelector("#loginForm").dispatchEvent(new Event("submit"));
});