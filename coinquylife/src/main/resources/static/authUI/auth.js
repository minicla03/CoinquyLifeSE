document.addEventListener("DOMContentLoaded", function ()
{
    const form = document.getElementById("auth-form");
    const toggleText = document.getElementById("toggle-text");
    const formTitle = document.getElementById("form-title");
    const submitButton = document.getElementById("submit-btn");
    const nameGroup = document.getElementById("name-group");

    let isLogin = true;

    function toggleForm() {
        isLogin = !isLogin;
        formTitle.innerText = isLogin ? "Login" : "Registrazione";
        submitButton.innerText = isLogin ? "Accedi" : "Registrati";
        toggleText.innerHTML = isLogin
            ? 'Non hai un account? <a onclick="toggleForm()">Registrati</a>'
            : 'Hai già un account? <a onclick="toggleForm()">Accedi</a>';
        nameGroup.style.display = isLogin ? "none" : "block";
    }

    // Aggiungi il listener per il submit del form
    form.addEventListener("submit", function (e) {
        e.preventDefault();
        const email = document.getElementById("email").value;
        const password = document.getElementById("password").value;
        const name = document.getElementById("name") ? document.getElementById("name").value : null;

        if (isLogin) {

            loginUser(email, password);
        } else {
            // Registrazione
            registerUser(name, email, password);
        }
    });

    // Funzione per eseguire il login
    async function loginUser(username, password) {
        try {
            const response = await fetch("http://localhost:8080/rest/auth/login", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({ username, password }),
            });

            const data = await response.json();

            if (response.ok)
            {
                alert(data.message); // Successo
                // Puoi aggiungere la logica per redirigere l'utente alla pagina principale
            } else {
                alert(data.message); // Errore
            }
        } catch (error) {
            console.error("Errore durante il login:", error);
            alert("Errore nella connessione al server");
        }
    }

    // Funzione per eseguire la registrazione
    async function registerUser(name, email, password) {
        try {
            const response = await fetch("http://localhost:8080/rest/auth/register", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({ name, email, password }),
            });

            const data = await response.json();

            if (response.ok) {
                alert(data.message);
                // Puoi aggiungere la logica per redirigere l'utente alla pagina di login
            } else {
                alert(data.message);
            }
        } catch (error) {
            console.error("Errore durante la registrazione:", error);
            alert("Errore nella connessione al server");
        }
    }

    window.toggleForm = toggleForm;
});
