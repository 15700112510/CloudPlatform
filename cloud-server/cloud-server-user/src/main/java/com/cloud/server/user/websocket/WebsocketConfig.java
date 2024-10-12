package com.cloud.server.user.websocket;

import com.cloud.server.user.mapper.UserMapper;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * TODO:
 *
 * @author likain
 * @since 2023/6/11 14:01
 */
@Configuration
public class WebsocketConfig {

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    @Bean
    public WebsocketServer websocketServer(ApplicationContext applicationContext) {
        WebsocketServer.setProperties(applicationContext.getBean(UserMapper.class));
        return new WebsocketServer();
    }
}
