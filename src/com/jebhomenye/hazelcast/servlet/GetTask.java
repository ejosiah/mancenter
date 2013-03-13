package com.jebhomenye.hazelcast.servlet;

import java.io.DataOutputStream;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jebhomenye.hazelcast.service.Job;
import com.jebhomenye.hazelcast.service.JobManager;

/**
 * Servlet implementation class GetTask
 */
public class GetTask extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetTask() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JobManager manager = JobManager.getInstance();
		Job job = manager.getRequest(request.getParameter("cluster"), request.getParameter("member"));
		DataOutputStream out = new DataOutputStream(response.getOutputStream());
		
		if(job != null && job.getRequest() != null){
			out.writeInt(job.getJobId().intValue());
			out.writeInt(job.getRequest().getType());
			job.getRequest().writeData(out);
		}else{
			out.writeInt(0);
		}
	}

}
