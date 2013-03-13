function buildInstance(cluster, instance, operations){
	Ext.get(instance).dom.innerHTML = '';
	instanceTpl.append(Ext.get(instance).dom, cluster[instance]);
	
	var anchors = Ext.get(instance).query('a');
	var size = instance.length;
	var type = instance.substring(0, size - 1);
	anchors.each(function(a){ 
		a.href = a.href + '$' + type + '$' + operations.join('$');
		var elm = new Ext.dom.Element(a);
		elm.on('click', getInstanceInfo);
	});
}

function getMemberInfo(event, anchor){
	event.stopEvent();
	var link = anchor.href;
	var key = link.substring(link.lastIndexOf('/')+1);
	var member = memberData(key);
	member;
	member.load(onMemberLoadHandler);
}

function onMemberLoadHandler(record, operation, success){
	if(success){
		var member = record[0].data;		
		var mainTab = Ext.getCmp('main');
		
		if(!Ext.getCmp(member.label)){
			var tab = buildTab(member);
			tab.on('close', function(){ tab.destroy();});
			mainTab.add(tab);
			mainTab.setActiveTab(member.label);
			
			runtimeTpl.append(Ext.get('member-runtime-' + member.label).dom, member.runtime);
			
			Ext.getCmp('sysProps-cmp' + member.label).on('activate', function(event, elm){
				var sysPropsTable = buildSysPropsTable(member.systemProps);
				Ext.get('member-sysProps-' + member.label).dom.appendChild(sysPropsTable);
				Ext.getCmp('sysProps-cmp' + member.label).un('activate', arguments.callee);
			});
			
			Ext.getCmp('partitions-cmp-' + member.label).on('activate', function(event, elm){
				partitionsTpl.append(Ext.get('member-partitions-'+ member.label).dom, member.partitions);
				Ext.getCmp('partitions-cmp-'+ member.label).un('activate', arguments.callee);
			});
			Ext.getCmp('member-config-cmp-' + member.label).on('activate', function(event, elm){
				var pre = $.create('pre');
				var content = $.text(member.memberConfig);
				pre.appendChild(content);
				Ext.get('member-config-' + member.label).dom.appendChild(pre);
				Ext.getCmp('member-config-cmp-' + member.label).un('activate', arguments.callee);
			});
		}else{
			mainTab.setActiveTab(member.label);
		}
	}
}

function buildMemoryChartPanel(mapStat){
	return Ext.create('Ext.panel.Panel',{
		title : mapStat.instance,
		id : mapStat.domId,
		closable : mapStat,
		layout : 'fit',
		tbar : [
		    '->',
		    {
		    	text : 'Browse',
		    	handler : function(){
		    		 var win = Ext.create('Ext.window.Window', {
		    			 title : 'Map Broswer',
		    			 width : 700,
		    			 height : 200,
		    			 layout : 'border',
		    			 resizable : false,
		    			 items : [
		    			     {
		    			    	 xtype : 'panel',
		    			    	 region : 'north',
		    			    	 layout : 'column',
		    			    	 frame : false,
		    			    	 items : [
		    			    		 {
		    			    			 xtype : 'textfield',
		    			    			 fieldLabel : 'Key:',
		    			    			 id : 'dataKey',
		    			    			 labelWidth : 40,
		    			    			 allowBlank : false,
		    			    			 columWidth : .4
		    			    		 },{
		    			    			 xtype : 'combo',
		    			    			 id : 'dataType',
		    			    			 store : ['string', 'integer', 'long'],
		    			    			 editable : false,
		    			    			 columnWidth : .3
		    			    		 },{
		    			    			 xtype : 'button',
		    			    			 text : 'Browse',
		    			    			 columnWidth : .2,
		    			    			 handler : function(){
		    			    				var name =  mapStat.instance;
		    			    				var key = Ext.getCmp('dataKey').getValue();
		    			    				var type = Ext.getCmp('dataType').getValue();
		    			    				var entryData = mapEntry(name, type, key);
		    			    				entryData.load(onEntryDataLoadHandler);
		    			    			 }
		    			    		 }
		    			    	 ]
		    			     },{
		    			    	 xtype : 'panel',
		    			    	 region : 'center',
		    			    	 html : '<div id="entry_data"></div>'
		    			     }
		    			 ]
		    			 
		    		 });
		    		 win.show();
		    	}
		    },{
		    	text : 'Config',
		    	handler : function(){
		    		Ext.MessageBox.alert('Not yet implemented');
		    	}
		    }
		],
		items: [
		        { 
		        	html : '<div><div><span id="' + mapStat.domId + '-chart"></span>'
		        		   + '<span id="' + mapStat.domId + '-throughChart"></span></div>'
		        		   + '<div id="' + mapStat.domId + '-memoryTable" class="chartTable"></div>'
		        		   + '<div id="' + mapStat.domId + '-throughTable" class="chartTable"></div>'
		        		   + '</div>'
		        }
		]
	});
}

