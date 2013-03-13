package com.jebhomenye.hazelcast.util;

public final class TimeUtil {
	
	public static final long SECONDS = 1000L;
	public static final Long MINUTE = 60l * SECONDS;
	public static final Long HOUR = MINUTE * 60l;
	public static final Long DAY = HOUR * 24L;
	
	private TimeUtil() {}
	
	public static Long floor(Long time){
		return time - (time % 5000l);
	}
	
	public static String upTimeToStrig(Double upTime){
		Double days = upTime / DAY;
		Double hours = (upTime % DAY) / HOUR;
		Double minutes = (upTime % DAY)  % HOUR / MINUTE;
		Double seconds = (upTime % DAY) % HOUR % MINUTE / SECONDS;
		
		return String.format("%s days, %s hours, %s minutes, %s seconds ",days.longValue(), 
				hours.longValue(), minutes.longValue(), seconds.longValue());
	}
}
