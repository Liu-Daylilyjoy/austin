package org.mura.austin.dao;

import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Mapper;
import org.mura.austin.domain.SmsRecord;

/**
 * @author Akutagawa Murasame
 * @date 2022/3/27 12:06
 *
 * 操作短信记录的dao
 */
@Mapper
public interface SmsRecordDao extends IService<SmsRecord> {
}
