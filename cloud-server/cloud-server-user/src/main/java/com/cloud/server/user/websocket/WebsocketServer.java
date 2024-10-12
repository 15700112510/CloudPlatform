package com.cloud.server.user.websocket;

import com.cloud.common.core.utils.JwtUtil;
import com.cloud.server.user.mapper.UserMapper;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;

import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.HashMap;
import java.util.Map;

/**
 * TODO:
 *
 * @author likain
 * @since 2023/6/11 14:03
 */
@Slf4j
@ServerEndpoint("/ws/{token}")
public class WebsocketServer {

    private static Map<Session, String> userSessions;
    private static UserMapper userMapper;

    public static void setProperties(UserMapper mapper) {
        userSessions = new HashMap<>();
        userMapper = mapper;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) {
        Claims claims = JwtUtil.parseToken(token);
        String username = (String) claims.get("username");
        log.info("User [{}] has login", username);

        userSessions.put(session, username);
    }

    @OnClose
    public void onClose(Session session) {
        String username = userSessions.get(session);
        userSessions.remove(session);

        if (!userSessions.containsValue(username)) {
            userMapper.logout(username);
            log.info("User [{}] has disconnected", username);
        }
    }
}

