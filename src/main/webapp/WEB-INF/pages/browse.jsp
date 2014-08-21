<!DOCTYPE html>
<html lang="en">
  <head>
    <%
      String[] oddList = (String[]) request.getAttribute("oddList");
      String[] evenList = (String[]) request.getAttribute("evenList");
    %>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="Phillip Whittlesea">

    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css">

    <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css">

    <!-- Optional theme -->
    <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap-theme.min.css">

    <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
  </head>

  <body>

    <div class="navbar navbar-inverse navbar-fixed-top" role="navigation">
      <div class="container">
        <div class="navbar-header">
          <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="navbar-brand" href="#"><%=request.getAttribute("productName")%></a>
        </div>
        <div class="collapse navbar-collapse">
          <ul class="nav navbar-nav">
            <li><a href="<%=request.getContextPath()%>/">Home</a></li>
            <li class="active"><a href="#">Browse</a></li>
          </ul>
        </div><!--/.nav-collapse -->
      </div>
    </div>

    <div class="container">
      <ol class="breadcrumb">
        <li class="active"><span class="glyphicon glyphicon-home"></span></li>
      </ol>

      <div class="main-page">
        <div class="row">
          <div class="col-md-2">
          </div>
          <div class="col-md-8">
            <p class="text-center lead">Select the product family</p>
              <div class="row">
                <div class="col-md-6">
                  <% for(int i = 0; i < oddList.length; i+=1) { %>
                    <a href="<%=request.getContextPath()%>/browse/<%=oddList[i]%>.html" class="btn btn-primary btn-lg btn-block"><%=oddList[i]%></a>
                  <% } %>
                </div>
                <div class="col-md-6">
                  <% for(int i = 0; i < evenList.length; i+=1) { %>
                    <a href="<%=request.getContextPath()%>/browse/<%=evenList[i]%>.html" class="btn btn-primary btn-lg btn-block"><%=evenList[i]%></a>
                  <% } %>
                </div>
              </div>
          </div>
          <div class="col-md-2">
          </div>
        </div>
      </div>

    </div><!-- /.container -->


    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
    <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>
  </body>
</html>
