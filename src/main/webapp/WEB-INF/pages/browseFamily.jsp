<%@ include file="_header.jsp" %>
    <div class="container">
      <ol class="breadcrumb">
        <li><a href="${contextPath}/browse"><span class="glyphicon glyphicon-home"></span></a></li>
        <li class="active">${family}</li>
      </ol>

      <div class="main-page">
        <div class="row">
          <div class="col-md-8 col-md-offset-2">
            <p class="text-center lead">Select the product</p>
            <c:forEach items="${list}" var="product">
                <a href="${contextPath}/browse/${family}/${product.name}.html"
                	class="btn btn-${(product.discontinued) ? "default" : "primary"} btn-lg btn-block">${product.name}</a>
            </c:forEach>
          </div>
        </div>
      </div>

    </div><!-- /.container -->
<%@ include file="_footer.jsp" %>
