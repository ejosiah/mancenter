package com.jebhomenye.hazelcast.model.assemby;

import static com.jebhomenye.hazelcast.util.TimeUtil.upTimeToStrig;

import java.util.Date;
import java.util.Map;

import com.jebhomenye.hazelcast.model.Runtime;
import com.jebhomenye.hazelcast.util.Util;

public class RuntimeAssember {
	
	private RuntimeAssember() {}
	
	public static Runtime assemble(Map<String, Long> runtimeProps){
		return assemble(runtimeProps, false);
	}
	
	public static Runtime assembleAll(Map<String, Long> runtimeProps){
		return assemble(runtimeProps, true);
	}
	
	private static Runtime assemble(Map<String, Long> runtimeProps, boolean all){
		Runtime runtime = new Runtime();
		runtime.setStartTime(new Date(runtimeProps.get("date.startTime")).toString());
		runtime.setUpTime(upTimeToStrig(runtimeProps.get("seconds.upTime").doubleValue()));
		
		runtime.setFreeMemory(Util.toString(runtimeProps.get("memory.freeMemory")));
		runtime.setMaxMemory(Util.toString(runtimeProps.get("memory.maxMemory")));
		runtime.setTotalMemory(Util.toString(runtimeProps.get("memory.totalMemory")));
		
		if(all){
			runtime.setHeapMemoryUsed(Util.toString(runtimeProps.get("memory.heapMemoryUsed")));
			runtime.setHeapMemoryMax(Util.toString(runtimeProps.get("memory.heapMemoryMax")));
			runtime.setNoneHeapMemoryUsed(Util.toString(runtimeProps.get("memory.nonHeapMemoryUsed")));
			runtime.setNoneHeapMemoryMax(Util.toString(runtimeProps.get("memory.nonHeapMemoryMax")));
			
			runtime.setThreadCount(runtimeProps.get("runtime.threadCount"));
			runtime.setPeakThreadCount(runtimeProps.get("runtime.peakThreadCount"));
			runtime.setDaemonThreadCount(runtimeProps.get("runtime.daemonThreadCount"));
			runtime.setTotalStartedThreadCount(runtimeProps.get("runtime.totalStartedThreadCount"));
			runtime.setAvailableProcessors(runtimeProps.get("runtime.availableProcessors"));
			
			runtime.setLoadedClassCount(runtimeProps.get("runtime.loadedClassCount"));
			runtime.setTotalLoadedClassCount(runtimeProps.get("runtime.totalLoadedClassCount"));
			runtime.setTotalUnloadedClassCount(runtimeProps.get("runtime.unloadedClassCount"));
			
		}
		
		return runtime;
	}
}
