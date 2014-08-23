<%@ include file="_header.jsp" %>
    <div class="container">
      <ol class="breadcrumb">
        <li><a href="${contextPath}/browse"><span class="glyphicon glyphicon-home"></span></a></li>
        <li><a href="${contextPath}/browse/${family}.html">${family}</a></li>
        <li class="active">${product}</li>
      </ol>

      <div class="main-page">
        <div class="row">
          <div class="col-md-8 col-md-offset-2">
            <p class="text-center lead">Select the version</p>
            <% for(int i = 0; i < list.length; i+=1) { %>
              <a href="<%=prodUrl%>/<%=list[i]%>.html" class="btn btn-primary btn-lg btn-block"><%=list[i]%></a>
            <% } %>
          </div>
        </div>
      </div>

    </div><!-- /.container -->
<%@ include file="_footer.jsp" %>