function createChartBar(transactionLineChartDataList, months, canvasId, title) {

    const chartData = {
        labels: months,
        datasets: []
    };

    // Récupération des transactions par type et du solde pour chaque mois
    const {groupedTransactions, balanceMonth} = groupTransactionsByTypeAndMonth(transactionLineChartDataList);
    createTransactionDataSet(groupedTransactions, months, chartData);
    createBalanceDataSet(balanceMonth, months, chartData);

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

/**
 * Regroupe les transactions par type et par mois et génère le solde mensuel
 * @param transactionLineChartDataList donnée brut
 * @returns {{balanceData: *[], groupedTransactions: {}}} solde mensuel et transactions regroupées
 */
function groupTransactionsByTypeAndMonth(transactionLineChartDataList) {
// Groupement des transactions par type et par mois
    const groupedTransactions = {};
    const balanceMonth = {};

    transactionLineChartDataList.forEach((transaction) => {
        const {transactionMonth, type, transactionTotalAmountOfMonth} = transaction;
        const transactionAmount = Number(transactionTotalAmountOfMonth);

        // Création des clés pour le groupement par type
        if (!groupedTransactions[type]) {
            groupedTransactions[type] = {};
        }
        if (!groupedTransactions[type][transactionMonth]) {
            groupedTransactions[type][transactionMonth] = 0;
        }

        // Calcul du montant total pour chaque type et chaque mois
        groupedTransactions[type][transactionMonth] += transactionAmount;

        // Calcul du solde pour chaque mois
        if (!balanceMonth[transactionMonth]) {
            balanceMonth[transactionMonth] = 0;
        }
        balanceMonth[transactionMonth] += type === 'INCOME' ? transactionAmount : -transactionAmount;
    });

    console.log(balanceMonth);
    console.log(groupedTransactions);

   // const balanceData = Object.keys(balanceMonth).map((month) => balanceMonth[month]);
    return {groupedTransactions, balanceMonth};
}


/**
 * Créer le dataset des transactions mensuel par type
 * @param groupedTransactions transactions regroupées
 * @param months liste de mois
 * @param chartData données du graphique
 */
function createTransactionDataSet(groupedTransactions, months, chartData) {

    Object.keys(groupedTransactions).forEach((transactionType) => {
        const transactionTotalAmounts = [];
        let backgroundColor;
        let borderColor;
        let label;

        months.forEach((month) => {
            const monthIndex = months.indexOf(month) + 1;
            let transactionTotalAmount = groupedTransactions[transactionType][monthIndex] || 0;

/*            if(transactionType === 'SPENT'){
                transactionTotalAmount = transactionTotalAmount * - 1;
            }*/

            transactionTotalAmounts.push(transactionTotalAmount);
        });

        if (transactionType === 'INCOME') {
            backgroundColor = getRandomIncomeColor();
            borderColor = backgroundColor;
            label = 'Revenus'
        } else {
            backgroundColor = getRandomExpenseColor();
            borderColor = backgroundColor;
            label = 'Dépenses'
        }

        const dataset = {
            label: label,
            data: transactionTotalAmounts,
            backgroundColor: backgroundColor,
            borderColor: borderColor,
            fill: false,
        };

        chartData.datasets.push(dataset);
    });
}

/**
 * Créer le dataset du solde mensuel
 * @param balanceData donnée du solde
 * @param months mois
 * @param chartData données du graphique
 */
function createBalanceDataSet(balanceData, months, chartData) {
    const balanceMonthsTotal = [];

    Object.keys(chartData).forEach(() => {
        months.forEach((month) => {
            const monthIndex = months.indexOf(month) + 1;
            const balanceOfMonth = balanceData[monthIndex] || 0;

            console.log(balanceOfMonth);
            balanceMonthsTotal.push(balanceOfMonth);
        });
    });


    // Ajouter le solde
    const datasetBalance = {
        label: 'Solde',
        data: balanceMonthsTotal,
        type: 'bar',
        backgroundColor: balanceMonthsTotal.map((balance) => balance >= 0 ? '#00ff15' : '#ff0015')

    };

    chartData.datasets.push(datasetBalance);
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