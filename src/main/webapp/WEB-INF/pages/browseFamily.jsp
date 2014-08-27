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
            <c:if test="${products.size() > 1}">
              <a href="${contextPath}/browse/${family}/all.html" class="btn btn-primary btn-sm btn-block">All</a>
            </c:if>
            <c:forEach items="${products}" var="product">
              <a href="${contextPath}/browse/${family}/${product}.html" class="btn btn-primary btn-lg btn-block">${product}</a>
            </c:forEach>
          </div>
        </div>
      </div>

    </div><!-- /.container -->
<%@ include file="_footer.jsp" %>
