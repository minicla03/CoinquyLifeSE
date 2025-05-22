const expenses = [];
const coinquilini = [];
const houseId = localStorage.getItem("houseId");

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
    const createdBy = payerInput.value.trim(); // User ID of who created the expense
    const category = categorySelect.value;
    const selectedParticipants = Array.from(document.querySelectorAll('input[name="participants"]:checked')).map(input => input.value);

    console.log("Description:", description);
    console.log("Amount:", amount);
    console.log("Created By:", createdBy);
    console.log("Category:", category);
    console.log("Selected Participants:", selectedParticipants);

    if (!description) {
        alert("Descrizione non può essere vuota.");
        return;
    }
    if (!createdBy) {
        alert("Inserisci chi ha pagato.");
        return;
    }
    if (!category) {
        alert("Seleziona una categoria.");
        return;
    }
    if (isNaN(amount) || amount <= 0) {
        alert("Inserisci un importo valido maggiore di zero.");
        return;
    }
    if (selectedParticipants.length === 0) {
        alert("Seleziona almeno un partecipante.");
        return;
    }

    const expenseData = {
        description,
        amount,
        createdBy, // User ID of who created the expense
        category, // Assuming category is a string, you may need to adjust this based on your CategoryExpense structure
        houseId,
        participants: selectedParticipants,
        status: "PENDING" // Assuming the status is set to PENDING by default
    };

    fetch('/expense/createExpense', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(expenseData)
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
            renderExpenseList();
            updateTotal();
            balancesDiv.innerHTML = "<p>ℹ️ Nessun calcolo effettuato</p>";
        })
        .catch(error => {
            console.error('Errore:', error);
            alert('Si è verificato un errore durante la creazione della spesa.');
        });

    // Reset dei campi di input
    descriptionInput.value = "";
    amountInput.value = "";
    payerInput.value = "";
    categorySelect.value = "";
    document.querySelectorAll('input[name="participants"]:checked').forEach(el => el.checked = false);
}

function renderExpenseList() {
    expenseList.innerHTML = "";
    expenses.forEach((expense, index) => {
        const li = document.createElement("li");
        li.classList.toggle("paid", expense.status === "PAID");

        const detailsDiv = document.createElement("div");
        detailsDiv.classList.add("details");
        detailsDiv.innerHTML = `
            <span class="title">${expense.description}</span>
            <span class="meta">🍂 ${expense.category} • 🙋 ${expense.createdBy}</span>`;

        const amountDiv = document.createElement("div");
        amountDiv.classList.add("amount");
        amountDiv.textContent = `€${expense.amount.toFixed(2)}`;

        const paidBtn = document.createElement("button");
        paidBtn.textContent = expense.status === "PAID" ? "✅ Pagata" : "✔️ Saldata";
        paidBtn.disabled = expense.status === "PAID";
        paidBtn.classList.add("paid-btn");

        paidBtn.addEventListener("click", () => {
            expenses[index].status = "PAID";
            renderExpenseList();
            updateTotal();
        });

        li.appendChild(detailsDiv);
        li.appendChild(amountDiv);
        li.appendChild(paidBtn);

        expenseList.appendChild(li);
    });
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
            expenses.length = 0; // svuota array
            expenses.push(...data);
            renderExpenseList();
            updateTotal();
        })
        .catch(error => {
            console.error('Errore:', error);
            alert('Si è verificato un errore durante il recupero delle spese.');
        });
}

function updateTotal() {
    const total = expenses
        .filter(exp => exp.status !== "PAID")
        .reduce((sum, exp) => sum + exp.amount, 0);
    totalSpan.textContent = `€${total.toFixed(2)}`;
}

function calculateBalances() {
    if (expenses.length === 0) {
        balancesDiv.innerHTML = "<p>⚠️ Nessuna spesa registrata.</p>";
        return;
    }

    const unpaidExpenses = expenses.filter(expense => expense.status !== "PAID");

    if (unpaidExpenses.length === 0) {
        balancesDiv.innerHTML = "<p>✅ Tutte le spese sono state saldate!</p>";
        return;
    }

    const allParticipantsSet = new Set();
    unpaidExpenses.forEach(expense => {
        expense.participants.forEach(p => allParticipantsSet.add(p));
        allParticipantsSet.add(expense.createdBy);
    });
    const allParticipants = Array.from(allParticipantsSet);

    const balances = {};
    allParticipants.forEach(p => balances[p] = 0);

    unpaidExpenses.forEach(expense => {
        const n = expense.participants.length;
        const share = expense.amount / n;

        expense.participants.forEach(p => {
            balances[p] -= share;
        });

        balances[expense.createdBy] += expense.amount;
    });

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

    balancesDiv.innerHTML = transactions.length === 0 ? "<p>✅ Tutti sono in pari!</p>" : "";
    transactions.forEach(t => {
        const p = document.createElement("p");
        p.textContent = t;
        balancesDiv.appendChild(p);
    });
}

async function retriveCoinquys() {
    await fetch(`/auth/getuserByHouse?houseId=${encodeURIComponent(houseId)}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Errore durante il recupero dei coinquilini.');
            }
            return response.json();
        })
        .then(data => {
            coinquilini.length = 0;
            data.forEach(coinquilino => {
                coinquilini.push(coinquilino);
            });
            const container = document.getElementById('participants-container');
            container.innerHTML = "";
            coinquilini.forEach(coinquilino => {
                const checkbox = document.createElement('input');
                checkbox.type = 'checkbox';
                checkbox.name = 'participants';
                checkbox.value = coinquilino.id; // Assuming coinquilino has an ID
                checkbox.id = `participant-${coinquilino.id}`;

                const label = document.createElement('label');
                label.htmlFor = checkbox.id;
                label.textContent = coinquilino.name;

                container.appendChild(checkbox);
                container.appendChild(label);
                container.appendChild(document.createElement('br'));
            });
        })
        .catch(error => {
            console.error('Errore:', error);
            alert('Si è verificato un errore durante il recupero dei coinquilini.');
        });
}

document.addEventListener('DOMContentLoaded', async () => {
    await retriveCoinquys();
    await retriveExpenses();
    updateTotal();
    calculateBalances();
});

addBtn.addEventListener("click", addExpense);
calculateBtn.addEventListener("click", calculateBalances);
