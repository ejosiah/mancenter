Ext.onReady(function () {
	var store1 = Ext.create('Ext.data.JsonStore', {
		fields : ['memoryChart'],
		proxy : {
			type : 'ajax',
			url : 'chart.do?',
			reader :{
				type : 'json',
				successProperty : 'meta.successful'
			}
		}
	});
	store1.load();
	var chart = Ext.create('Ext.chart.Chart', {
	    xtype: 'chart',
        style: 'background:#fff',
        animate: true,
        store: store1,
        shadow: true,
        theme: 'Category1',
        legend: {
            position: 'right'
        },
        axes: [{
            type: 'Numeric',
            minimum: 0,
            position: 'left',
            fields: ['memoryChart.rows.data'],
            title: 'Number of Hits',
            minorTickSteps: 1,
            grid: {
                odd: {
                    opacity: 1,
                    fill: '#ddd',
                    stroke: '#bbb',
                    'stroke-width': 0.5
                }
            }
        }, {
            type: 'Category',
            position: 'bottom',
            fields: ['memoryChart.rows.time'],
            title: 'Month of the Year'
        }],
        series: [{
            type: 'line',
            highlight: {
                size: 7,
                radius: 7
            },
            axis: 'left',
            xField: 'memoryChart.rows.data',
            yField: 'memoryChart.rows.time',
            markerConfig: {
                type: 'cross',
                size: 4,
                radius: 4,
                'stroke-width': 0
            }
        }]
    });

    var win = Ext.create('Ext.Window', {
        width: 800,
        height: 600,
        minHeight: 400,
        minWidth: 550,
        hidden: false,
        maximizable: true,
        title: 'Line Chart',
        renderTo: Ext.getBody(),
        layout: 'fit',
        tbar: [{
            text: 'Save Chart',
            handler: function() {
                Ext.MessageBox.confirm('Confirm Download', 'Would you like to download the chart as an image?', function(choice){
                    if(choice == 'yes'){
                        chart.save({
                            type: 'image/png'
                        });
                    }
                });
            }
        }, {
            text: 'Reload Data',
            handler: function() {
                store1.loadData();
            }
        }],
        items: chart
    });
    
});