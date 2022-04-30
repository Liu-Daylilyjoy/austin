package org.mura.austin.handler;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Akutagawa Murasame
 * @date 2022/4/30 8:26
 *
 * 多种业务需要多种handler，每种handler都对应一种或多种channel
 * 这个类就是用来处理这个映射关系的
 */
@Component
public class HandlerHolder {
    private final Map<Integer, Handler> handlerMap = new HashMap<>(32);

    public void putHandler(Integer channelCode, Handler handler) {
        handlerMap.put(channelCode, handler);
    }

    public Handler route(Integer channelCode) {
        return handlerMap.get(channelCode);
    }
}
