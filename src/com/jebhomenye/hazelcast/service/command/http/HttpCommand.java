package com.jebhomenye.hazelcast.service.command.http;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jebhomenye.hazelcast.service.command.Command;

public abstract class HttpCommand<T> implements Command<T> {

	@Override
	public T execute(Object... args) {
		return execute((HttpServletRequest)args[0], (HttpServletResponse)args[1]);
	}
	
	public abstract T execute(HttpServletRequest request, HttpServletResponse response);

}
