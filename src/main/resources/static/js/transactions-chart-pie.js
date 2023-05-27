function createChartPie(chartData, canvasId, title) {

    const labels = chartData.map(function (data) {
        return data.label;
    });
    const amounts = chartData.map(function (data) {
        return data.amount;
    });

    let backgroundColor = ['#39e007', '#0096f8', '#a000f1', '#00f5f5', '#f971b9', '#009a7a'];
    const backgroundColorOutCome = ['#ff0025', '#ffb200', '#ff4600', '#c77fe1', '#f971b9', '#009a7a'];
    if (canvasId === "chartExpense") {
        backgroundColor = backgroundColorOutCome;
    }

    const ctx = document.getElementById(canvasId).getContext('2d');
    new Chart(ctx, {
        type: 'pie',
        data: {
            labels: labels,
            datasets: [{
                label: 'Transactions by Category',
                backgroundColor: backgroundColor,
                data: amounts
            }]
        },
        options: {
            maintainAspectRatio: false,
            responsive: true,
            title: {
                display: true,
                text: title,
                fontColor: 'white',
                fontSize: 25,
            },
            legend: {
                labels: {
                    fontColor: 'white',
                    fontSize: 16,
                },
            },

        }
    });
}