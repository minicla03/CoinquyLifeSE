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
    let out;
    const inputs = document.querySelectorAll("#registerForm input");
    const token = localStorage.getItem("token");
    const data = {
        houseName: inputs[0].value,
        houseAddress: inputs[1].value,
    };


    try {
        const res = await fetch("http://localhost:8080/House/rest/house/create", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": "Bearer " + token,
            },
            body: JSON.stringify(data),
        });


        const result = await res.text();
        if (res.ok) {
            //alert("Registrazione completata!");
            const json = JSON.parse(result);
            const code = json["code"];
            out = document.getElementById("outputreg");
            out.innerHTML = "✅ Registrazione completata, il codice di accesso alla tua Coinquihouse è: <br>" + code;
        } else {
            //alert("Errore: " + result);
            out = document.getElementById("outputreg");
            out.innerHTML = "❗️" + result;
        }
    } catch (error) {
        //alert("Errore di rete: " + error.message);
        out = document.getElementById("outputreg");
        out.innerHTML = "❗️" + error.message;
    }
});

// Aggiungi un listener per il click sul link "Hai già un account?"
document.querySelector("#loginForm").addEventListener("submit", async (event) => {
    let out;
    event.preventDefault(); // Previene il comportamento predefinito del form
    const inputs = document.querySelectorAll("#loginForm input");
    const token = localStorage.getItem("token");
    const data = {
        houseId: inputs[0].value,
    };

    try {
        const res = await fetch(`http://localhost:8080/House/rest/house/loginHouse`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": "Bearer " + token,

            },
            body: JSON.stringify(data),
        });

        const result = await res.text();
        if (res.ok) {
            //alert("Login effettuato!");
            out = document.getElementById("outputlog");
            out.innerHTML = "✅ Login effettuato!";
            redirect();
            window.location.href = "/home";
        } else if (res.status === 401 || res.status === 403) {
            //alert("Codice di accesso errato");
            out = document.getElementById("outputlog");
            out.innerHTML = "❗️ Accesso negato";
        }
        else {
            //alert("Errore: " + result);
            out = document.getElementById("outputlog");
            out.innerHTML = "❗️" + result;
        }
    } catch (error) {
        //alert("Errore di rete: " + error.message);
        out = document.getElementById("outputlog");
        out.innerHTML = "❗️" + error.message;
    }
});


function redirec() {
    const token = localStorage.getItem("token");
    if (token) {
        fetch("http://localhost:8081/House/rest/client/dash", {
            method: "GET",
            headers: {
                "Authorization": "Bearer " + token
            }
        })
            .then(response => {
                if (response.ok) {
                    return response.json();  // <--- ATTENZIONE: bisogna restituire la Promise
                } else {
                    throw new Error("Errore: " + response.statusText);
                }
            })
            .then(data => {
                const link = data["path"];
                if (link) {
                    window.location.href = link;
                } else {
                    alert("Errore: 'path' non trovato nella risposta");
                }
            })
            .catch(err => {
                alert(err.message);
            });
    } else {
        alert("Token non trovato. Assicurati di essere autenticato.");
    }
}


// Aggiungi un listener per il click sul link "Hai già un account?"
document.querySelector("#loginForm button").addEventListener("click", async (event) => {
    event.preventDefault(); // Previene il doppio invio del form
    document.querySelector("#loginForm").dispatchEvent(new Event("submit"));
});

