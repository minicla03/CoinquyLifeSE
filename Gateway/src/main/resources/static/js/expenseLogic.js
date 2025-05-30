
const expenses = [];
localStorage.setItem("houseId", "af106523-00fb-41a8-af03-ef9d23a00952"); // Imposta un valore di esempio per houseId, può essere modificato in base alle necessità
const houseId = localStorage.getItem("houseId");

let coinquilini = [];

const descriptionInput = document.getElementById("description");
const amountInput = document.getElementById("amount");
let payerInput = document.getElementById("option")
const categorySelect = document.getElementById("category");
const addBtn = document.getElementById("add-btn");
const expenseList = document.getElementById("expense-list");
const totalSpan = document.getElementById("total");
const calculateBtn = document.getElementById("calculate-btn");
const balancesDiv = document.getElementById("balances");

function addExpense() {
    const description = descriptionInput.value.trim();
    const amountStr = amountInput.value.trim().replace(",", ".");
    const amount = parseFloat(amountStr);
    const createdBy = payerInput.value.trim();
    const category = categorySelect.value;
    const selectedParticipants = Array.from(document.querySelectorAll('input[name="participants"]:checked')).map(cb => cb.value);
    participants: selectedParticipants.map(name => name.charAt(0) + name.slice(1));

    if (!description || !payer || !category || isNaN(amount) || amount <= 0 || selectedParticipants.length === 0)
    {
        alert("Per favore, compila tutti i campi correttamente.");
    }

    fetch('http://localhost:8080/Expense/rest/expense/createExpense', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            description: description,
            amount: amount,
            createdBy: createdBy,
            category: category,
            participants: selectedParticipants,
            houseId: houseId
        })
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Errore durante la creazione della spesa.');
            } else if (response.ok) {
                alert('Spesa creata con successo');
                let expense = {
                    description: description,
                    amount: amount,
                    createdBy: createdBy,
                    category: category,
                    participants: selectedParticipants,
                    houseId: houseId
                }
                expenses.push(expense);
                renderSingleExpense(expense, expenses.length-1);
                updateTotal();
                balancesDiv.innerHTML = "<p>ℹ️ Nessun calcolo effettuato</p>"

            }
        })
        .catch(error => {
            console.error('Errore:', error);
        });

    descriptionInput.value = "";
    amountInput.value = "";
    payerInput.value = "";
    categorySelect.value = "";
    balancesDiv.innerHTML = "<p>ℹ️ Nessun calcolo effettuato</p>";
}

function renderSingleExpense(expense, index) {
    const li = document.createElement("li");
    li.classList.toggle("paid", expense.paid);

    const detailsDiv = document.createElement("div");
    detailsDiv.classList.add("details");
    detailsDiv.innerHTML = `
        <span class="title">${expense.description}</span>
        <span class="meta">🍂 ${expense.category} • 🙋 ${expense.createdBy}</span>`;

    const amountDiv = document.createElement("div");
    amountDiv.classList.add("amount");
    amountDiv.textContent = `€${expense.amount.toFixed(2)}`;

    const paidBtn = document.createElement("button");
    paidBtn.textContent = expense.paid ? "✅ Pagata" : "✔️ Saldata";
    paidBtn.disabled = expense.paid;
    paidBtn.classList.add("paid-btn");

    paidBtn.addEventListener("click", () => {
        expenses[index].paid = true;
        expenseList.children[index].replaceWith(renderSingleExpense(expenses[index], index));
        updateTotal();
    });

    li.appendChild(detailsDiv);
    li.appendChild(amountDiv);
    li.appendChild(paidBtn);

    expenseList.appendChild(li);
    return li;
}

