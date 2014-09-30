<%@ include file="_header.jsp" %>
    <div class="container">
      <ol class="breadcrumb">
        <li class="active"><span class="glyphicon glyphicon-home"></span></li>
      </ol>

      <div class="main-page">
        <div class="row">
          <div class="col-md-8 col-md-offset-2">
            <p class="text-center lead">Select the product family</p>
              <div class="row">
                <div class="col-md-6">
                  <c:forEach items="${leftList}" var="family">
                    <a href="${contextPath}/browse/${family}.html" class="btn btn-primary btn-lg btn-block">${family}</a>
                  </c:forEach>
                </div>
                <div class="col-md-6">
                  <c:forEach items="${rightList}" var="family">
                    <a href="${contextPath}/browse/${family}.html" class="btn btn-primary btn-lg btn-block">${family}</a>
                  </c:forEach>
                </div>
              </div>
          </div>
        </div>
      </div>

    </div><!-- /.container -->
<%@ include file="_footer.jsp" %>
