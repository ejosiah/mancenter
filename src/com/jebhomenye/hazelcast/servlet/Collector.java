package com.jebhomenye.hazelcast.servlet;

import java.io.DataInputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hazelcast.monitor.TimedMemberState;
import com.hazelcast.nio.Address;
import com.jebhomenye.hazelcast.service.StateManager;

/**
 * Servlet implementation class Collector
 */
public class Collector extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public Collector() {
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		DataInputStream input = new DataInputStream(request.getInputStream());
		process(input);
	}

	public synchronized void process(DataInputStream dataInput)
			throws IOException {
		TimedMemberState memberState = new TimedMemberState();

		try {
			dataInput.readUTF();
			new Address().readData(dataInput);
			dataInput.readUTF();
			memberState.readData(dataInput);
			StateManager.getInstance().addMemberState(memberState);
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
		return;
	}
}
