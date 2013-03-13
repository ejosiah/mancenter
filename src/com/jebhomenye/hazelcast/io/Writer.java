package com.jebhomenye.hazelcast.io;

import java.io.OutputStream;

public interface Writer {
	
	void write(Object value, OutputStream out);

}
