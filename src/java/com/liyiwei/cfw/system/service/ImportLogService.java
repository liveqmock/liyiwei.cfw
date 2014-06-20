/*
 * Created on 2013-04-27 15:09:47
 *
 */
package com.liyiwei.cfw.system.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.liyiwei.cfw.system.entity.ImportLog;

/**
 * @author Liyiwei
 * 
 */
@Service
public class ImportLogService extends BaseImportLogService {
	@Transactional(readOnly = false)
	public void addImportLog(int importBatchId, String module, int objectId) {
		ImportLog importLog = new ImportLog();
		importLog.setModule(module);
		importLog.setImportBatchId(importBatchId);
		importLog.setObjectId(objectId);
		this.addImportLog(importLog);
	}
}