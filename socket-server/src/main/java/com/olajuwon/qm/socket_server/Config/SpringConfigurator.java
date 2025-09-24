package com.olajuwon.qm.socket_server.Config;


import jakarta.websocket.server.ServerEndpointConfig;
import org.springframework.context.ApplicationContext;

public class SpringConfigurator extends ServerEndpointConfig.Configurator {

    private static ApplicationContext context;

    public static void setApplicationContext(ApplicationContext ctx) {
        context = ctx;
    }

    @Override
    public <T> T getEndpointInstance(Class<T> endpointClass) throws InstantiationException {
        if (context == null) {
            throw new IllegalStateException("Spring ApplicationContext not initialized!");
        }
        return context.getBean(endpointClass);
    }
}