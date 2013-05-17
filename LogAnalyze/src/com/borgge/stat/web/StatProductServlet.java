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
import com.borgge.stat.utils.BgLogUtils;

public class StatProductServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String date = request.getParameter("date");
		String product_id = request.getParameter("product_id");
		String simple = request.getParameter("simple");
		String length = request.getParameter("length");
		
		date = date == null?BgLogUtils.getToday() : date;
		product_id = product_id == null? "banbao" : product_id;
		simple = simple == null? "n" : simple;
		length = length == null? "7" : length;
		
		int len = Integer.valueOf(length);
		String startDate = BgLogUtils.getLastSomeDay(date, len);
		
		SqlSession session = DbHelper.getInstance().getSqlSession();
        LogDataDao logDataDao = session.getMapper(LogDataDao.class);
		
		Map<String, List<DailyStat>> rs = new HashMap<String, List<DailyStat>>();
		
		List<DailyStat> stats = logDataDao
				.getDailyStatByProductAndPlatformAndDate(product_id, "%",
						startDate, date);
			
		
//		System.out.println(stats);
		Collections.sort(stats, new Comparator<DailyStat>() {

			@Override
			public int compare(DailyStat o1, DailyStat o2) {
				if (o1.getProduct_platform().equals(o2.getProduct_platform())){
					return 0;
				}
				if (o1.getToday_active_user_count() <= o2.getToday_active_user_count()){
					return 1;
				}else{
					return -1;
				}
//				return 1;
			}

		});
//		System.out.println("===========================");
//		System.out.println(stats);
		
		List platform = new ArrayList();
		for (DailyStat dailyStat : stats) {
			if (dailyStat.getProduct_platform().equals("%")){
				continue;
			}
			if (!platform.contains(dailyStat.getProduct_platform())){
				platform.add(dailyStat.getProduct_platform());
			}
			String product_platform = dailyStat.getProduct_platform();
			
			if (!rs.keySet().contains(product_platform)){
				List<DailyStat> l = new ArrayList();
				l.add(dailyStat);
				rs.put(product_platform, l);
			}else{
				List<DailyStat> l = rs.get(product_platform);
				l.add(dailyStat);
//				rs.put(product_platform, l);
			}
			
//			System.out.println("day_after_new_user_lose_rate : " + dailyStat.getDay_after_new_user_lose_rate());
		}
			
		
		request.setAttribute("length", length); 
		request.setAttribute("simple", simple); 
		request.setAttribute("product_id", product_id); 
		request.setAttribute("platform", platform); 
		request.setAttribute("stats", rs); 
		request.getRequestDispatcher("pages/stat_product.ftl").forward(request, response);
		
		
//		response.setContentType("text/html");
//		response.setCharacterEncoding("utf-8");
//		response.setStatus(HttpServletResponse.SC_OK);
//		response.getWriter().println("<h1>Hello</h1>");
	}

}
