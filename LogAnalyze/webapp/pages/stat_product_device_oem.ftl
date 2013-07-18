<html xmlns="http://www.w3.org/1999/xhtml">
<head>
</head>
<body>  
<h1>品牌分布</h1>





<h3>${product_id}-${product_platform}-${date}</h3>
<table border="1">
  <tr>
    <th>用户数</th>
    <th>品牌</th>
    <th>比例</th>
  </tr>
  
  <#list stats as dailystat>	
  <tr>
  	<th>${dailystat.num}</th>
  	<th>${dailystat.type}</th>
  	<th>${dailystat.num / total_num * 100}%</th>
  </tr>
  </#list>
  
</table>





</body>
</html>