package com.jebhomenye.hazelcast.service.command.http;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hazelcast.monitor.TimedMemberState;
import com.jebhomenye.hazelcast.model.Member;
import com.jebhomenye.hazelcast.model.Meta;
import com.jebhomenye.hazelcast.model.assemby.MemberAssember;
import com.jebhomenye.hazelcast.service.JobExecutor;
import com.jebhomenye.hazelcast.service.StateManager;
import com.jebhomenye.hazelcast.service.command.Command;
import com.jebhomenye.hazelcast.service.command.mCenter.GetMemberConfigCommand;
import com.jebhomenye.hazelcast.service.command.mCenter.GetMemberSysPropsCommand;

public class MemberCommand implements Command<Map<String, Object>> {

	@Override
	public Map<String, Object> execute(Object...args) {
		HttpServletRequest request = (HttpServletRequest) args[0];
		String key = request.getParameter("key");
		TimedMemberState memberState = StateManager.getInstance().getTimedMember(key);
		
		if(memberState == null){
			Meta meta = new Meta(false, "Could not find member: " + key);
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("meta", meta);
			return result;
		}
		
		Map<String, String> systemProps = new GetMemberSysPropsCommand(JobExecutor.getInstance()).execute(memberState.getClusterName(), key);
		String memberConfig = new GetMemberConfigCommand(JobExecutor.getInstance()).execute(memberState.getClusterName(), key);
		Member member = MemberAssember.assemble(memberState, systemProps, memberConfig);
		Meta meta = new Meta(true, "member " + key + " is active");
		
		Map<String, Object> result = new LinkedHashMap<String, Object>();
		result.put("member", member);
		result.put("meta", meta);
		
		return result;
	}

}
