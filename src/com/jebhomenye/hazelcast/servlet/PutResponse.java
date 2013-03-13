package com.jebhomenye.hazelcast.servlet;

import java.io.DataInputStream;
import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.SneakyThrows;


import com.hazelcast.impl.management.ConsoleRequest;
import com.jebhomenye.hazelcast.service.JobManager;

/**
 * Servlet implementation class PutResponse
 */
public class PutResponse extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	ConsoleRequest consoleRequests[];
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PutResponse() {
        super();
    }
    
    @SneakyThrows
    public void init(ServletConfig config)
    {
    	Class<? extends ConsoleRequest>[] consoleReqClz = buildClasses(config);
        final int count = consoleReqClz.length;
        consoleRequests = new ConsoleRequest[count+1];
        for(int i = 0; i < count; i++){
        	ConsoleRequest request = consoleReqClz[i].newInstance();
        	register(request);
        }
        
    }
    
    @SuppressWarnings("unchecked")
	private Class<? extends ConsoleRequest>[] buildClasses(ServletConfig config){
    	String[] requestClsNames = config.getInitParameter("requests").split("\n");
    	Class<? extends ConsoleRequest>[] classes = new Class[requestClsNames.length];
    	try {
			for(int i = 0; i < classes.length; i++){
				classes[i] = (Class<? extends ConsoleRequest>)
						Class.forName(requestClsNames[i].trim());
			}
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("unable to construct console request objects");
		}
    	return classes;
    }

    public void register(ConsoleRequest consoleRequest)
    {
        consoleRequests[consoleRequest.getType()] = consoleRequest;
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JobManager manager = JobManager.getInstance();
		DataInputStream input = new DataInputStream(request.getInputStream());
		int jobId = input.readInt();
		int type = input.readInt();
		ConsoleRequest consoleReq = consoleRequests[type];
		Object consoleResponse = null;
		consoleResponse = consoleReq.readResponse(input);
		manager.putResponse(jobId, consoleResponse);
	}

}
