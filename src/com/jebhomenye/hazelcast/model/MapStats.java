package com.jebhomenye.hazelcast.model;

import lombok.Data;

@Data
public class MapStats {
	private String instance;
	private Table memoryDataTable;
	private Table throughputDataTable;
	private Table memoryChart;
	private Table throughputChart;
	
	public boolean isEmpty() {
		return memoryChart.isEmtpy() && memoryDataTable.isEmtpy() 
				&& throughputChart.isEmtpy() && throughputDataTable.isEmtpy();
	}
}
