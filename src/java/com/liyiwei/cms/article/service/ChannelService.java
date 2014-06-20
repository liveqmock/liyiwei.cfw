/*
 * Created on 2013-03-13 19:01:00
 *
 */
package com.liyiwei.cms.article.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.liyiwei.cms.article.entity.Channel;
import com.liyiwei.common.cache.Cache;
import com.liyiwei.common.util.StringUtil;
import com.liyiwei.common.web.Html;

/**
 * @author Liyiwei
 * 
 */
@Service
public class ChannelService extends BaseChannelService {
	@Transactional(readOnly = false)
	public void upChannelSeq(int parentId, int sno) {
		List<Channel> list = getChannelList("where  channel.parentId=" + parentId + " order by  channel.seq,channel.id");
		int i = 1;
		for (Channel channel : list) {
			if (i == sno - 1) {
				channel.setSeq(i + 1);
			} else if (i == sno) {
				channel.setSeq(i - 1);
			} else {
				channel.setSeq(i);
			}
			super.modifyChannel(channel);
			i++;
		}
		Cache.removeStartsWith("Channel");
	}

	@Transactional(readOnly = false)
	public void downChannelSeq(int parentId, int sno) {
		List<Channel> list = getChannelList("where  channel.parentId=" + parentId + " order by  channel.seq,channel.id");
		int i = 1;
		for (Channel channel : list) {
			if (i == sno) {
				channel.setSeq(i + 1);
			} else if (i == sno + 1) {
				channel.setSeq(i - 1);
			} else {
				channel.setSeq(i);
			}
			super.modifyChannel(channel);
			i++;
		}
		Cache.removeStartsWith("Channel");
	}

	public String getChannelName(Integer id) {
		if (id == null) {
			return "";
		}

		Channel channel = getChannel(id.intValue());
		return channel == null ? "" : channel.getName();
	}

	public String channelSelectBox(String selectBoxName, String selectData) {
		return channelSelectBox(selectBoxName, selectBoxName, selectData, "", "");
	}

	public String channelSelectBox(String selectBoxName, String selectData, String message) {
		return channelSelectBox(selectBoxName, selectBoxName, selectData, message, "");
	}

	public String channelSelectBox(String selectBoxName, String selectBoxId, String selectData, String message, String other) {
		List<Channel> list = getChannelList();
		if (list != null && list.size() > 0) {
			String[][] paras = new String[list.size() + 1][2];
			paras[0][0] = "";
			paras[0][1] = message;

			int i = 1;
			for (Channel channel : list) {
				paras[i][0] = String.valueOf(channel.getId());
				paras[i][1] = channel.getName();
				i++;
			}
			return Html.selectBox(selectBoxName, selectBoxId, paras, selectData, other);
		} else {
			String[][] paras = new String[1][2];
			paras[0][0] = "";
			paras[0][1] = message;
			return Html.selectBox(selectBoxName, selectBoxId, paras, selectData, other);
		}
	}

	@Override
	public Channel getChannel(int id) {
		return getChannelByCache(id);
	}

	@Override
	public List<Channel> getChannelList() {
		return getChannelListByCache();
	}

	@SuppressWarnings("unchecked")
	private Channel getChannelByCache(int id) {
		HashMap<Integer, Channel> hashMap = (HashMap<Integer, Channel>) Cache.get("Channel");
		if (hashMap == null) {
			hashMap = new HashMap<Integer, Channel>();
			List<Channel> list = getChannelList(" order by channel.seq,channel.id");
			for (Channel channel : list) {
				hashMap.put(channel.getId(), channel);
			}
			Cache.put("Channel", hashMap);
			Cache.put("ChannelList", list);
		}

		return (Channel) hashMap.get(id);
	}

	@SuppressWarnings("unchecked")
	private List<Channel> getChannelListByCache() {
		List<Channel> list = (List<Channel>) Cache.get("ChannelList");
		if (list == null) {
			HashMap<Integer, Channel> hashMap = new HashMap<Integer, Channel>();
			list = getChannelList(" order by channel.seq,channel.id");
			for (Channel channel : list) {
				hashMap.put(channel.getId(), channel);
			}
			Cache.put("Channel", hashMap);
			Cache.put("ChannelList", list);
		}

		return list;
	}

	@Override
	@Transactional(readOnly = false)
	public void addChannel(Channel channel) {
		super.addChannel(channel);
		Cache.removeStartsWith("Channel");
	}

	@Override
	@Transactional(readOnly = false)
	public void modifyChannel(Channel channel) {
		super.modifyChannel(channel);
		Cache.removeStartsWith("Channel");
	}

	@Override
	@Transactional(readOnly = false)
	public void deleteChannel(int id) {
		super.deleteChannel(id);
		Cache.removeStartsWith("Channel");
	}

	@Override
	@Transactional(readOnly = false)
	public void deleteChannelByIds(String ids) {
		super.deleteChannelByIds(ids);
		Cache.removeStartsWith("Channel");
	}

	public String getChannelIds(int id) {
		List<Channel> list = null;
		if (id == 0) {
			list = this.getChannelList(" where channel.parentId is null or channel.parentId = 0");
		} else {
			list = this.getChannelList(" where channel.parentId = " + id);
		}

		String channelIds = StringUtil.toString(id);
		if (list == null || list.size() == 0) {
			return channelIds;
		} else {
			for (Channel channel : list) {
				channelIds += "," + getChannelIds(channel.getId());
			}
		}
		return channelIds;
	}
}