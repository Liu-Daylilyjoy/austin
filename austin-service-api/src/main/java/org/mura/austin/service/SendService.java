package org.mura.austin.service;

import org.mura.austin.domain.BatchSendRequest;
import org.mura.austin.domain.SendRequest;
import org.mura.austin.domain.SendResponse;

/**
 * @author Akutagawa Murasame
 */
public interface SendService {
    /**
     * 单文案发送接口
     */
    SendResponse send(SendRequest sendRequest);

    /**
     * 多文案发送接口，一次远程调用处理多个单次发送的请求，避免多次远程调用单次发送的请求
     */
    SendResponse batchSend(BatchSendRequest batchSendRequest);
}