<%@ include file="_header.jsp" %>
    <div class="container">
      <ol class="breadcrumb">
        <li><a href="${contextPath}/browse"><span class="glyphicon glyphicon-home"></span></a></li>
        <li><a href="${contextPath}/browse/${family}.html">${family}</a></li>
        <li><a href="${contextPath}/browse/${family}/${application}.html">${application}</a></li>
        <li class="active">${version}</li>
      </ol>

      <div class="main-page">
        <div class="row">
          <div class="col-md-12">
            <div class="panel panel-default">
              <div class="panel-heading">
                ${application} ${version}
                <div class="btn-group pull-right">
                  <c:if test="${resources.size() > 1}">
                    <a href="${contextPath}/download/${family}/${application}/${version}.zip" class="btn btn-primary btn-xs">Download All</a>
                  </c:if>
                  <!-- Button trigger modal -->
                  <button class="btn btn-default btn-xs" data-toggle="modal" data-target="#jqlModal">
                    <span class="glyphicon glyphicon-cog"></span>
                  </button>
                </div>

                <!-- Modal -->
                <div class="modal fade" id="jqlModal" tabindex="-1" role="dialog" aria-labelledby="jqlModalLabel" aria-hidden="true">
                  <div class="modal-dialog">
                    <div class="modal-content">
                      <div class="modal-header">
                        <h4 class="modal-title" id="jqlModalLabel">Modal title</h4>
                      </div>
                      <form role="form" id="jql">
                        <div class="modal-body">
                          <div class="form-group">
                            <div class="input-group">
                              <div class="input-group-addon">${parentJQL}</div>
                              <input type="text" class="form-control" id="changelog-text" placeholder="JQL Script" value="${childJQL}"/>
                            </div>
                          </div>
                        </div>
                        <div class="modal-footer">
                          <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                          <button type="submit" class="btn btn-primary">Save changes</button>
                        </div>
                      </form>
                    </div>
                  </div>
                </div> <!-- /.modal -->

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
                    <button id="more-button" type="button" class="btn btn-link btn-xs" data-toggle="collapse" data-target="#more-items">Show More</button>
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
                      <th class="col-lg-7 text-left">Name</th>
                      <th class="col-lg-2 text-right">MD5</th>
                      <th class="col-lg-1 text-right">Size</th>
                      <th class="col-lg-2 text-right">Modified</th>
                    </tr>
                  </thead>
                  <tbody>
                    <c:forEach items="${resources}" var="resource">
                      <tr>
                        <td>
                          <span class="glyphicon glyphicon-file"></span>
                          <a href="${contextPath}/download/${family}/${application}/${version}/${resource[0]}">${resource[0]}</a>
                        </td>
                        <td class="text-right">${resource[3]}</td>
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
    <script type="text/javascript">
      $("#more-items").on('show.bs.collapse', function () {
        $("#more-button").text('Show Less');
      }).on('hide.bs.collapse', function () {
        $("#more-button").text('Show More');
      });
      $( "#jql" ).submit(function( event ) {
        event.preventDefault();
        $.ajax({
          type: "PUT",
          url: "${contextPath}/upload/${family}/${application}/${version}/.jira",
          data: $("#changelog-text").val()
        })
        .fail(function( xhr ) {
          $("#jql").addClass('has-error');
          alert( "Request failed: " + xhr.status + " -> " + xhr.statusText );
          console.log(xhr);
          return false;
        })
        .done(function( msg ) {
          $("#jqlModal").modal("close");
          return true;
        });
      });
    </script>
<%@ include file="_footer.jsp" %>
