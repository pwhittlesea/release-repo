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
          <div class="col-md-8 col-md-offset-2">
            <div class="table-responsive">
                <table id="bs-table" class="table table-hover">
                  <thead>
                    <tr>
                      <th class="col-lg-8 text-left">Name</th>
                      <th class="col-lg-2 text-right">Size</th>
                      <th class="col-lg-2 text-right">Modified</th>
                    </tr>
                  </thead>
                  <tfoot>
                    <tr>
                      <td colspan="3">
                        <small class="pull-left text-muted">${resources.size()} files, ${totalSize} in total</small>
                    </tr>
                  </tfoot>
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
              </div>
            </div>
          </div>
        </div>
      </div>

    </div><!-- /.container -->
<%@ include file="_footer.jsp" %>
