function createChartBar(transactionLineChartDataList, months, canvasId, title) {

    const chartData = {
        labels: months,
        datasets: []
    };

    const { groupedTransactions, balanceData } = groupTransactionsByTypeAndMonth(transactionLineChartDataList);

    Object.keys(groupedTransactions).forEach((transactionType) => {
        const transactionTotalAmounts = [];

        months.forEach((month) => {
            const monthIndex = months.indexOf(month) + 1;
            const transactionTotalAmount = groupedTransactions[transactionType][monthIndex] || 0;

            transactionTotalAmounts.push(transactionTotalAmount);
        });

        let randomColor;
        let label;
        if (transactionType === 'INCOME') {
            randomColor = getRandomIncomeColor();
            label = 'Revenus'
        } else {
            randomColor = getRandomExpenseColor();
            label = 'Dépenses'
        }

        const dataset = {
            label: label,
            data: transactionTotalAmounts,
            backgroundColor: randomColor,
            borderColor: randomColor,
            fill: false,
        };

        chartData.datasets.push(dataset);
    });


    // Ajouter le solde
    const datasetBalance = {
        label: 'Solde',
        data: balanceData,
        type: 'bar',
        backgroundColor:"#ffffff",
    };


    chartData.datasets.push(datasetBalance)

    const ctx = document.getElementById(canvasId).getContext('2d');
    Chart.defaults.global.defaultFontColor = '#FFFFFF';
    new Chart(ctx, {

        type: 'bar',
        data: chartData,
        options: {
            maintainAspectRatio: false,
            responsive: true,
            scales: {
                x: {
                    scaleLabel: {
                        display: true,
                        labelString: 'Mois'
                    }
                },
                y: {
                    scaleLabel: {
                        display: true,
                        labelString: 'Montant'
                    }
                },

                grid: {
                    /*     backgroundColor: '#8f3838',*/
                    drawOnChartArea: false, // only want the grid lines for one axis to show up

                },
            },
            title: {
                display: true,
                text: title
            },
        }

    });
}

function groupTransactionsByTypeAndMonth(transactionLineChartDataList) {
    const groupedTransactions = {};
    const balanceMonth = {};

    transactionLineChartDataList.forEach((transaction) => {
        const transactionMonth = transaction.transactionMonth;
        const transactionType = transaction.type;
        const transactionAmount = transaction.transactionTotalAmountOfMonth;

        if (!groupedTransactions[transactionType]) {
            groupedTransactions[transactionType] = {};
        }
        if (!groupedTransactions[transactionType][transactionMonth]) {
            groupedTransactions[transactionType][transactionMonth] = 0;
        }
        groupedTransactions[transactionType][transactionMonth] += transactionAmount;
        if (!balanceMonth[transactionMonth]) {
            balanceMonth[transactionMonth] = 0;
        }
        if (transactionType === 'INCOME') {
            balanceMonth[transactionMonth] += transactionAmount;
        } else {
            balanceMonth[transactionMonth] -= transactionAmount;
        }


    });
    console.log(balanceMonth);
    console.log(groupedTransactions);

    const balanceData = Object.keys(balanceMonth).map((month) => balanceMonth[month]);
    return { groupedTransactions, balanceData };
}

function getRandomColor() {
    const letters = '0123456789ABCDEF';
    let color = '#';
    for (let i = 0; i < 6; i++) {
        color += letters[Math.floor(Math.random() * 16)];
    }
    return color;
}

// Fonction qui retourne une couleur aléatoire proche du rouge/orange pour les dépenses
function getRandomExpenseColor() {
    const hue = Math.floor(Math.random() * 31) + 15; // Hue entre 15 et 45 (rouge/orange)
    const saturation = Math.floor(Math.random() * 41) + 50; // Saturation entre 50 et 90
    const lightness = Math.floor(Math.random() * 21) + 40; // Luminosité entre 40 et 60
    return `hsl(${hue}, ${saturation}%, ${lightness}%)`;
}

// Fonction qui retourne une couleur aléatoire proche du vert/bleu pour les revenus
function getRandomIncomeColor() {
    const hue = Math.floor(Math.random() * 101) + 160; // Hue entre 160 et 260 (vert/bleu)
    const saturation = Math.floor(Math.random() * 41) + 50; // Saturation entre 50 et 90
    const lightness = Math.floor(Math.random() * 21) + 40; // Luminosité entre 40 et 60
    return `hsl(${hue}, ${saturation}%, ${lightness}%)`;
}