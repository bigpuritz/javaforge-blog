<%@page import="net.javaforge.blog.dbconf.beans.AppInfoBean" %>
<%@page import="org.springframework.web.context.WebApplicationContext" %>
<%@ page
        import="org.springframework.web.context.support.WebApplicationContextUtils" %>
<%
    WebApplicationContext ctx = WebApplicationContextUtils
            .getRequiredWebApplicationContext(request.getSession()
                    .getServletContext());
    AppInfoBean bean = (AppInfoBean) ctx.getBean("appInfo");
%>
<!DOCTYPE unspecified PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>dbconf example page</title>
    <style type="text/css">
        body {
            font-family: verdana, sans-serif;
            font-size: 10px;
        }
    </style>

</head>
<body>
<h3>AppInfoBean properties loaded as values from the database:</h3>
<ul>
    <li><strong>Application name:</strong> <%=bean.getAppName()%>
    </li>
    <li><strong>Application version:</strong> <%=bean.getAppVersion()%>
    </li>
</ul>
<br/>
<a href="status/logback">click here to see logback status</a>
</body>
</html>