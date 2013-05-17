<html xmlns="http://www.w3.org/1999/xhtml">
<head>
</head>
<body>  
<h1>频道分布</h1>





<h3>${product_id}-${product_platform}-${date}</h3>
<table border="1">
  <tr>
    <th>频道</th>
    <th>文章阅读数</th>
  </tr>
  
  <#list stats as dailystat>	
  <tr>
  	<th>${dailystat.channel_name}</th>
  	<th>${dailystat.article_by_channel}</th>
  	
  </tr>
  </#list>
  
</table>





</body>
</html>