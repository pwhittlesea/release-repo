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
            <c:if test="${resources.size() > 1}">
              <p class="text-right">
                <a href="${contextPath}/download/${family}/${product}/${version}.zip" class="btn btn-primary btn-xs">Download All</a>
              </p>
            </c:if>
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
                    </td>
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
        <div class="jira hide">
          <div class="row">
            <div class="col-md-8 col-md-offset-2">
              <div class="form-inline">
                <div class="checkbox">
                  <label>Change Log</label>
                </div>
                <div class="pull-right">
                  <button type="button" id="loading-btn" data-loading-text="Loading..." class="btn btn-primary btn-xs">Load</button>
                </div>
              </div>
            </div>
          </div>
          <div style="height:10px;"></div>
          <div class="row">
            <div class="col-md-8 col-md-offset-2">
              <div class="change-log well"></div>
            </div>
          </div>
        </div>
      </div>

    </div><!-- /.container -->
    <script type="text/javascript">
      $(document).ready(function() {
        $.ajax("${contextPath}/jira")
          .always(function (responseText) {
            if (responseText === 'true') {
              $('.jira').removeClass('hide');
            }
          });
        $('#loading-btn').click(function () {
          var btn = $(this)
          btn.button('loading')
          $.ajax("${contextPath}/jira/${family}/${product}/${version}.html")
            .done(function(responseText) {
              $('.change-log').html(responseText.replace(/\n/g, "<br>"))
            })
            .always(function () {
              btn.button('reset')
            });
        });
      });
    </script>
<%@ include file="_footer.jsp" %>
