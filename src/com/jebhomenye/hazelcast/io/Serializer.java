package com.jebhomenye.hazelcast.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import lombok.SneakyThrows;

import org.codehaus.jackson.map.ObjectMapper;


public class Serializer {
	
	@SneakyThrows
	public byte[] serialzie(Object object){
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		new ObjectMapper().writeValue(new GZIPOutputStream(out), object);
		
		return out.toByteArray();
	}
	
	@SneakyThrows
	public <T> T  deserialize(byte[] toObject, Class<T> clz){
		GZIPInputStream in = new GZIPInputStream(new ByteArrayInputStream(toObject));
		ObjectMapper mapper = new ObjectMapper();
		T result = mapper.readValue(in, clz);
		return result;
	}

}
