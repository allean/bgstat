package com.borgge.stat.web;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSession;

import com.borgge.stat.dbstat.DailyStat;
import com.borgge.stat.dbstat.DbHelper;
import com.borgge.stat.dbstat.LogDataDao;
import com.borgge.stat.utils.BgLogUtils;

public class StatServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String date = request.getParameter("date");
		String simple = request.getParameter("simple");
		String length = request.getParameter("length");
		
		
//		String product_id = request.getParameter("product_id");
		
		date = date == null?BgLogUtils.getYesterday() : date;
		simple = simple == null?"y" : simple;
		length = length == null? "7" : length;
		
		int len = Integer.valueOf(length);
		String startDate = BgLogUtils.getLastSomeDay(date, len);
		
		List<String> products = new ArrayList();
		products.add("banbao");
		products.add("beauty");
		products.add("dailynews");
		
		SqlSession session = DbHelper.getInstance().getSqlSession();
        LogDataDao logDataDao = session.getMapper(LogDataDao.class);
        
        List<String> p = logDataDao.getProduct(date);
        if (null != p && p.size() > 0){
        	products = p;
        }
        
		
		Map<String, List<DailyStat>> rs = new HashMap<String, List<DailyStat>>();
		
		for (String product : products) {
			List<DailyStat> stats = logDataDao
					.getDailyStatByProductAndPlatformAndDate(product, "\\%",
							startDate, date);
			
			rs.put(product, stats);
		}
		
		
		request.setAttribute("length", length); 
		request.setAttribute("products", products); 
		request.setAttribute("simple", simple); 
		request.setAttribute("date", date); 
		request.setAttribute("stats", rs); 
		request.getRequestDispatcher("pages/stat.ftl").forward(request, response);
		
		
//		response.setContentType("text/html");
//		response.setCharacterEncoding("utf-8");
//		response.setStatus(HttpServletResponse.SC_OK);
//		response.getWriter().println("<h1>Hello</h1>");
	}

}
