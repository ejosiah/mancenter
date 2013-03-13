package com.jebhomenye.hazelcast.io;


import java.io.OutputStream;

import org.codehaus.jackson.map.ObjectMapper;

public class JSONWriter implements Writer {
	
	public void write(Object value, OutputStream out){
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.writeValue(out, value);
			out.flush();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
