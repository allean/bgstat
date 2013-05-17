package com.borgge.stat.web;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import com.borgge.stat.dbstat.ProductVersoin;
import com.borgge.stat.utils.BgLogUtils;

public class StatProductVertsionServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String date = request.getParameter("date");
		String product_id = request.getParameter("product_id");
		String product_platform = request.getParameter("product_platform");
		
		date = date == null?BgLogUtils.getToday() : date;
		product_id = product_id == null? "banbao" : product_id;
		product_platform = product_platform == null? "wp7" : product_platform;
		
		SqlSession session = DbHelper.getInstance().getSqlSession();
        LogDataDao logDataDao = session.getMapper(LogDataDao.class);
		
		
		List<ProductVersoin> stats = logDataDao
				.getDailyStatByProductAndPlatformAndVertionAndDate(product_id, product_platform,
						date);
		
//		System.out.println(stats);
		Collections.sort(stats, new Comparator<ProductVersoin>() {

			@Override
			public int compare(ProductVersoin o1, ProductVersoin o2) {
				
				if (o1.getActive_user_count() <= o2.getActive_user_count()){
					return 1;
				}else{
					return -1;
				}
//				return 1;
			}

		});
			
		
		request.setAttribute("product_id", product_id); 
		request.setAttribute("product_platform", product_platform); 
		request.setAttribute("date", date); 
		request.setAttribute("stats", stats); 
		request.getRequestDispatcher("pages/stat_product_version.ftl").forward(request, response);
		
		
//		response.setContentType("text/html");
//		response.setCharacterEncoding("utf-8");
//		response.setStatus(HttpServletResponse.SC_OK);
//		response.getWriter().println("<h1>Hello</h1>");
	}

}
