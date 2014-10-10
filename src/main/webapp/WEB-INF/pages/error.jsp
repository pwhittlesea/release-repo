<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
Exception:  ${exception.message}
<c:forEach items="${exception.stackTrace}" var="ste">${ste}
</c:forEach>