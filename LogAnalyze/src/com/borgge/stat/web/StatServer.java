package com.borgge.stat.web;

import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHolder;
import org.mortbay.jetty.webapp.WebAppContext;

import freemarker.ext.servlet.FreemarkerServlet;

public class StatServer {

	public static void main(String args[]) throws Exception {
		Server server = new Server(8080);
//		Context root = new Context(server, "/", Context.SESSIONS);
//		ServletHolder sh = new ServletHolder(StatServlet.class);
//		root.addServlet(sh, "/hello");
		
//		ServletHolder freemark = new ServletHolder(FreemarkServlet.class);
//		root.addServlet(freemark, "*.ftl");
		
		ServletHolder freemarker = new ServletHolder(FreemarkerServlet.class);
		
		WebAppContext context = new WebAppContext(); 
		context.setDescriptor("webapp/WEB-INF/web.xml");  
        context.setResourceBase("./webapp");  
        context.setContextPath("/");  
        context.setParentLoaderPriority(true);  
   
        context.addServlet(StatServlet.class, "/stat");
        context.addServlet(StatProductServlet.class, "/stat_product");
        context.addServlet(StatProductVertsionServlet.class, "/stat_product_version");
        context.addServlet(StatProductChannelServlet.class, "/stat_product_channel");
        context.addServlet(FreemarkerServlet.class, "*.ftl");
        
        server.addHandler(context);  
		
//        context.addServlet(freemarker, "*.ftl");
//		root.addServlet(servlet, pathSpec)
		server.start();
	}

}
