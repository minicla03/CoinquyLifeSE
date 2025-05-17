
const expenses = [];

const titleInput = document.getElementById("title");
const amountInput = document.getElementById("amount");
const payerInput = document.getElementById("payer");
const categorySelect = document.getElementById("category");
const addBtn = document.getElementById("add-btn");
const expenseList = document.getElementById("expense-list");
const totalSpan = document.getElementById("total");
const calculateBtn = document.getElementById("calculate-btn");
const balancesDiv = document.getElementById("balances");

function addExpense() {
    const title = titleInput.value.trim();
    const amountStr = amountInput.value.trim().replace(",", ".");
    const amount = parseFloat(amountStr);
    const payer = payerInput.value.trim();
    const category = categorySelect.value;

    if (!title || !payer || !category || isNaN(amount) || amount <= 0) {
        alert("Per favore, compila tutti i campi correttamente.");
        return;
    }

    expenses.push({ title, amount, payer, category, paid: false });

    titleInput.value = "";
    amountInput.value = "";
    payerInput.value = "";
    categorySelect.value = "";

    renderExpenses();
    updateTotal();
    balancesDiv.innerHTML = "<p>ℹ️ Nessun calcolo effettuato</p>";
}

function renderExpenses() {
    expenseList.innerHTML = "";

    expenses.forEach((exp, index) => {
        const li = document.createElement("li");

        li.classList.toggle("paid", exp.paid); // aggiungi classe paid se pagata

        const detailsDiv = document.createElement("div");
        detailsDiv.classList.add("details");
        detailsDiv.innerHTML = `
            <span class="title">${exp.title}</span>
            <span class="meta">🍂 ${exp.category} • 🙋 ${exp.payer}</span>
        `;

        const amountDiv = document.createElement("div");
        amountDiv.classList.add("amount");
        amountDiv.textContent = `€${exp.amount.toFixed(2)}`;

        const paidBtn = document.createElement("button");
        paidBtn.textContent = exp.paid ? "✅ Pagata" : "✔️ Saldata";
        paidBtn.disabled = exp.paid;
        paidBtn.classList.add("paid-btn");

        paidBtn.addEventListener("click", () => {
            expenses[index].paid = true;
            renderExpenses();
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

    // Considera solo le spese NON pagate per il calcolo debiti
    const unpaidExpenses = expenses.filter(exp => !exp.paid);

    if (unpaidExpenses.length === 0) {
        balancesDiv.innerHTML = "<p>✅ Tutte le spese sono state saldate!</p>";
        return;
    }

    const participantsSet = new Set();
    unpaidExpenses.forEach(e => participantsSet.add(e.payer));
    const participants = Array.from(participantsSet);

    const spentMap = {};
    participants.forEach(p => (spentMap[p] = 0));
    unpaidExpenses.forEach(e => {
        spentMap[e.payer] += e.amount;
    });

    const total = unpaidExpenses.reduce((sum, e) => sum + e.amount, 0);
    const share = total / participants.length;

    const balances = {};
    participants.forEach(p => {
        balances[p] = spentMap[p] - share;
    });

    const creditors = [];
    const debtors = [];

    for (const p of participants) {
        const bal = balances[p];
        if (bal > 0.01) creditors.push({ person: p, amount: bal });
        else if (bal < -0.01) debtors.push({ person: p, amount: -bal });
    }

    const transactions = [];
    let i = 0;
    let j = 0;

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

addBtn.addEventListener("click", addExpense);
calculateBtn.addEventListener("click", calculateBalances);