async function retriveExpenses() {
    await fetch(`http://localhost:8080/Expense/rest/expense/getAllExpenses?houseId=${houseId}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Errore durante il recupero delle spese.');
            }
            return response.json();
        })
        .then(data => {
            console.log('Spese recuperate con successo:', data);
            if (Array.isArray(data.expenses)) {
                data.expenses.forEach(expense => { expenses.push(expense); });
            }
        })
        .catch(error => {
            console.error('Errore:', error);
            alert('Si è verificato un errore durante il recupero delle spese.');
        });

    expenseList.innerHTML = "";
    console.log(expenses);
    expenses.forEach((expense, index) => {
        const li = document.createElement("li");

        li.classList.toggle("paid", expense.paid);

        const detailsDiv = document.createElement("div");
        detailsDiv.classList.add("details");
        detailsDiv.innerHTML = `
        <span class="title">${expense.description}</span>
        <span class="meta">🍂 ${expense.category} • 🙋 ${expense.createdBy}</span>`;

        const amountDiv = document.createElement("div");
        amountDiv.classList.add("amount");
        amountDiv.textContent = `€${expense.amount.toFixed(2)}`;

        const paidBtn = document.createElement("button");
        paidBtn.textContent = expense.paid ? "✅ Pagata" : "✔️ Saldata";
        paidBtn.disabled = expense.paid;
        paidBtn.classList.add("paid-btn");

        paidBtn.addEventListener("click", () => {
            expenses[index].paid = true;
            updateTotal();
        });

        li.appendChild(detailsDiv);
        li.appendChild(amountDiv);
        li.appendChild(paidBtn);

        expenseList.appendChild(li);
    });
}

function updateTotal() {
    const total = expenses
        .filter(exp => !exp.paid)
        .reduce((sum, exp) => sum + exp.amount, 0);
    totalSpan.textContent = `€${total.toFixed(2)}`;
}

async function retriveCoinquys() {
    try {
        const response = await fetch('http://localhost:8080/Auth/rest/auth/getUserByHouseId', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                houseId: houseId
            })
        });

        if (!response.ok) {
            throw new Error('Errore durante il recupero dei coinquilini.');
        }

        coinquilini = await response.json(); // <-- await qui è fondamentale
        console.log('Coinquilini recuperati con successo:', coinquilini);


        // Assicurati che sia un array
        if (!Array.isArray(coinquilini)) {
            throw new Error("Il valore ricevuto non è un array");
        }

        // Participants Checkbox (scelta multipla)
        const container = document.getElementById('participants-container');
        container.innerHTML = ""; // Pulisce eventuali contenuti precedenti

        const participantsLabel = document.createElement('label');
        participantsLabel.textContent = "👥 Chi partecipa ?";
        participantsLabel.style.display = "block";
        participantsLabel.style.marginLeft= "2px";
        participantsLabel.style.fontWeight = "bold";
        participantsLabel.style.marginBottom = "8px";
        container.appendChild(participantsLabel);

        const participantsGrid = document.createElement('div');
        participantsGrid.style.display = "grid";
        participantsGrid.style.gridTemplateColumns = "1fr 1fr";
        participantsGrid.style.gap = "8px";

        coinquilini.forEach(coinquilino => {
            const div = document.createElement('div');
            div.style.display = "flex";
            div.style.alignItems = "center";
            div.style.background = "#f4f4f8";
            div.style.borderRadius = "8px";
            div.style.padding = "6px 10px";
            div.style.boxShadow = "0 1px 3px rgba(0,0,0,0.04)";
            div.style.transition = "background 0.2s";

            const checkbox = document.createElement('input');
            checkbox.type = "checkbox";
            checkbox.name = "participants";
            checkbox.value = coinquilino.username;
            checkbox.style.marginRight = "8px";
            checkbox.style.accentColor = "#29297f";

            const label = document.createElement('label');
            label.textContent = "👤 " + coinquilino.username;
            label.style.cursor = "pointer";
            label.style.userSelect = "none";

            div.appendChild(checkbox);
            div.appendChild(label);
            participantsGrid.appendChild(div);
        });
        container.appendChild(participantsGrid);

        //Payer Select
        const containerSelect = document.getElementById('payer-container');
        containerSelect.innerHTML = ""; // Pulisce eventuali contenuti precedenti
        const selectPayer = document.createElement('select');
        selectPayer.id = "payer";
        selectPayer.name = "payer";
        selectPayer.style.width = "100%";
        const defaultOption = document.createElement('option');
        defaultOption.value = "";
        defaultOption.textContent = "🙋 Chi ha pagato ?";
        selectPayer.appendChild(defaultOption);
        coinquilini.forEach(coinquilino => {
            const option = document.createElement('option');
            option.value = coinquilino.username;
            option.textContent = coinquilino.username;
            selectPayer.appendChild(option);
        });
        containerSelect.appendChild(selectPayer);


    } catch (error) {
        console.error('Errore nel recupero dei coinquilini:', error);
        alert("Errore durante il caricamento dei coinquilini.");
    }
}

//per inizializzazione dei contenuti della pagina nel caso ci sono
//delle informazioni salvate nel DB
document.addEventListener('DOMContentLoaded', async () => {

    await retriveCoinquys()
    payerInput = document.getElementById("payer");
    await retriveExpenses();
    updateTotal();
});

addBtn.addEventListener("click", addExpense);