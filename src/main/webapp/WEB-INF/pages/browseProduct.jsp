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
            <p class="text-center lead">Select a version</p>
            <p class="text-center">
              <a href="#" type="button" class="ver-select btn btn-xs btn-primary">Stable</a>
              <a href="#" type="button" class="ver-select btn btn-xs btn-info">Candidate</a>
              <a href="#" type="button" class="ver-select btn btn-xs btn-warning">Beta</a>
              <a href="#" type="button" class="ver-select btn btn-xs btn-danger">Alpha</a>
            </p>
            <c:forEach items="${versions}" var="version">
              <c:set var="verPath" value="${contextPath}/browse/${family}/${product}/${version}.html"/>
              <c:choose>
                <c:when test="${status.get(version) eq 'candidate'}">
                  <a href="${verPath}" class="ver ver-candidate btn btn-info btn-lg btn-block">${version}</a>
                </c:when>
                <c:when test="${status.get(version) eq 'beta'}">
                  <a href="${verPath}" class="ver ver-beta btn btn-warning btn-lg btn-block">${version}</a>
                </c:when>
                <c:when test="${status.get(version) eq 'alpha'}">
                  <a href="${verPath}" class="ver ver-alpha btn btn-danger btn-lg btn-block">${version}</a>
                </c:when>
                <c:otherwise>
                  <a href="${verPath}" class="ver ver-stable btn btn-primary btn-lg btn-block">${version}</a>
                </c:otherwise>
              </c:choose>
            </c:forEach>
          </div>
        </div>
      </div>

    </div><!-- /.container -->
    <script type="text/javascript">
      $(document).ready(function() {
        $( ".ver-select" ).click(function(event) {
          $( ".ver-select" ).removeClass('active');
          $(this).addClass('active');

          var verName = $(this).text().toLowerCase();
          $(".ver").hide();
          $(".ver-" + verName).show();
        });
      });
    </script>
<%@ include file="_footer.jsp" %>