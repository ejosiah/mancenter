package com.jebhomenye.hazelcast.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.management.RuntimeErrorException;

import org.codehaus.jackson.map.ObjectMapper;

import lombok.Data;
@Data
public class Table {
	private final List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
	
	public void addRow(Map<String, Object> row){
		rows.add(row);
	}
	
	public void addRow(String data){
		String[] cells = data.split(",");
		Map<String, Object> row = new LinkedHashMap<String, Object>();
		try {
			for(String cell : cells){
				String[] entry = cell.split(":");
				if(entry.length == 2){
					row.put(entry[0].trim(), entry[1].trim());
				}else{
					row.put(entry[0].trim(), concat(1, entry).trim());
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("Malformed data: " + data);
		}
		rows.add(row);
	}
	
	private String concat(int start, String[] array){
		StringBuilder result = new StringBuilder();
		for(int i = start; i < array.length; i++){
			result.append(array[i]).append(":");
		}
		return result.replace(result.length() - 1, result.length(), "").toString();
	}
	
	public boolean isEmtpy(){
		return rows.isEmpty();
	}
}
