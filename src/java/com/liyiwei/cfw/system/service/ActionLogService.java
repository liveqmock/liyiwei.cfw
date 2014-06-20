/*
 * Created on 2013-02-17 19:33:51
 *
 */
package com.liyiwei.cfw.system.service;

import java.util.Date;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.liyiwei.cfw.system.entity.ActionLog;

/**
 * @author Liyiwei
 * 
 */
@Service
public class ActionLogService extends BaseActionLogService {
	@Transactional(readOnly = false)
	public void writeActionLog(String system, String module, String action, String objectId, String note, String ip, int userId) {
		ActionLog actionLog = new ActionLog();
		actionLog.setSystem(system);
		actionLog.setModule(module);
		actionLog.setAction(action);
		actionLog.setObjectId(objectId);
		actionLog.setNote(note);
		actionLog.setUserId(userId);
		actionLog.setIp(ip);
		actionLog.setLogTime(new Date());
		addActionLog(actionLog);
	}

	@Transactional(readOnly = false)
	public void writeActionLog(String system, String module, String action, String note, String ip, int userId) {
		writeActionLog(system, module, action, null, note, ip, userId);
	}

	@Transactional(readOnly = false)
	public void writeLoginLog(String system, String note, String ip, int userId) {
		writeActionLog(system, "用户", "登录", note, ip, userId);
	}

	@Transactional(readOnly = false)
	public void writeLogoutLog(String system, String ip, int userId) {
		writeActionLog(system, "用户", "退出登录", "", ip, userId);
	}
}