package com.jebhomenye.hazelcast.model;


import java.util.List;
import java.util.Map;
import java.util.Set;

import lombok.Data;

@Data
public class Cluster {
	private List<String> members;
	private List<String> executors;
	private Set<String> maps;
	private Set<String> multiMaps;
	private Set<String> queues;
	private Set<String> topics;
	private Runtime runtime;
	private Meta meta;
}
