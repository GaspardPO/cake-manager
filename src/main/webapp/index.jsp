<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8" />
        <title>Test</title>
    </head>
    <body>
        <table BORDER="1">
            <tr>
                <TH> Title </TH>
                <TH> Description </TH>
                <TH> Picture </TH>
            </tr>
            <c:forEach var="cake" items="${cakes}" >
            <tr>
                <td><c:out value="${cake.title}"/></td>
                <td><c:out value="${cake.description}"/></td>
                <td><img  width="94" height="94"  src="<c:out value="${cake.image}"/>"/></td>
            </tr>
           </c:forEach>
        </table>

        <form method="post">
            <label for="title">Title:</label>
            <input id="title" name="title" type="text"/>
            </br>
            <label for="description">Description:</label>
            <input id="description" name="description" type="text"/>
            </br>
            <label for="image">Image URL:</label>
            <input id="image" name="image" type="text"/>
            </br>
            <input type="submit" value="Create new Cake" />
        </form>
    </body>
</html>