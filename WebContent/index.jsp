<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="https://cdn.bootcss.com/jquery/3.2.1/jquery.min.js"></script>
<title>Insert title here</title>
</head>
<body>
	<button onClick="test()">提交</button>
	<script>
		function test(){
			$.ajax(
				{ url: "/HelloWorld/Servlet", 
				context: document.body, 
				success: function(data){
					console.log(data);
		        	//$(this).addClass("done");
		      	}
			});
		}
	</script>
</body>
</html>