function buildTab(member){
	return Ext.create('Ext.tab.Panel', {
		xtype : 'tabpanel',
		activeTab : 0,
		title : member.label,
		id : member.label,
		closable : true,
		items : [
		     {	 id : 'runtime-cmp-' + member.label,
		    	 title :'Runtime',
		    	 html : '<div class="member-runtime" id="member-runtime-' + member.label + '"></div>'
		     },{
		    	 id : 'sysProps-cmp' + member.label, 
		    	 title : 'Properties',
		    	 autoScroll : true,
		    	 html : '<div class="member-sysProps" id="member-sysProps-' + member.label + '"></div>'
		     },
		     {
		    	 id : 'member-config-cmp-' + member.label,
		    	 title : 'Configuration',
		    	 autoScroll : true,
		    	 html : '<div class="member-config" id="member-config-' + member.label + '"></div>'
		     },{
		    	 id : 'partitions-cmp-' + member.label,
		    	 title : 'partitions',
		    	 html : '<div class="member-partitions" id="member-partitions-' + member.label + '"></div>'
		     }
		]
	});
}

function getInstanceInfo(event, a){
	event.stopEvent();
	var link = a.href;
	var keys = link.substring(link.lastIndexOf('/')+1).split('$');
	var key = 'type=' + keys[1].capitalize() + '&instance=' + keys[0];
	var chartOp = key + '&property=' + keys[2];
	var tableOp = key +  '&property=' + keys[3];
	
	
	if(keys[1] === 'map'){
		var stats = mapMemoryTable('operation=iTable&' + tableOp, keys[1]);
		stats.instance = keys[0];
		stats.domId = stats.instance + '_' + keys[1];
		stats.container = stats.domId + '-memoryTable';
		stats.builder = buildMemoryTable;
		
		var throughputTable = mapThrougutTable('operation=oTable&' + tableOp, keys[1]);
		throughputTable.instance = keys[0];
		throughputTable.domId = throughputTable.instance + '_' + keys[1];
		throughputTable.container = throughputTable.domId + '-throughTable';
		throughputTable.builder = buildThroughputTable;
		
		stats.load(onMapStatsLoadHandler);
		throughputTable.load(onMapStatsLoadHandler);
	}
	
	var memoryChart = mapChartTable('operation=iChart&' + chartOp, keys[1]);
	memoryChart.instance = keys[0];
	memoryChart.domId = memoryChart.instance + '_' + keys[1];
	memoryChart.container = memoryChart.domId + '-chart';
	memoryChart.builder = buildChart;
	memoryChart.max = 10;
	
	var memoryThroughput = mapThroughputChartTable('operation=oChart&' + tableOp, keys[1]);
	memoryThroughput.instance = keys[0];
	memoryThroughput.domId = memoryThroughput.instance + '_' + keys[1];
	memoryThroughput.container = memoryThroughput.domId + '-throughChart';
	memoryThroughput.builder = buildChart;
	memoryThroughput.max = 10000;
	memoryThroughput.fireActivate = true;
	
	memoryChart.load(onChartLoadHandler);
	memoryThroughput.load(onChartLoadHandler);
}

function onMapStatsLoadHandler(record, operation, success){
	var store = this;
	if(success){
		console.log(store.instance + '-' + store.container);
		var mapStats = record;
		var mainTab = Ext.getCmp('main');
		var mapStatsTab = undefined;
		
		if(!Ext.getCmp(store.domId)){
			mapStats.instance = store.instance;
			mapStats.domId = store.domId;
			mapStatsTab = buildMemoryChartPanel(mapStats);
			mainTab.add(mapStatsTab);
			mainTab.setActiveTab(mapStats.domId);
			
		}else{
			mapStatsTab = Ext.getCmp(store.domId);
		}
		var memoryTable = this.builder(mapStats);
		
		Ext.get(this.container).dom.innerHTML = '';
		Ext.get(this.container).dom.appendChild(memoryTable);
		if(!store.live){
			mapStatsTab.on('activate', function(){
				store.statsIntervalId = setInterval(function(){
					store.reload();
				}, 5000);
				console.log("activate " + store.domId + ", interval :" + store.statsIntervalId);
			});
			
			mapStatsTab.on('deactivate', function(){
				clearInterval(store.statsIntervalId);
				console.log("deactivate " + store.domId + ", interval :" + store.statsIntervalId);
			});
			
			mapStatsTab.on('close',function(){
				clearInterval(store.statsIntervalId);
				console.log("deactivate " + store.domId + ", interval :" + store.statsIntervalId);
				mapStatsTab.destroy();
			});
			store.live = true;
		}
		mainTab.setActiveTab(store.domId);
		
	}
}
function onChartLoadHandler(record, operation, success){
	var store = this;
	if(success && !store.chart){	
		var mapStatsTab = Ext.getCmp(store.domId);
		
		if(!mapStatsTab){
			var mainTab = Ext.getCmp('main');
			mapStatsTab = buildMemoryChartPanel(store);
			mainTab.add(mapStatsTab);
			mainTab.setActiveTab(store.domId);
		}
		
		
		store.fields = Object.keys(record[0].data);
		var chart = store.builder(store);
		var container = Ext.get(store.container).dom;
		container.innerHTML = '';
		chart.render(container);
		
		if(!store.chart){
			mapStatsTab.on('activate', function(){
				store.chartIntervalId = setInterval(function(){
					store.reload();
				}, 5000);
				console.log("activate " + store.domId + ", interval :" + store.chartIntervalId);
			});
			
			mapStatsTab.on('deactivate', function(){
				clearInterval(store.chartIntervalId);
				console.log("deactivate " + store.domId + ", interval :" + store.chartIntervalId);
			});
			
			mapStatsTab.on('close',function(){
				clearInterval(store.chartIntervalId);
				console.log("deactivate " + store.domId + ", interval :" + store.chartIntervalId);
				mapStatsTab.destroy();
			});
		}
		store.chart = chart;
		if(store.fireActivate){
			mapStatsTab.fireEvent('activate');
		}
	}
}


