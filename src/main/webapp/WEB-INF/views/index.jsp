<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css"
          integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">

    <title>Accident</title>
</head>
<body>
<div class="container">

    <ul class="nav justify-content-end">
        <li class="nav-item "><a class="nav-link" href="<c:url value='/'/>">Главная</a></li>
        <li class="nav-item"><a class="nav-link" href="<c:url value='/create'/>"> Добавить инцидент</a></li>
    </ul>

    <hr class="my-4">

    <table class="table table-hover mb-0" id="table">
        <thead>
        <tr>
            <th scope="col"></th>
            <th scope="col">Название</th>
            <th scope="col">Описание</th>
            <th scope="col">Адрес</th>
            <th scope="col">Тип</th>
            <th scope="col">Статьи</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${accidents}" var="accident">
            <tr>
                <td>
                    <a href='<c:url value="/edit?id=${accident.id}"/>'>
                        <i class="fa fa-edit mr-3"></i>
                    </a>
                </td>
                <td><c:out value="${accident.name}"/></td>
                <td><c:out value="${accident.text}"/></td>
                <td><c:out value="${accident.address}"/></td>
                <td><c:out value="${accident.type.name}"/></td>
                <td>
                    <c:forEach items="${accident.rules}" var="rule">
                        <c:out value="${rule.name}; "/>
                    </c:forEach>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
</body>
</html>