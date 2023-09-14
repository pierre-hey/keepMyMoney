function createChartLine(transactionLineChartDataList, months, canvasId, title) {

    const chartData = {
        labels: months,
        datasets: []
    };

    const groupedTransactions = groupTransactionsByCategoryAndMonth(transactionLineChartDataList);

    Object.keys(groupedTransactions).forEach((categoryLabel) => {
        const transactionTotalAmounts = [];

        months.forEach((month) => {
            const monthIndex = months.indexOf(month) + 1;
            const transactionTotalAmount = groupedTransactions[categoryLabel][monthIndex] || 0;
            transactionTotalAmounts.push(transactionTotalAmount);
        });

        const randomColor = getRandomColor();
        const dataset = {
            label: categoryLabel,
            data: transactionTotalAmounts,
            backgroundColor: randomColor,
            borderColor: randomColor,
            fill: false,
        };

        chartData.datasets.push(dataset);
    });

    const ctx = document.getElementById(canvasId).getContext('2d');
    Chart.defaults.global.defaultFontColor = '#FFFFFF';
    new Chart(ctx, {

        type: 'line',
        data: chartData,
        options: {
            maintainAspectRatio: false,
            responsive: true,
            scales: {
                xAxes: [{
                    scaleLabel: {
                        display: true,
                        labelString: 'Mois'
                    }
                }],
                yAxes: [{
                    scaleLabel: {
                        display: true,
                        labelString: 'Montant'
                    },
                    ticks: {
                        beginAtZero: true
                    }
                }],
                y: {
                    beginAtZero: true
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

function groupTransactionsByCategoryAndMonth(transactionLineChartDataList) {
    const groupedTransactions = {};

    transactionLineChartDataList.forEach((transaction) => {
        const transactionMonth = transaction.transactionMonth;
        if (!groupedTransactions[transaction.categoryLabel]) {
            groupedTransactions[transaction.categoryLabel] = {};
        }
        if (!groupedTransactions[transaction.categoryLabel][transactionMonth]) {
            groupedTransactions[transaction.categoryLabel][transactionMonth] = 0;
        }
        groupedTransactions[transaction.categoryLabel][transactionMonth] += transaction.transactionTotalAmountOfMonth;
    });
    /*console.log(groupedTransactions);*/
    return groupedTransactions;
}

function getRandomColor() {
    const letters = '0123456789ABCDEF';
    let color = '#';
    for (let i = 0; i < 6; i++) {
        color += letters[Math.floor(Math.random() * 16)];
    }
    return color;
}