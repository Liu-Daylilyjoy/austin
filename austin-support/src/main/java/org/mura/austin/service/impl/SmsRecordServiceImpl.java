package org.mura.austin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.mura.austin.domain.SmsRecord;
import org.mura.austin.mapper.SmsRecordMapper;
import org.mura.austin.service.SmsRecordService;
import org.springframework.stereotype.Service;

/**
 * @author Akutagawa Murasame
 * @date 2022/3/27 16:17
 */
@Service
public class SmsRecordServiceImpl
        extends ServiceImpl<SmsRecordMapper, SmsRecord>
        implements SmsRecordService {
}
