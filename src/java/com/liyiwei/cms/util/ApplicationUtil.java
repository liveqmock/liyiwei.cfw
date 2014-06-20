/*
 * Created on 2013-03-12 18:31:00
 *
 */
package com.liyiwei.cms.util;

import com.liyiwei.cms.article.service.ChannelService;

/**
 * @author Liyiwei
 * 
 */
public class ApplicationUtil extends com.liyiwei.cfw.util.ApplicationUtil {
	private static ChannelService channelService;

	public static ChannelService getChannelService() {
		if (channelService == null) {
			channelService = (ChannelService) applicationContext.getBean("channelService");
		}
		return channelService;
	}

}