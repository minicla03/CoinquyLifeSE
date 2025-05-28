
const expenses = [];
const coinquiliniStr = localStorage.getItem("coinquilini");
const houseId = localStorage.getItem("houseId");

let coinquilini = [];
try {
    coinquilini = coinquiliniStr ? JSON.parse(coinquiliniStr) : [];
} catch (e) {
    console.error("Errore nel parsing di coinquilini da localStorage", e);
}

const descriptionInput = document.getElementById("description");
const amountInput = document.getElementById("amount");
const payerInput = document.getElementById("payer");
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
    const payer = payerInput.value.trim();
    const category = categorySelect.value;
    const selectedParticipants = Array.from(document.querySelectorAll('input[name="participants"]:checked')).map(input => input.value);

    if (!description || !payer || !category || isNaN(amount) || amount <= 0 || selectedParticipants.length === 0)
    {
        alert("Per favore, compila tutti i campi correttamente.");
        return;
    }

    fetch('/expense/createExpense', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({description, amount, payer, category, selectedParticipants, houseId})
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Errore durante la creazione della spesa.');
            }
            return response.json();
        })
        .then(data => {
            console.log('Spesa creata con successo:', data);
            expenses.push(data);
            renderSingleExpense(data, expenses.length-1);
            updateTotal();
            balancesDiv.innerHTML = "<p>ℹ️ Nessun calcolo effettuato</p>";
        })
        .catch(error => {
            console.error('Errore:', error);
            alert('Si è verificato un errore durante la creazione della spesa.');
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
        <span class="meta">🍂 ${expense.category} • 🙋 ${expense.payer}</span>`;

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
    await fetch(`/expense/getAllExpenses?houseId=${encodeURIComponent(houseId)}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Errore durante il recupero delle spese.');
            }
            return response.json();
        })
        .then(data => {
            console.log('Spese recuperate con successo:', data);
            data.forEach(expense => { expenses.push(expense); });
        })
        .catch(error => {
            console.error('Errore:', error);
            alert('Si è verificato un errore durante il recupero delle spese.');
        });

    expenseList.innerHTML = "";
    expenses.forEach((expense, index) => {
        const li = document.createElement("li");

        li.classList.toggle("paid", expense.paid);

        const detailsDiv = document.createElement("div");
        detailsDiv.classList.add("details");
        detailsDiv.innerHTML = `
        <span class="title">${expense.description}</span>
        <span class="meta">🍂 ${expense.category} • 🙋 ${expense.payer}</span>`;

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

function calculateBalances() {
    if (expenses.length === 0) {
        balancesDiv.innerHTML = "<p>⚠️ Nessuna spesa registrata.</p>";
        return;
    }

    const unpaidExpenses = expenses.filter(expense => !expense.paid);

    if (unpaidExpenses.length === 0) {
        balancesDiv.innerHTML = "<p>✅ Tutte le spese sono state saldate!</p>";
        return;
    }

    // Raccogli tutti i partecipanti che compaiono nelle spese non pagate
    const allParticipantsSet = new Set();
    unpaidExpenses.forEach(expense => {
        expense.selectedParticipants.forEach(p => allParticipantsSet.add(p));
        allParticipantsSet.add(expense.payer);  // il pagatore potrebbe non essere tra i partecipanti?
    });
    const allParticipants = Array.from(allParticipantsSet);

    // Inizializza i saldi a zero
    const balances = {};
    allParticipants.forEach(p => balances[p] = 0);

    // Per ogni spesa, suddividi l'importo solo tra i partecipanti
    unpaidExpenses.forEach(expense => {
        const n = expense.selectedParticipants.length;
        const share = expense.amount / n;

        // Ogni partecipante deve "dare" la sua quota (quindi decrementa saldo)
        expense.selectedParticipants.forEach(p => {
            balances[p] -= share;
        });

        // Chi ha pagato riceve l'importo intero (quindi incrementa saldo)
        balances[expense.payer] += exp.amount;
    });

    // Ora separiamo i creditori e i debitori
    const creditors = [];
    const debtors = [];

    for (const person in balances) {
        const bal = balances[person];
        if (bal > 0.01) creditors.push({ person, amount: bal });
        else if (bal < -0.01) debtors.push({ person, amount: -bal });
    }

    const transactions = [];
    let i = 0, j = 0;

    while (i < debtors.length && j < creditors.length) {
        const debtor = debtors[i];
        const creditor = creditors[j];
        const minAmount = Math.min(debtor.amount, creditor.amount);

        transactions.push(`${debtor.person} deve pagare a ${creditor.person} €${minAmount.toFixed(2)}`);

        debtor.amount -= minAmount;
        creditor.amount -= minAmount;

        if (debtor.amount < 0.01) i++;
        if (creditor.amount < 0.01) j++;
    }

    if (transactions.length === 0) {
        balancesDiv.innerHTML = "<p>✅ Tutti sono in pari!</p>";
    } else {
        balancesDiv.innerHTML = "";
        transactions.forEach(t => {
            const p = document.createElement("p");
            p.textContent = t;
            balancesDiv.appendChild(p);
        });
    }
}

async function retriveCoinquys()
{
    const container = document.getElementById('participants-container');

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
    await retriveExpenses();
    updateTotal();
    calculateBalances();
});

document.getElementById("back-btn").addEventListener("click", async () => {
    try {
        const response = await fetch('https://localhost:8084/Expense/rest/client/backToHome', {
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

addBtn.addEventListener("click", addExpense);
calculateBtn.addEventListener("click", calculateBalances);