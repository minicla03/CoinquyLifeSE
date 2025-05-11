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
        nome: inputs[0].value,
        cognome: inputs[1].value,
        username: inputs[2].value,
        email: inputs[3].value,
        password: inputs[4].value,
    };

    try {
        const res = await fetch("/rest/auth/register", {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded",
            },
            body: JSON.stringify(data),
        });

        const result = await res.json();
        if (res.ok) {
            alert("Registrazione completata!");
            showLogin();
        } else {
            alert("Errore: " + result.message);
        }
    } catch (error) {
        alert("Errore di rete: " + error.message);
    }
});

document.querySelector("#loginForm button").addEventListener("click", async () => {
    const inputs = document.querySelectorAll("#loginForm input");
    const data = {
        username: inputs[0].value,
        password: inputs[1].value,
    };

    try {
        const res = await fetch("/rest/auth/register", {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded",
            },
            body: JSON.stringify(data),
        });

        const result = await res.json();
        if (res.ok) {
            alert("Login effettuato!");


        } else {
            alert("Errore: " + result.message);
        }
    } catch (error) {
        alert("Errore di rete: " + error.message);
    }
});
