<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	</head>
	<body>		
						
		<c:forEach var="order" items="${orders}">
			<table>
				<thead>
					<th>User Name</th>
					<th>User Age</th>
					<th>User Document</th>
					<th>User Gender</th>
					<th>User Available AccountCredit</th>					
				</thead>
				<tbody>	
					<td><c:out value="${order.user.name}"></c:out></td>
					<td><c:out value="${order.user.age}"></c:out></td>
					<td><c:out value="${order.user.documentId}"></c:out></td>
					<td><c:out value="${order.user.gender}"></c:out></td>
					<td><c:out value="${order.user.availableAccountCredit}"></c:out></td>					
				</tbody>
			</table>			
			<table>
				<thead>
					<th>Order Number</th>
				</thead>
				<tbody>					
						<td><c:out value="${order.orderNumber}"></c:out></td>		
						
						<c:forEach var="product" items="${order.product}">
							<table>
								<thead>
									<th>Product Code</th>
									<th>Description</th>
									<th>Price</th>
									<th>Category</th>
								</thead>
								<tbody>	
									<td><c:out value="${product.productCode}"></c:out></td>
									<td><c:out value="${product.description}"></c:out></td>
									<td><c:out value="${product.price}"></c:out></td>
									<td><c:out value="${product.category}"></c:out></td>
								</tbody>
							</table>	
						</c:forEach>							
				</tbody>
			</table>
			<br/>
		</c:forEach>		
	</body>
</html>
