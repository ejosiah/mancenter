package com.jebhomenye.hazelcast.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Scanner;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;

import com.jebhomenye.hazelcast.io.JSONWriter;
import com.jebhomenye.hazelcast.io.Writer;
import com.jebhomenye.hazelcast.service.command.CommandFactory;

/**
 * Servlet implementation class Main
 */
public class Main extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public Main() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String commandKey = request.getParameter("operation");
		
		Object result = CommandFactory.get(commandKey).execute(request, response);
		new JSONWriter().write(result, response.getOutputStream());
	}

}
