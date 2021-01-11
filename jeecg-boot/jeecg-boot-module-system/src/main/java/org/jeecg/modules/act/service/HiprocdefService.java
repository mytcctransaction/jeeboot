package org.jeecg.modules.act.service;



import org.jeecg.modules.act.entity.Page;
import org.jeecg.modules.act.entity.PageData;

import java.util.List;

/** 
 * 说明： 历史流程任务接口
 * 创建人：FH Q313596790
 * 官网：www.fhadmin.org
 */
public interface HiprocdefService {
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception;
	
	/**历史流程变量列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> hivarList(PageData pd)throws Exception;

}
