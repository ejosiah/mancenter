package com.jebhomenye.hazelcast.util;

import static java.lang.String.*;

import java.lang.reflect.Method;

public final class MethodUtils {
	private MethodUtils() {}
	
	public static Object invoke(String method, Object onThis, Object...args){
		Object result = null;
		try{
			Method methodToInvoke = onThis.getClass().getDeclaredMethod(method, getParameterTypes(args));
			result = methodToInvoke.invoke(onThis, args);
		}catch(Exception e){
			throw new RuntimeException(format("invocation of  method %s on %s failed", method, onThis.getClass()), e);
		}
		return result;
	}
	
	private static Class[] getParameterTypes(Object...args){
		Class[] types = new Class[args.length];
		for(int i = 0; i < args.length; i++){
			types[i] = args[i].getClass();
		}
		return types.length > 0 ? types : null;
	}
}
