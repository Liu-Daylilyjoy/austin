package org.mura.austin.dao;

import org.mura.austin.domain.SmsRecord;
import org.springframework.data.repository.CrudRepository;

/**
 * @author Akutagawa Murasame
 * @date 2022/3/27 16:13
 */
public interface SmsRecordDao extends CrudRepository<SmsRecord, Long> {
}
