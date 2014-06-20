/*
 * Created on 2013-04-27 15:09:15
 *
 */
package com.liyiwei.cfw.system.service;

import java.util.Date;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.liyiwei.cfw.system.entity.ImportBatch;

/**
 * @author Liyiwei
 * 
 */
@Service
public class ImportBatchService extends BaseImportBatchService {
	@Transactional(readOnly = false)
	public int addImportBatch(String module, String filename) {
		ImportBatch importBatch = new ImportBatch();
		importBatch.setModule(module);
		importBatch.setFilename(filename);
		importBatch.setTotalCount(0);
		importBatch.setFinishCount(0);
		importBatch.setFailCount(0);
		importBatch.setImportUserId(1);
		importBatch.setImportTime(new Date());
		this.addImportBatch(importBatch);
		return importBatch.getId();
	}

	@Transactional(readOnly = false)
	public void modifyImportBatchResult(int id, int totalCount, int finishCount, int failCount) {
		String hql = "update from ImportBatch importBatch set importBatch.totalCount=" + totalCount + ",importBatch.finishCount=" + finishCount + ", importBatch.failCount=" + failCount
				+ " where importBatch.id=" + id;
		super.execute(hql);
	}

}