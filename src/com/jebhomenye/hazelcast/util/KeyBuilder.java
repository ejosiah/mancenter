package com.jebhomenye.hazelcast.util;

import com.hazelcast.monitor.TimedMemberState;
import com.hazelcast.nio.Address;

public final class KeyBuilder {
	
	private KeyBuilder() {}
	
	public static String buildMemberKey(TimedMemberState memberState){
		return new StringBuilder()
			.append(memberState.getMemberState().getAddress().getHost())
			.append(":").append(memberState.getMemberState().getAddress().getPort())
			.toString();
	}
	
	public static String buildClusterKey(TimedMemberState state, Long time){
		StringBuilder key = new StringBuilder(state.getClusterName());
		key.append("_").append(time);
		return key.toString();
	}
	
	public static String buildClusterKey(String cluster, Long time){
		StringBuilder key = new StringBuilder(cluster);
		key.append("_").append(time);
		return key.toString();
	}
	
	public static String buildConnectKey(TimedMemberState memberState){
		String addressKey = buildAddressKey(memberState);
		StringBuilder key = new StringBuilder(memberState.getClusterName());
		key.append("_").append(addressKey);
		return key.toString();
	}

	public static String buildAddressKey(TimedMemberState memberState){
		StringBuilder result = new StringBuilder();
		Address address = memberState.getMemberState().getAddress();
		result.append(address.getHost()).append(":").append(address.getPort());
		return result.toString();
	}
	
}
