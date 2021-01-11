package org.jeecg.modules.act.service;

import org.jeecg.modules.act.entity.Page;
import org.jeecg.modules.act.entity.PageData;

import java.util.List;

public interface ProcdefService{
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception;
	
}

