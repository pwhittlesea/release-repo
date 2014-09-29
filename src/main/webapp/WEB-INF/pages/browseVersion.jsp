<%@ include file="_header.jsp" %>
    <div class="container">
      <ol class="breadcrumb">
        <li><a href="${contextPath}/browse"><span class="glyphicon glyphicon-home"></span></a></li>
        <li><a href="${contextPath}/browse/${family}.html">${family}</a></li>
        <li><a href="${contextPath}/browse/${family}/${product}.html">${product}</a></li>
        <li class="active">${version}</li>
      </ol>

      <div class="main-page">
        <div class="row">
          <div class="col-md-12">
            <div class="panel panel-default">
              <div class="panel-heading">
                ${product} ${version}
                <c:if test="${resources.size() > 1}">
                  <a href="${contextPath}/download/${family}/${product}/${version}.zip" class="btn btn-primary btn-xs pull-right">Download All</a>
                </c:if>
              </div>
              <div class="panel-body">
                <p>
                  <c:if test="${jiraShortList.size() < 1}">
                    <span class="muted">No Changelog avaliable</span>
                  </c:if>
                  <c:forEach items="${jiraShortList}" var="entry">
                    <a href="${jiraBaseUrl}/browse/${entry.key}">${entry.key}</a>: ${entry.value}<br>
                  </c:forEach>
                </p>
                <c:if test="${jiraLongList.size() > 0}">
                  <p class="text-center">
                    <button type="button" class="btn btn-link btn-xs" data-toggle="collapse" data-target="#more-items">Show More</button>
                  </p>
                  <div id="more-items" class="collapse">
                    <p>
                      <c:forEach items="${jiraLongList}" var="entry">
                        <a href="${jiraBaseUrl}/browse/${entry.key}">${entry.key}</a>: ${entry.value}<br>
                      </c:forEach>
                    </p>
                  </div>
                </c:if>
              </div>
              <div class="table-responsive">
                <table id="bs-table" class="table table-hover">
                  <thead>
                    <tr>
                      <th class="col-lg-8 text-left">Name</th>
                      <th class="col-lg-2 text-right">Size</th>
                      <th class="col-lg-2 text-right">Modified</th>
                    </tr>
                  </thead>
                  <tbody>
                    <c:forEach items="${resources}" var="resource">
                      <tr>
                        <td>
                          <span class="glyphicon glyphicon-file"></span>
                          <a href="${contextPath}/download/${family}/${product}/${version}/${resource[0]}">${resource[0]}</a>
                        </td>
                        <td class="text-right">${resource[1]}</td>
                        <td class="text-right">${resource[2]}</td>
                      </tr>
                    </c:forEach>
                  </tbody>
                </table>
                <div class="panel-footer"><small>${resources.size()} files, ${totalSize} in total</small></div>
              </div>
            </div>
          </div>

        </div>
      </div>

    </div><!-- /.container -->
<%@ include file="_footer.jsp" %>
