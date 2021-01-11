package org.jeecg.modules.act.mapper;



import org.jeecg.modules.act.entity.Page;
import org.jeecg.modules.act.entity.PageData;

import java.util.List;

/** 
 * 说明： 流程管理Mapper
 * 作者：FH Admin QQ313596790
 * 官网：www.fhadmin.org
 * @version
 */
public interface ProcdefMapper{
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	List<PageData> datalistPage(Page page);
	
}

