package org.mura.austin.cron.pending;

import lombok.extern.slf4j.Slf4j;
import org.mura.austin.cron.domain.CrowdInfoVo;
import org.mura.austin.support.pending.BatchPendingThread;
import org.mura.austin.support.pending.Pending;
import org.mura.austin.support.pending.PendingParam;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Akutagawa Murasame
 * @date 2022/7/27 21:32
 *
 * 批量处理任务信息
 */
@Component
@Slf4j
public class CrowdBatchTaskPending extends Pending<CrowdInfoVo> {
    /**
     * 主要是给线程命名
     */
    @Override
    public void initAndStart(PendingParam pendingParam) {
        threadNum = pendingParam.getThreadNum() == null ? threadNum : pendingParam.getThreadNum();
        queue = pendingParam.getQueue();

        for (int i = 0; i < threadNum; ++i) {
            BatchPendingThread<CrowdInfoVo> batchPendingThread = new BatchPendingThread<>();
            batchPendingThread.setPendingParam(pendingParam);
            batchPendingThread.setName("batchPendingThread-" + i);
            batchPendingThread.start();
        }
    }

    /**
     * TODO
     */
    @Override
    public void doHandle(List<CrowdInfoVo> list) {
        log.info("threadName:{},already Handle:{}", Thread.currentThread().getName(), list.size());
    }
}
