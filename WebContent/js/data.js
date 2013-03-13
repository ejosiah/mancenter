clusterData = Ext.create('Ext.data.JsonStore',{
	fields : ['members', 'executors', 'maps', 'multiMaps', 'queues', 'topics', 'runtime'],
	proxy : {
		type : 'ajax',
		url : 'main.do?operation=cluster',
		reader :{
			type : 'json',
			successProperty : 'meta.successful'
		}
	}
});

/*
mapStatsData = memorize(function(query){
	return Ext.create('Ext.data.JsonStore', {
		fields : ['instance', 'memoryDataTable', 'throughputDataTable', 'memoryChart', 'throughputChart'],
		proxy : {
			type : 'ajax',
			url : 'main.do?' + query,
			reader :{
				type : 'json',
				root : 'mapStats',
				successProperty : 'meta.successful'
			}
		}
	});
});*/

mapMemoryTable = memorize(function(query, type){
	return Ext.create('Ext.data.JsonStore', {
		fields : ['id', 'member', 'entry', 'entry_memory', 'backups', 'backup_memory', 'dirty', 'marked', 'marked_mem', 'lock'],
		proxy : {
			type : 'ajax',
			url : 'main.do?' + query,
			reader :{
				type : 'json',
				root : 'mapStats.rows',
				successProperty : 'meta.successful'
			}
		}
	});
});

mapThrougutTable = memorize(function(query, type){
	return Ext.create('Ext.data.JsonStore', {
		fields : ['id', 'member', 'total', 'gets', 'avg_get_letency', 'puts', 'avg_put_lentency', 'removes', 'avg_remove_le', 'other_opts', 'events'],
		proxy : {
			type : 'ajax',
			url : 'main.do?' + query,
			reader :{
				type : 'json',
				root : 'mapStats.rows',
				successProperty : 'meta.successful'
			}
		}
	});
});

mapChartTable = memorize(function(query, type){
	return Ext.create('Ext.data.JsonStore', {
		fields : ['Size', 'time'],
		proxy : {
			type : 'ajax',
			url : 'main.do?' + query,
			reader :{
				type : 'json',
				root : 'chart.rows',
				successProperty : 'meta.successful'
			}
		}
	});
});

mapThroughputChartTable = memorize(function(query, type){
	return Ext.create('Ext.data.JsonStore', {
		fields : ['Throughput', 'time'],
		proxy : {
			type : 'ajax',
			url : 'main.do?' + query,
			reader :{
				type : 'json',
				root : 'chart.rows',
				successProperty : 'meta.successful'
			}
		}
	});
});

memberData = memorize(function(value){
	return Ext.create('Ext.data.JsonStore',{
		fields : ['label', 'runtime', 'partitions', 'systemProps', 'memberConfig'],
		proxy : {
			type : 'ajax',
			url : 'main.do?operation=member&key=' + value,
			reader : {
				type : 'json',
				root : 'member',
				successProperty :' meta.successful'
			}
		}
	});
});

pieData = Ext.create('Ext.data.JsonStore',{
	fields : ['label', 'data'],
	proxy : {
		type : 'ajax',
		url : 'main.do?operation=pie',
		reader : {
			type : 'json',
			root : 'members',
			successProperty : 'meta.success'
		}
	}
});

pieData.load({
	callback: function(records, op, success){
		if(success){
		//	console.log("members: " + records[0]);
		}else{
		//	console.log("the server reported an error")
		}
	}
});

mapEntry = memorize(function(name, type, key){
	return Ext.create('Ext.data.JsonStore',{
		fields : ['valid', 'clazz', 'hits', 'value', 'version', 'accessTime',
		          'creationTime', 'expirationTime', 'updateTime', 'cost'],
		proxy : {
			type : 'ajax',
			url : 'main.do?operation=browseMap&name=' + name + '&type=' + type + '&key=' + key,
			reader : {
				type : 'json',
				root : 'entry',
				successProperty : 'meta.success'
			}
		}
	})
});