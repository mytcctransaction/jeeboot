package org.jeecg.modules.act.mapper;



import org.jeecg.modules.act.entity.Page;
import org.jeecg.modules.act.entity.PageData;

import java.util.List;

/** 
 * 说明： 历史流程Mapper
 * 作者：FH Admin QQ313596790
 * 官网：www.fhadmin.org
 */
public interface HiprocdefMapper {
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> datalistPage(Page page)throws Exception;
	
	/**历史流程变量列表
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> hivarList(PageData pd)throws Exception;

}
