package org.mura.austin.service;

import org.mura.austin.domain.SendRequest;
import org.mura.austin.domain.SendResponse;

/**
 * @author Akutagawa Murasame
 */
public interface SendService {
    /**
     * 单次发送
     */
    SendResponse send(SendRequest sendRequest);

    /**
     * 批量发送
     */
    SendResponse batchSend(SendRequest sendRequest);
}
