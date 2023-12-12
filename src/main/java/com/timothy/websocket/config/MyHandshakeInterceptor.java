package com.timothy.websocket.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

public class MyHandshakeInterceptor implements HandshakeInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(MyHandshakeInterceptor.class);

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
            WebSocketHandler wsHandler, java.util.Map<String, Object> attributes) throws Exception {
        logger.info("Before Handshake: Client connected from {}", request.getRemoteAddress());

        // Log request parameters
        String queryString = request.getURI().getQuery();
        if (queryString != null) {
            String[] queryParams = queryString.split("&");
            for (String param : queryParams) {
                String[] keyValue = param.split("=");
                if (keyValue.length == 2) {
                    logger.info("Request Parameter - {}: {}", keyValue[0], keyValue[1]);
                }
            }
        }

        // Log request body (if available)
        String requestBody = request.getBody() != null ? request.getBody().toString() : "No request body";
        logger.info("Request Body: {}", requestBody);

        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
            WebSocketHandler wsHandler, Exception exception) {
        // No action needed after handshake
        logger.info("After Handshake: Client connected from {}", request.getRemoteAddress());
    }
}
