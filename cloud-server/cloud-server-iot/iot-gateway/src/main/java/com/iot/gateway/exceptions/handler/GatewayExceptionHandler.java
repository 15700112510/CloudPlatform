package com.iot.gateway.exceptions.handler;

import com.cloud.common.core.R;
import com.cloud.common.core.enums.ErrorEnum;
import com.iot.gateway.exceptions.GatewayMqttException;
import com.iot.gateway.exceptions.ReplyTimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * TODO:
 *
 * @author likain
 * @since 2023/5/29 14:39
 */
@RestControllerAdvice
public class GatewayExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(GatewayExceptionHandler.class);

    @ExceptionHandler(ReplyTimeoutException.class)
    public R handleReplyTimeoutException(ReplyTimeoutException e) {
        return R.fail(ErrorEnum.TIMEOUT_ERROR);
    }

    @ExceptionHandler(GatewayMqttException.class)
    public R handleGatewayMqttException(GatewayMqttException e) {
        logger.error("Mqtt call error, cause: {}", e.getMessage(), e);
        return R.fail(ErrorEnum.MQTT_ERROR);
    }
}
