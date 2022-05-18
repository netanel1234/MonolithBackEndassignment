<%@ page language="java" contentType="text/html; charset=windows-1255"
    pageEncoding="windows-1255" import="com.assignment.*,java.util.*"%>
<!DOCTYPE html>
<html>
<head>
<style>
table, th, td {
  border:1px solid black;
}
</style>
<meta charset="windows-1255">
<title>Insert title here</title>
</head>
<body>
<div id="products"></div>
<form method="get" action="/BackEndAssignment/Controller">
	<input type="submit" value="Call API">
</form>

<%
	if(session.getAttribute("empty") == null) {
		session.setAttribute("empty", 1);
		session.setAttribute("products", null);
		session.setAttribute("categories", null);
	}
		
	if(session.getAttribute("products") != null) {%>
	
		<h1>List of products</h1> 
		<h3>To categories list: <a href="#categories">Categories List</a></h3> 
		<table style="width:100%">
			<tr>
				<th>ID</th>
				<th>Title</th>
				<th>Price</th>
				<th>Attributes</th>
			</tr><%
			Product[] products = (Product[]) session.getAttribute("products");
			for(Product product : products) {%>
				<tr>
					<th><%= product.getId() %></th>
					<th><%= product.getTitle() %></th>
					<th><%= product.getPrice() %></th>
					<th><% 
					for(Map.Entry<String, ArrayList<String>> entry : product.getLabels().entrySet()) {
						%><%=entry.getKey() + ": " %><% 
						for(int i = 0; i < entry.getValue().size(); i++) {
							%><small><%=entry.getValue().get(i) %></small><% 
							if(i != entry.getValue().size() - 1)
								%><%=", "%><%									
							else
								%><%= "."%><%
						}%><br><%
					}%>
					</th>
				</tr><%
			}%>
		</table><%
	}
	
	if(session.getAttribute("categories") != null) {
		%><div id="categories"></div>
		<h1>List of categories</h1> 
		<h3>To products list: <a href="#products">Product List</a></h3>
		<table style="width:100%">
			<tr>
				<th>Category</th>
				<th>Number of related products</th>
				<th>Related attributes</th>
			</tr>		
			<%
			Category[] categories = (Category[]) session.getAttribute("categories");
			for(Category category : categories) {
				%>
				<tr>
					<th><%=category.getTitle() %></th>
					<th><%=category.getCountRelatedProducts() %></th>
					<th>
					<% 
					for(Map.Entry<String, HashMap<String, Integer>> attributeEntry : category.getRelatedAttributes().entrySet())
					{
						String attribute = attributeEntry.getKey();
						%><%= attribute + ": " %><small><%
						for(Map.Entry<String, Integer> labelEntry : attributeEntry.getValue().entrySet())
						{
							%><%=labelEntry.getKey() + " - " + labelEntry.getValue() + ", " %><%
						}
						%><br></small><%
					}
					%>
					</th>
				</tr><%
			}
			%>
		</table>
		<%
	}
%>
</body>
</html>