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
            <p class="text-center">
              <a href="#" type="button" disabled="disabled" class="btn btn-xs btn-primary">Active</a>
              <a href="#" type="button" disabled="disabled" class="btn btn-xs btn-warning">Discontinued</a>
            </p>
            <c:if test="${products.size() > 1}">
              <a href="${contextPath}/browse/${family}/all.html" class="btn btn-primary btn-sm btn-block">All</a>
            </c:if>
            <c:forEach items="${products}" var="product">
              <c:choose>
                <c:when test="${discontinued.get(product) == true}">
                  <a href="${contextPath}/browse/${family}/${product}.html" class="btn btn-warning btn-lg btn-block">${product}</a>
                </c:when>
                <c:otherwise>
                  <a href="${contextPath}/browse/${family}/${product}.html" class="btn btn-primary btn-lg btn-block">${product}</a>
                </c:otherwise>
              </c:choose>
            </c:forEach>
          </div>
        </div>
      </div>

    </div><!-- /.container -->
<%@ include file="_footer.jsp" %>
