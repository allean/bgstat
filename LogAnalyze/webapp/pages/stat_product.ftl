<html xmlns="http://www.w3.org/1999/xhtml">
<head>
</head>
<body>  
<h1>统计-${product_id}</h1>





<#list   platform as product_platform>	
<#assign list_product_dailystat=stats[product_platform]>

<h3>${product_platform}</h3>
<table border="1">
  <tr>
    <th>时间</th>
    <th>当日新增数</th>
    <th>当日活跃数</th>
    <th>当日ip数</th>
    <th>当日pv数</th>
    <th>次日活跃率</th>
    <th>次日留存率</th>
    <th>当日转发数</th>
    <th>当日评论数</th>
    <th>当日平均阅读数</th>
<#if simple="n">    
    <th>1周新增数</th>
    <th>1周活跃数</th>
    <th>次周活跃率</th>
    <th>次周留存率</th>
        
    <th>1月新增数</th>
    <th>1月活跃数</th>
    <th>1月ip</th>
    <th>1月pv</th>
	<th>次月活跃率</th>
    <th>次月留存率</th>
    <th>1月转发数</th>
    <th>1月评论数</th>
    
    <th>总用户数</th>
    <th>总ip</th>
    <th>总转发数</th>
    <th>总转评论数</th>
</#if>
    
    
  </tr>
  
  <#list list_product_dailystat as dailystat>	
  <tr>
  	<th>${dailystat.create_date}</th>
  	<th>${dailystat.today_new_user_count}</th>
  	<th><a href="/stat_product_version?product_id=${product_id}&product_platform=${product_platform}&date=${dailystat.create_date}">${dailystat.today_active_user_count}</a></th>
  	<th>${dailystat.today_ip}</th>
  	<th>${dailystat.today_pv}</th>
  	<th>${dailystat.day_after_use_rate * 100}%</th>
  	<th>${dailystat.day_after_new_user_lose_rate * 100}%</th>
  	<th>${dailystat.today_forward_count}</th>
  	<th>${dailystat.today_comment_count}</th>
  	<th><a href="/stat_product_channel?product_id=${product_id}&product_platform=${product_platform}&date=${dailystat.create_date}">${dailystat.today_active_user_read_rate}</a></th>
<#if simple="n">  	
  	<th>${dailystat.week_new_user_count}</th>
  	<th>${dailystat.week_active_user_count}</th>
  	<th>${dailystat.week_after_use_rate * 100}%</th>
  	<th>${dailystat.week_after_new_user_lose_rate * 100}%</th>

	<th>${dailystat.month_new_user_count}</th>
  	<th>${dailystat.month_active_user_count}</th>
  	<th>${dailystat.month_ip}</th>
  	<th>${dailystat.month_pv}</th>
  	<th>${dailystat.month_after_use_rate * 100}%</th>
  	<th>${dailystat.month_after_new_user_lose_rate * 100}%</th>	
  	<th>${dailystat.month_forward_count}</th>	
  	<th>${dailystat.month_comment_count}</th>
  	
  	<th>${dailystat.total_user_count}</th>
  	<th>${dailystat.total_ip}</th>
  	<th>${dailystat.total_forward_count}</th>
  	<th>${dailystat.total_comment_count}</th>
 </#if> 	
  </tr>
  </#list>
  
</table>

</#list>




</body>
</html>