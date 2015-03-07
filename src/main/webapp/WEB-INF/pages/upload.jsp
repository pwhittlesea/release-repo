<%@ include file="_header.jsp" %>
    <div class="container">
      <div class="main-page">
        <div class="row">
          <div class="col-md-8 col-md-offset-2">
            <p class="text-center lead">Upload a new Release</p>
              <div class="row">
                <form>
                    <div class="form-group">
                      <label for="family">Family</label>
                      <select id="family" class="form-control">
                        <option value="-1" selected="selected" >Select a Family</option>
                        <c:forEach items="${families}" var="family">
                            <option value="0">${family.getName()}</option>
                        </c:forEach>
                      </select>
                    </div>
                    <div class="form-group hidden">
                      <label for="application">Application</label>
                      <select id="application" class="form-control"></select>
                    </div>
                    <div class="form-group hidden">
                      <label for="version">Version</label>
                      <select id="version" class="form-control"></select>
                    </div>
                    <div id="newVersionNumberGroup" class="form-group hidden">
                      <label for="newVersionNumber">OR new version</label>
                      <div class="row-fluid">
                        <div style="padding-left: 0px" class="col-md-8">
                          <div class="input-group">
                            <div class="input-group-addon">v</div>
                            <input type="text" class="form-control" id="newVersionNumber" placeholder="0.0.0-SNAPSHOT">
                          </div>
                        </div>
                        <div class="col-md-4">
                          <div class="input-group">
                            <button id="newVersionButton" type="button" class="btn btn-primary">Submit</button>
                          </div>
                        </div>
                      </div>
                    </div>
                    <div id="artifactGroup" class="form-group hidden">
                      <label for="artifact">File input</label>
                      <input id="artifact" name="artifacts" type="file" multiple=true class="file-loading" data-upload-url="${contextPath}/upload" data-max-file-count="10">
                    </div>
                </form>
                </div>
          </div>
        </div>
      </div>

    </div><!-- /.container -->

    <script src="${contextPath}/js/fileinput.min.js"></script>
    <script type="text/javascript">
      function addOption(element, key, text) {
        element.append(
          $("<option></option>").attr("value",key).text(text)
        );
      }

      // Start Application

      function initApplications(family) {
        var applicationElement = $('#application');
        applicationElement.parent().removeClass("hidden");
        applicationElement.empty();
        addOption(applicationElement, -1, "Select an Application");

        var familyUrl = "${contextPath}/list/" + family + "/";
        $.getJSON( familyUrl, function( data ) {
          $.each( data, function( key, value ) {
            addOption(applicationElement ,key, value.replace(familyUrl, ''));
          });
        });
      }

      function resetApplications() {
        var applicationElement = $('#application');
        applicationElement.parent().addClass("hidden");
        applicationElement.empty();
      }

      // End Application

      // Start Version

      function initVersions(family, application) {
        var versionElement = $('#version');
        versionElement.parent().removeClass("hidden");
        versionElement.empty();
        addOption(versionElement, -1, "Select a Version");

        var applicationUrl = "${contextPath}/list/" + family + "/" + application + "/";
        $.getJSON( applicationUrl, function( data ) {
          $.each( data, function( key, value ) {
            addOption(versionElement ,key, value.replace(applicationUrl, ''));
          });
        });
      }

      function resetVersions() {
        var versionElement = $('#version');
        versionElement.parent().addClass("hidden");
        versionElement.empty();
      }

      // End Version

      // Start New Version

      function initNewVersions(family, application) {
        $("#newVersionNumberGroup").removeClass("hidden");
      }

      function resetNewVersions() {
        $('#newVersionNumberGroup').addClass("hidden");
        $('#newVersionNumber').val("");
      }

      // End New Version

      // Start Artifacts

      function initArtifacts(family, application, version) {
        $('#artifactGroup').removeClass("hidden");
        $('#artifact').fileinput('refresh', {uploadUrl: "${contextPath}/upload/" + family + "/" + application + "/" + version + "/new"});
      }

      function resetArtifacts() {
        $('#artifactGroup').addClass("hidden");
        $('#artifact').fileinput('refresh');
      }

      // End Artifacts

      $(document).ready(function() {
        $("#artifact").fileinput();

        $("#family").change(function() {
          resetVersions();
          resetNewVersions();
          resetArtifacts();
          if ($('#family :selected').val() > -1) {
            var family = $('#family :selected').text();
            initApplications(family);
          } else {
            resetApplications();
          }
        });

        $("#application").change(function() {
          resetArtifacts();
          if ($('#application :selected').val() > -1) {
            var family = $('#family :selected').text();
            var application = $('#application :selected').text();
            initVersions(family, application);
            initNewVersions(family, application);
          } else {
            resetVersions();
            resetNewVersions();
          }
        });

        $("#version").change(function() {
          if ($('#version :selected').val() > -1) {
            resetNewVersions();
            var family = $('#family :selected').text();
            var application = $('#application :selected').text();
            var version = $('#version :selected').text();
            initArtifacts(family, application, version);
          } else {
            resetArtifacts();
          }
        });

        $("#newVersionButton").click(function() {
          var family = $('#family :selected').text();
          var application = $('#application :selected').text();
          var version = $('#newVersionNumber').val();
          initVersions(family, application);
          initArtifacts(family, application, version);
        });
      });
    </script>
<%@ include file="_footer.jsp" %>
