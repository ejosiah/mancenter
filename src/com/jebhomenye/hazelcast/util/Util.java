package com.jebhomenye.hazelcast.util;

public final class Util {
	public static final Long KB = 1024L;
	public static final Long MB = KB * KB;
	public static final Long GB = MB * KB;
	
	private Util() {}
	
	public static String toString(Long freeMemory){
		if(freeMemory / GB == 0l){
			return freeMemory.doubleValue()/MB + " MB";
		}else{
			return freeMemory.doubleValue()/GB + " GB";
		}
	}
}
