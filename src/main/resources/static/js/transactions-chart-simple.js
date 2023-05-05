function createChart(chartData, canvaId) {

    const labels = chartData.map(function (data) {
        return data.label;
    });
    const amounts = chartData.map(function (data) {
        return data.amount;
    });

    let backgroundColor = ['#39e007', '#0096f8', '#a000f1', '#00f5f5', '#f971b9', '#009a7a'];
    const backgroundColorOutCome = ['#ff0025', '#ffb200', '#ff4600', '#c77fe1', '#f971b9', '#009a7a'];
    if(canvaId==="chartOutcome"){
        backgroundColor=backgroundColorOutCome;
    }

    const ctx = document.getElementById(canvaId).getContext('2d');
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
            legend: {
                labels: {
                    fontColor: 'white'
                }
            }
        }
    });
}