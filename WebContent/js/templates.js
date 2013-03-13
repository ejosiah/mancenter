runtime = Ext.create('Ext.Template',[
	'<div id="left">' ,
		'<table border="0">' ,
			'<tr>' ,
				'<td><b>Version:<b/></td>',
				'<td>2.5-ee</td>',
			'</tr>',
			'<tr>',
				'<td><b>Max Memory:</b></td>',
				'<td>{maxMemory}</td>',
			'</tr>',
				'<tr>',
				'<td><b>Start Time:</b></td>',
				'<td>{startTime}</td>',
			'</tr>',
		'</table>',
	'</div>',
	'<div id="right">',
		'<table>',
			'<tr>',
				'<td><b>Build:<b/></td>',
				'<td></td>',
			'</tr>',
			'<tr>',
				'<td><b>Free Memory:</b></td>',
				'<td>{freeMemory}</td>',
			'</tr>',
			'<tr>',
				'<td><b>Up Time</b></td>',
				'<td>{upTime}</td>',
			'</tr>',
		'</table>',
	'</div>'
]);

runtime.compile();

membersTpl = Ext.create('Ext.XTemplate', [
    '<ul>',
    	'<tpl for=".">',
    		'<b><li><a class="member" href="{.}">{#}- {.}</a></li></b>',
    	'</tpl>',
    '</ul>'
]);

membersTpl.compile();

sysPropsTpl = Ext.create('Ext.XTemplate', [
    '<table>',
    	'<tpl for=".">',
    		'<tr><td>this.key({.})</td><td>this.value({.}, this.key({.}))</td></tr>',
    	'</tpl>',
    '</table>',
    {
    	key : function(obj){
    		return Object.keys(obj)[0];
    	},
    	value : function(obj, key){
    		return obj[key];
    	}
    }
]);

sysPropsTpl.compile();

runtimeTpl = Ext.create('Ext.Template', [
    '<table>',
    	'<tr>',
    		'<td><b>Number of Processors:</b></td>',
    		'<td>{availableProcessors}</td>',
    	'</tr>',
    	'<tr>',
	    	'<td><b>Start Time:</b></td>',
	    	'<td>{startTime}</td>',
    	'</tr>',
    	'<tr>',
    		'<td><b>Up Time:</b></td>',
    		'<td>{upTime}</td>',
    	'</tr>',
    	'<tr>',
    		'<td><b>Maximum Memory:</b></td>',
    		'<td>{maxMemory}</td>',
    	'</tr>',
    	'<tr>',
    		'<td><b>Total Memory:</b></td>',
    		'<td>{totalMemory}</td>',
    	'</tr>',
    	'<tr>',
    		'<td><b>Free Memory:</b></td>',
    		'<td>{freeMemory}</td>',
    	'</tr>',
    	'<tr>',
    		'<td><b>Used Heap Memory:</b></td>',
    		'<td>{heapMemoryUsed}</td>',
    	'</tr>',
    	'<tr>',
    		'<td><b>Max Heap Memory:</b></td>',
    		'<td>{heapMemoryMax}</td>',
    	'</tr>',
    	'<tr>',
    		'<td><b>Used Non-Heap Memory:</b></td>',
    		'<td>{noneHeapMemoryUsed}</td>',
    	'</tr>',
    	'<tr>',
    		'<td><b>Max Non-Heap Memory:</b></td>',
    		'<td>{noneHeapMemoryMax}</td>',
    	'</tr>',
    	'<tr>',
    		'<td><b>Total Loaded Classes:</b></td>',
    		'<td>{totalLoadedClassCount}</td>',
    	'</tr>',
    	'<tr>',
    		'<td><b>Current Loaded Classes:</b></td>',
    		'<td>{loadedClassCount}</td>',
    	'</tr>',
    	'<tr>',
    		'<td><b>Total unLoaded Classes:</b></td>',
    		'<td>{totalUnloadedClassCount}</td>',
    	'</tr>',
    	'<tr>',
    		'<td><b>Total Thread Count:</b></td>',
    		'<td>{totalStartedThreadCount}</td>',
    	'</tr>',
    	'<tr>',
    		'<td><b>Active Thread Count:</b></td>',
    		'<td>{threadCount}</td>',
    	'</tr>',
    	'<tr>',
    		'<td><b>Peak Thread Count:</b></td>',
    		'<td>{peakThreadCount}</td>',
    	'</tr>',
    	'<tr>',
    		'<td><b>Daemon Thread Count:</b></td>',
    		'<td>{daemonThreadCount}</td>',
    	'</tr>',
    '</table>'
]);
runtimeTpl.compile();

partitionsTpl = Ext.create('Ext.XTemplate',[
	'<table>',
		'<tr>',
		'<tpl for=".">',
			'<td>{.}</td>',
			'<tpl if="this.nextRow(xindex)">',
				'</tr><tr>',
			'</tpl>',
		'</tpl>',
	'</table>',
	{
		nextRow : function(count){
			return count %10 == 0;
		}
	}
]);
partitionsTpl.compile();


instanceTpl = Ext.create('Ext.XTemplate', [
  '<ul>',
  	'<tpl for=".">',
  		'<b><li><a href="{.}">{.}</a></li></b>',
  	'</tpl>',
  '</ul>'
 ]);

instanceTpl.compile();

mapEntryTpl = Ext.create('Ext.XTemplate', [
    '<table>',
    	'<tr>',
    		'<td class="title">Value:</td>',
    		'<td>{value}</td>',
    		'<td class="title">Class:</td>',
    		'<td>{clazz}</td>',
    	'</tr>',
    	'<tr>',
	    	'<td class="title">Cost:</td>',
	    	'<td>{cost}</td>',
	    	'<td class="title">Create Time:</td>',
	    	'<td>{creationTime}</td>',
    	'</tr>',
    	'<tr>',
	    	'<td class="title">Expiration Time:</td>',
	    	'<td>{expirationTime}</td>',
	    	'<td class="title">Hits:</td>',
	    	'<td>{hits}</td>',
    	'</tr>',
    	'<tr>',
	    	'<td class="title">Access Time:</td>',
	    	'<td>{accessTime}</td>',
	    	'<td class="title">Update Time:</td>',
	    	'<td>{updateTime}</td>',
    	'</tr>',
    	'<tr>',
	    	'<td class="title">Version:</td>',
	    	'<td>{version}</td>',
	    	'<td class="title">Valid:</td>',
	    	'<td>{valid}</td>',
    	'</tr>',
    '</table>'
]);

mapEntryTpl.compile();