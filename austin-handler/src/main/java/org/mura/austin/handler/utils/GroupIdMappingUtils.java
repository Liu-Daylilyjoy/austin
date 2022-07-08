package org.mura.austin.handler.utils;

import org.mura.austin.common.domain.TaskInfo;
import org.mura.austin.common.enums.ChannelType;
import org.mura.austin.common.enums.MessageType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Akutagawa Murasame
 * @date 2022/4/30 10:58
 *
 * 通过在这里预置groupId，ReceiverStart会自动将设置的groupId放到各个Receiver的groupId中
 * 这样就预防了为每一个groupId创建一个Receiver类的情况
 */
public class GroupIdMappingUtils {
    /**
     * 获取所有的groupId
     * （不同的渠道不同的消息类型拥有自己的groupId）
     */
    public static List<String> getAllGroupIds() {
        List<String> groupIds = new ArrayList<>();

//        渠道和消息类型是一对多的关系
        for (ChannelType channelType : ChannelType.values()) {
            for (MessageType messageType : MessageType.values()) {
                groupIds.add(channelType.getCodeEn() + "." + messageType.getCodeEn());
            }
        }

        return groupIds;
    }

    /**
     * 根据TaskInfo获取当前消息的groupId
     */
    public static String getGroupIdByTaskInfo(TaskInfo taskInfo) {
        String channelCodeEn = ChannelType.getEnumByCode(taskInfo.getSendChannel()).getCodeEn();
        String msgCodeEn = MessageType.getEnumByCode(taskInfo.getMsgType()).getCodeEn();

        return channelCodeEn + "." + msgCodeEn;
    }
}