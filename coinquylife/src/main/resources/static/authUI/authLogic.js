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
    };

    try {
            method: "POST",
            headers: {
            },
            body: JSON.stringify(data),
        });

        if (res.ok) {
            alert("Registrazione completata!");
            showLogin();
        } else {
        }
    } catch (error) {
        alert("Errore di rete: " + error.message);
    }
});

    const inputs = document.querySelectorAll("#loginForm input");
    const data = {
        username: inputs[0].value,
        password: inputs[1].value,
    };

    try {
            method: "POST",
            headers: {
            },
            body: JSON.stringify(data),
        });

        if (res.ok) {
            alert("Login effettuato!");
        } else {
        }
    } catch (error) {
        alert("Errore di rete: " + error.message);
    }
});
