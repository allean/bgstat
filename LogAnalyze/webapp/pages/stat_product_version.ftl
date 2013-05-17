<html xmlns="http://www.w3.org/1999/xhtml">
<head>
</head>
<body>  
<h1>频道分布</h1>







<h3>${product_id}-${product_platform}-${date}</h3>
<table border="1">
  <tr>
    <th>版本</th>
    <th>活跃用户数</th>
  </tr>
  
  <#list stats as dailystat>	
  <tr>
  	<th>${dailystat.product_version}</th>
  	<th>${dailystat.active_user_count}</th>
  	
  </tr>
  </#list>
  
</table>





</body>
</html>