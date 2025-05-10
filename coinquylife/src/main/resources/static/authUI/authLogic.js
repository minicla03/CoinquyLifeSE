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
        name: inputs[0].value,
        surname: inputs[1].value,
        username: inputs[2].value,
        email: inputs[3].value,
        password: inputs[4].value,
    };

    const queryParams = new URLSearchParams(data).toString();

    try {
        const res = await fetch(`/rest/auth/register?${queryParams}`, {
            method: "POST",
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

document.querySelector("#loginForm").addEventListener("submit", async () => {})
document.querySelector("#loginForm button").addEventListener("click", async () => {
    const inputs = document.querySelectorAll("#loginForm input");
    const data = {
        username: inputs[0].value,
        password: inputs[1].value,
    };

    const queryParams = new URLSearchParams(data).toString();

    try {
        const res = await fetch(`/rest/auth/login?${queryParams}`, {
            method: "POST",
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
