Ext.onReady(function(){
	var chart = Ext.create('Ext.chart.Chart', {
		width : 430,
		height : 430,
		animate : true,
		shadow: true,
		store : pieData,
		legend: { position : 'right'},
		theme : 'Base:gradient',
		series: [
			{
				type : 'pie',
				field : 'data',
				showInLegend: true,
				highlight : {
					segment : {
						margin: 20
					}
				},
				label : {
					field: 'label',
					display: 'rotate',
					contrast: true,
					font: '14px Arial'
				},
				tips : {
					trackMouse : true,
					width : 160,
					height : 28,
					renderer : function(storeItem, item){
						var total = 0;
						pieData.each(function(rec){
							total += rec.get('data');
						});
						this.setTitle(storeItem.get('label') + ': ' 
								+ Math.round(storeItem.get('data') / total * 100) + '%');
					}
				}
			}
		]
	});

	var clusterHome = {
		id : 'cluster-home',
		title : 'Cluster Home',
		closable : true,
		html : 	'<div><div id="stats"></div><div id="pie"><h2>Members & Partitions</h2></div></div>'
	};

	Ext.create('Ext.Viewport', {
		layout : 'border',
		defaults : {
			frame : false,
			split : true
		},
		items : [
			{
				xtype : 'panel',
				defaultType : 'button',
				title : 'Hazelcast Man center',
				region : 'north',
				height : 100,
				layout : 'column',
				bbar : [
				        '->',
				     {
				    	 text : 'home',
				    	 handler : function(){
				    		 clusterHome.fireEvent('deactivate');
				    		 Ext.getCmp('content').getLayout().setActiveItem(0);
				    	 }
				     }, '-' ,{
				    	 text : 'Sytem Logs',
				    	 handler : function(){ 
				    		 Ext.getCmp('content').getLayout().setActiveItem(2);
				    	 }
				     }, '-' ,{
				    	 text : 'Scripting',
				    	 handler : function(){ 
				    		 Ext.MessageBox.alert('Not yet implemented');
				    	 }
				     }, '-' ,{
				    	 text : 'Docs',
				    	 handler : function(){ 
				    		 Ext.getCmp('content').getLayout().setActiveItem(1);
				    	 }
				     }, '-' ,{
				    	 text : 'Alerts',
				    	 handler : function(){ Ext.MessageBox.alert('Not yet implemented');}
				     }, '-' ,{
				    	 text : 'Administration',
				    	 handler : function(){ Ext.MessageBox.alert('Not yet implemented');}
				     }         
				],
				tools : [
				    {
				    	type : 'gear',
				    	handler : function(){
				    		var activeCmp = Ext.getCmp('content').getLayout().getActiveItem();
				    		var mainCmp = Ext.getCmp('main_panel');
				    		if(activeCmp === mainCmp){
					    		 if(!Ext.get('stats')){
					    			 Ext.getCmp('main').add(clusterHome);
					    		 }
					    		 Ext.getCmp('main').setActiveTab('cluster-home');
					    		 clusterHome.fireEvent('activate');
				    		}
				    	}
				    },{
				    	type : 'refresh',
				    	handler : function(){
				    		window.location = window.location;
				    	}
				    },
				    {
				    	type : 'help',
				    	handler : function(){
				    		Ext.MessageBox.alert('Developed by Josiah Ebhomenye');
				    	}
				    }
				]
				
			},
			{
				xtype : 'panel',
				region : 'center',
				layout : 'card',
				id : 'content',
				tbar : [
				        '->',
				     {
				    	 text : 'Thread Dump',
				    	 handler : function() { Ext.MessageBox.alert('Not yet implemented');}
				     },
				     '-',
				     {
				    	 text : 'Run GC',
				    	 handler :  function() { Ext.MessageBox.alert('Not yet implemented');}
				     }
				 ],
				items : [
					{
						xtype : 'panel',
						layout : 'border',
						id : 'main_panel',
						items: [
							{
								xtype : 'container',
								title : 'West Panel',
								region : 'west',
								id: 'member-list',
								layout :{
									type : 'accordion',
									animate : true,
									fill : false,
									multi : true
								},
								defaults : {
									minHeight : 100
								},
								width : 200,
								items : [
									{
										id : 'mapCmp',
										xtype : 'panel',
										title : 'Maps',
										html : '<div id="maps"></div>'
									},{
										id : 'queueCmp',
										xtype : 'panel',
										title : 'Queues',
										html : '<div id="queues"></div>'
									},{
										id : 'topicCmp',
										xtype : 'panel',
										title : 'Topics',
										html : '<div id="topics"></div>'
									},{
										id : 'executorsCmp',
										xtype : 'panel',
										title : 'Executors',
										html : '<div id="executors"></div>'						
									}
									,{
										id : 'members',
										title : 'Members',
										layout : 'fit',
										height : 800,
										items : [
											{html : '<div id="memberlist"></div>'},
										]
									}
								]
							},
							{
								xtype : 'tabpanel',
								region : 'center',
								activeTab : 0,
								id : 'main',
								autoDestroy : false,
								enableTabScroll : true,
								items : [
									clusterHome
								]
							}
					    ]
					},{
						xtype : 'panel',
						id : 'doc',
						layout : 'fit',
						autoScroll : true,
						autoLoad : {
							url : 'documentation.html'
						}
					},{
						xtype : 'panel',
						id : 'sys_logs',
						layout : 'border',
						items : [
						    {
						    	xtype : 'panel',
						    	region : 'west',
						    	title : 'Fiter Logs',
						    	html : 'TODO'
						    },{
						    	xtype : 'panel',
						    	title : 'System Logs',
						    	region : 'center',
						    	html : 'TODO'
						    }
						]
					}
				]
			}
		]
	}).show();
	Ext.getCmp('members').expand();
	Ext.getCmp('mapCmp').expand();
	Ext.getCmp('queueCmp').collapse();
	Ext.getCmp('topicCmp').collapse();
	Ext.getCmp('executorsCmp').collapse();

	var pie = Ext.get('pie').dom;
	chart.render(pie);
	
	var clusterHome = Ext.getCmp('cluster-home');
	clusterHome.intervalId = setInterval( function(){ pieData.reload();}, 5000);
	clusterHome.on('activate', function(event, elm){
		clusterHome.intervalId = setInterval( function(){ pieData.reload();}, 5000);
	});
	
	clusterHome.on('deactivate', function(event, elm){
		clearInterval(clusterHome.intervalId);
	});
	
	clusterHome.on('close', function(){
		clearInterval(clusterHome.intervalId);
	});
	
	record = undefined;
	
	clusterData.load({
		callback: function(data, op, success){
			var cluster = data[0].data;
			if(success){
				
				if(Ext.get('stats')){
					var statsDiv = Ext.get('stats').dom;
					statsDiv.innerHTML = '';
					runtime.append(statsDiv, cluster.runtime);
				}
				record = data;
				
				// members
				var membersDiv = Ext.get('memberlist').dom;
				
				
				membersDiv.innerHTML = '';
				membersTpl.append(membersDiv, cluster.members);
				Ext.getCmp('members').setTitle('Members (' + cluster.members.length + ')');
				
				var anchors = $.get('member');
				for(var i = 0; i < anchors.length; i++){
					var elm = new Ext.dom.Element(anchors[i]);
					elm.on('click', getMemberInfo);
				}

				buildInstance(cluster, 'maps', ['OwnedEntryCount', 'total']);
				buildInstance(cluster, 'queues', ['OwnedItemCount', 'NumberOfOffers']);
				buildInstance(cluster, 'topics', []);
				buildInstance(cluster, 'executors', []);
			//	buildInstance(cluster, 'multiMaps', []);
				
			}else{
				console.log("error loading cluster state");
			}
		}
	});
});

function reload(){ 
	clusterData.reload();
};
reload.repeat(5000);