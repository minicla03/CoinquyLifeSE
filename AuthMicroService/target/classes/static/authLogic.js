// Funzione per mostrare il form di registrazione
function showRegister() {
    document.getElementById("loginForm").classList.add("hidden");
    document.getElementById("registerForm").classList.remove("hidden");
    document.getElementById("formTitle").textContent = "Registrazione";
}

// Funzione per mostrare il form di login
function showLogin() {
    document.getElementById("registerForm").classList.add("hidden");
    document.getElementById("loginForm").classList.remove("hidden");
    document.getElementById("formTitle").textContent = "Login";
}

// Funzione per reindirizzare alla pagina principale
function redirectToHouse() {
    const token = localStorage.getItem("token");
    if (token) {
        fetch("http://localhost:8080/rest/client/house", {
            method: "GET",
            headers: {
                "Authorization": "Bearer " + token
            }
        })
            .then(response => {
                if (!response.ok) throw new Error("Errore nella risposta del server");
                return response.text();
            })
            .then(html => {
                document.open();       // Svuota il DOM
                document.write(html);  // Scrive il nuovo HTML
                document.close();      // Chiude il documento
            })
            .catch(err => {
                alert("Errore: " + err.message);
            });
    } else {
        alert("Token non trovato. Assicurati di essere autenticato.");
    }
}

// Aggiungi un listener per il click sul link "Registrati"
document.querySelector("#registerForm button").addEventListener("click", async () => {
    const inputs = document.querySelectorAll("#registerForm input");
    const data = {
        name: inputs[0].value,
        surname: inputs[1].value,
        username: inputs[2].value,
        email: inputs[3].value,
        password: inputs[4].value,
    };

    try {
        const res = await fetch("http://localhost:8080/rest/auth/register", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(data),
        });

        const result = await res.text();
        if (res.ok) {
            //alert("Registrazione completata!");
            var out = document.getElementById("outputreg");
            out.innerHTML = "✅ Registrazione completata!";
            redirectToHouse();
        } else {
            //alert("Errore: " + result);
            var out = document.getElementById("outputreg");
            out.innerHTML = "❗️" + result;
        }
    } catch (error) {
        //alert("Errore di rete: " + error.message);
        var out = document.getElementById("outputreg");
        out.innerHTML = "❗️" + error.message;
    }
});

// Aggiungi un listener per il click sul link "Hai già un account?"
document.querySelector("#loginForm").addEventListener("submit", async (event) => {
    event.preventDefault(); // Previene il comportamento predefinito del form
    const inputs = document.querySelectorAll("#loginForm input");
    const data = {
        username: inputs[0].value,
        password: inputs[1].value,
    };

    try {
        const res = await fetch(`/rest/auth/login`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(data),
        });

        const result = await res.text();
        if (res.ok) {
            //alert("Login effettuato!");
            var out = document.getElementById("outputlog");
            out.innerHTML = "✅ Login effettuato!";
            const json = JSON.parse(result);
            const token = json["token"];
            localStorage.setItem("token", token);
            redirectToHouse();
        } else {
            //alert("Errore: " + result);
            var out = document.getElementById("outputlog");
            out.innerHTML = "❗️" + result;
        }
    } catch (error) {
        //alert("Errore di rete: " + error.message);
        var out = document.getElementById("outputlog");
        out.innerHTML = "❗️" + error.message;
    }
});

// Aggiungi un listener per il click sul link "Hai già un account?"
document.querySelector("#loginForm button").addEventListener("click", async (event) => {
    event.preventDefault(); // Previene il doppio invio del form
    document.querySelector("#loginForm").dispatchEvent(new Event("submit"));
});


