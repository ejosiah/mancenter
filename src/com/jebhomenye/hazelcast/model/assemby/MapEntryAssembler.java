package com.jebhomenye.hazelcast.model.assemby;

import java.util.Date;
import java.util.Map;

import com.jebhomenye.hazelcast.model.MapEntry;
import com.jebhomenye.hazelcast.util.TimeUtil;

public class MapEntryAssembler {
	
	public static MapEntry assemble(Map<String, String> entry){
		MapEntry mapEntry = new MapEntry(new Boolean(entry.get("boolean_valid"))
				, entry.get("browse_class"), new Long(entry.get("browse_hits"))
				, entry.get("browse_value"), new Long(entry.get("browse_version")), 
				new Date(new Long(entry.get("date_access_time"))).toString(), 
				new Date(new Long(entry.get("date_creation_time"))).toString(), 
				new Date(new Long(entry.get("date_expiration_time"))).toString(), 
				new Date(new Long(entry.get("date_update_time"))).toString()
				, entry.get("memory_cost"));
		
		return mapEntry;
	}

}
