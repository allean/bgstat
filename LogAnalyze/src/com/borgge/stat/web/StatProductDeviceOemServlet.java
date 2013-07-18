package com.borgge.stat.web;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSession;

import com.borgge.stat.dbstat.DbHelper;
import com.borgge.stat.dbstat.LogDataDao;
import com.borgge.stat.dbstat.NumTypePair;
import com.borgge.stat.dbstat.ProductVersoin;
import com.borgge.stat.utils.BgLogUtils;

public class StatProductDeviceOemServlet extends HttpServlet {

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
		
		
		List<NumTypePair> stats = logDataDao
				.device_oem_by_count(product_id, product_platform, date, date);
		
		int total_num = 0;
		for (NumTypePair numTypePair : stats) {
			total_num += numTypePair.getNum();
		}
		
		
		request.setAttribute("product_id", product_id); 
		request.setAttribute("product_platform", product_platform); 
		request.setAttribute("date", date); 
		request.setAttribute("stats", stats); 
		request.setAttribute("total_num", total_num); 
		request.getRequestDispatcher("pages/stat_product_device_oem.ftl").forward(request, response);
	}

}
