package com.jebhomenye.hazelcast.model.assemby;

import java.util.Map;

import com.hazelcast.monitor.TimedMemberState;
import com.jebhomenye.hazelcast.model.Member;
import com.jebhomenye.hazelcast.util.KeyBuilder;

public final class MemberAssember {
	
	private MemberAssember() {}
	
	public static Member assemble(TimedMemberState memberState, Map<String, String> systemProps, String memberConfig){
		Member member = new Member();
		member.setLabel(KeyBuilder.buildAddressKey(memberState));
		member.setRuntime(RuntimeAssember.assembleAll(memberState.getMemberState().getRuntimeProps()));
		member.setPartitions(memberState.getMemberState().getPartitions());
		member.setSystemProps(systemProps);
		member.setMemberConfig(memberConfig);
		
		return member;
	}
}
