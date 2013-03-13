package com.jebhomenye.hazelcast.model;

import lombok.Data;

@Data
public class MapEntry {
	private final boolean valid;
	private final String clazz;
	private final Long hits;
	private final String value;
	private final Long version;
	private final String accessTime;
	private final String creationTime;
	private final String expirationTime;
	private final String updateTime;
	private final String cost;
}