function buildMemoryTable(mapStats){
	var heading = ["#", "Members", "Entries", "Entry Memory", "Backups", "Backup Memory", "Dirty Entries", "Marked As Remove", "Marked As Remove Memory", "Locks"]
	var table = $.create('table');
	
	var thead = $.create('thead');
	var tr = $.create('tr');
	heading.each(function(val){
		var td = $.create('th');
		td.appendChild($.text(val));
		tr.appendChild(td);
		thead.appendChild(tr);
	});
	table.appendChild(thead);
	
	mapStats.each(function(data){
		var row = data.data;
		var tr = $.create('tr');
		var count = 0;
		for(p in row){
			var td = $.create('td');
			if(count == 1){
				var a = $.create('a');
				a.href = row[p];
				a.appendChild($.text(row[p]));
				new Ext.dom.Element(a).on('click', getMemberInfo);
				td.appendChild(a);
			}else{
				td.appendChild($.text(row[p]));
			}
			tr.appendChild(td);
			count++;
		}
		table.appendChild(tr);
	});
	var div = $.create('div');
	var h1 = $.create('h1');
	h1.appendChild($.text('Map Memory Data Table'));
	div.appendChild(h1);
	div.appendChild(table);
	return div;
}

function buildThroughputTable(mapStats){
	var heading = ['#', 'Members', 'Total', 'Gets', 'Ave Get Latency', 'Puts', 'Ave Put Latency', 'Removes', 'Ave Remove Latency', 'Other Ops', 'Events'];
	var table = $.create('table');
	
	var thead = $.create('thead');
	var tr = $.create('tr');
	heading.each(function(val){
		var td = $.create('th');
		td.appendChild($.text(val));
		tr.appendChild(td);
		thead.appendChild(tr);
	});
	table.appendChild(thead);
	
	
	mapStats.each(function(data){
		var row = data.data;
		var tr = $.create('tr');
		var count = 0;
		for(p in row){
			var td = $.create('td');
			if(count == 1){
				var a = $.create('a');
				a.href = row[p];
				a.appendChild($.text(row[p]));
				new Ext.dom.Element(a).on('click', getMemberInfo);
				td.appendChild(a);
			}else{
				td.appendChild($.text(row[p]));
			}
			tr.appendChild(td);
			count++;
		}
		table.appendChild(tr);
	});
	var div = $.create('div');
	var h1 = $.create('h1');
	h1.appendChild($.text('Map Throughput Data Table'));
	div.appendChild(h1);
	div.appendChild(table);
	return div;
}

function buildSysPropsTable(sysProps){
	var table = $.create('table');
	
	var keys = Object.keys(sysProps);
	keys.each(function(key){
		var tr = $.create('tr');
		var td1 = $.create('td');
		var td2 = $.create('td');
		td2.className = 'value';
		
		td1.appendChild($.text(key + ':'));
		td2.appendChild($.text(sysProps[key]));
		tr.appendChild(td1);
		tr.appendChild(td2);
		table.appendChild(tr);
	});
	return table;
}

function buildChart(store){
	return Ext.create('Ext.chart.Chart', {
	    xtype: 'chart',
	    style: 'background:#fff',
	    animate: true,
	    store: store,
	    shadow: true,
	    height : 250,
	    width : 400,
	    theme: 'Category1',
	    legend: {
	        position: 'right'
	    },
	    axes: [{
	        type: 'Numeric',
	        minimum: 0,
	        maximum: store.max,
	        position: 'left',
	        fields: [store.fields[0]],
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
	        fields: ['time'],
	    }],
	    series: [{
	        type: 'line',
	        highlight: {
	            size: 7,
	            radius: 7
	        },
	        axis: 'left',
	        xField: 'time',
	        yField: store.fields[0],
	        markerConfig: {
	            type: 'circle',
	            size: 4,
	            radius: 4,
	            'stroke-width': 0
	        }
	     }]
	});

}

function onEntryDataLoadHandler(record, operation, success){
	if(success){
		var div = Ext.get('entry_data').dom;
		mapEntryTpl.append(div, record[0].data);
	}
	
}
