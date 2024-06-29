<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Messages - SwapEx</title>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">
    <style>
        body {
            font-family: 'Poppins', sans-serif;
            background-color: #f8f9fa;
        }
        .message-container {
            max-width: 1000px;
            margin: 2rem auto;
            padding: 2rem;
            background-color: #ffffff;
            border-radius: 10px;
            box-shadow: 0 0 20px rgba(0,0,0,0.1);
        }
        .conversation-list {
            border-right: 1px solid #dee2e6;
            height: 600px;
            overflow-y: auto;
        }
        .conversation-item {
            padding: 15px;
            cursor: pointer;
            border-bottom: 1px solid #f1f3f5;
            transition: background-color 0.3s ease;
        }
        .conversation-item:hover {
            background-color: #f1f3f5;
        }
        .conversation-item.active {
            background-color: #e9ecef;
            border-left: 4px solid #8f0b0b;
        }
        .conversation-item strong {
            font-size: 1.1em;
            color: #333;
        }
        .conversation-item p {
            margin-bottom: 0;
            color: #6c757d;
            font-size: 0.9em;
        }
        .messages-header {
            background-color: #f8f9fa;
            padding: 15px;
            border-bottom: 1px solid #dee2e6;
            margin-bottom: 15px;
        }
        .messages {
            height: 450px;
            overflow-y: auto;
            padding: 15px;
        }
        .message {
            margin-bottom: 1rem;
            padding: 1rem;
            border-radius: 10px;
            max-width: 75%;
        }
        .message-sent {
            background-color: #e9ecef;
            margin-left: auto;
        }
        .message-received {
            background-color: #8f0b0b;
            color: #ffffff;
        }
        .message-content {
            margin-bottom: 5px;
        }
        .message-time {
            font-size: 0.8rem;
            color: #6c757d;
            text-align: right;
        }
        .message-form {
            margin-top: 1rem;
            background-color: #f8f9fa;
            padding: 15px;
            border-top: 1px solid #dee2e6;
        }
        .btn-send {
            background-color: #8f0b0b;
            color: #ffffff;
            transition: background-color 0.3s ease;
        }
        .btn-send:hover {
            background-color: #6d0808;
        }
    </style>
</head>
<body>
<jsp:include page="Header.jsp" />

<div class="container message-container">
    <h2 class="mb-4">Messages</h2>

    <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger" role="alert">
                ${errorMessage}
        </div>
    </c:if>

    <div class="row">
        <div class="col-md-4 conversation-list">
            <c:forEach var="conversation" items="${conversations}">
                <div class="conversation-item ${conversation.id == param.exchangeId ? 'active' : ''}"
                     onclick="loadConversation(${conversation.id}, ${conversation.getOtherUserId(sessionScope.userId)})">
                    <strong>${conversation.getOtherUserUsername(sessionScope.userId)}</strong>
                    <p>${fn:substring(conversation.status, 0, 30)}...</p>
                </div>
            </c:forEach>
        </div>
        <div class="col-md-8">
            <div class="messages-header">
                <h4>${selectedItem}</h4>
                <p>Exchanging with: ${selectedConversation.getOtherUserUsername(sessionScope.userId)}</p>
            </div>
            <div class="messages">
                <c:forEach var="message" items="${messages}">
                    <div class="message ${message.senderId == sessionScope.userId ? 'message-sent' : 'message-received'}">
                        <div class="message-content">${message.content}</div>
                        <div class="message-time">${message.createdAt}</div>
                    </div>
                </c:forEach>
            </div>

            <form class="message-form" action="MessageServlet" method="post">
                <input type="hidden" name="exchangeId" value="${param.exchangeId}">
                <input type="hidden" name="senderId" value="${sessionScope.userId}">
                <input type="hidden" name="receiverId" value="${param.receiverId}">
                <div class="form-group">
                    <textarea class="form-control" name="content" rows="3" placeholder="Type your message here..." required></textarea>
                </div>
                <button type="submit" class="btn btn-send">
                    <i class="fas fa-paper-plane mr-2"></i>Send Message
                </button>
            </form>
        </div>
    </div>
</div>

<jsp:include page="Footer.jsp" />

<script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.2/dist/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
<script>
    $(document).ready(function() {
        scrollToBottom();
    });

    function scrollToBottom() {
        console.log("Scrolling to bottom");
        $(".messages").scrollTop($(".messages")[0].scrollHeight);
    }

    function loadConversation(exchangeId, receiverId) {
        console.log("Loading conversation with exchangeId: " + exchangeId + ", receiverId: " + receiverId);
        window.location.href = 'MessageServlet?exchangeId=' + exchangeId + '&receiverId=' + receiverId;
    }
</script>
</body>
</html>
