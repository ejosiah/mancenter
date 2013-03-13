package com.jebhomenye.hazelcast.service.command;

public interface Command<T> {
	
	T execute(Object...args);
}
