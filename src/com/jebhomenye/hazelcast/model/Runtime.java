package com.jebhomenye.hazelcast.model;

import lombok.Data;

@Data
public class Runtime {
	private String version;
	private String startTime;
	private String upTime;
	private String maxMemory;
	private String totalMemory;
	private String freeMemory;
	private String heapMemoryUsed;
	private String heapMemoryMax;
	private String noneHeapMemoryUsed;
	private String NoneHeapMemoryMax;
	private Long availableProcessors;
	private Long totalStartedThreadCount;
	private Long daemonThreadCount;
	private Long threadCount;
	private Long peakThreadCount;
	private Long loadedClassCount;
	private Long totalLoadedClassCount;
	private Long totalUnloadedClassCount;
}
