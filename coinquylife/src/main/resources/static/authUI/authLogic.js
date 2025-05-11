function showRegister() {
    document.getElementById("loginForm").classList.add("hidden");
    document.getElementById("registerForm").classList.remove("hidden");
}

function showLogin() {
    document.getElementById("registerForm").classList.add("hidden");
    document.getElementById("loginForm").classList.remove("hidden");
}


document.querySelector("#registerForm button").addEventListener("click", async () => {
    const inputs = document.querySelectorAll("#registerForm input");
    const data = {
        username: inputs[2].value,
        password: inputs[4].value,
        name: inputs[0].value,
        surname: inputs[1].value,
        email: inputs[3].value,
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
            alert("Registrazione completata!");
            showLogin();
        } else {
            alert("Errore: " + result);
        }
    } catch (error) {
        alert("Errore di rete: " + error.message);
    }
});

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
            alert("Login effettuato!");
            window.location.href = "/home";
        } else {
            alert("Errore: " + result);
        }
    } catch (error) {
        alert("Errore di rete: " + error.message);
    }
});

document.querySelector("#loginForm button").addEventListener("click", async (event) => {
    event.preventDefault(); // Previene il doppio invio del form
    document.querySelector("#loginForm").dispatchEvent(new Event("submit"));
});