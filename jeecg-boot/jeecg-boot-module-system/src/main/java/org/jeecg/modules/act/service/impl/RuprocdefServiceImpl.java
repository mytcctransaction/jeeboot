package org.jeecg.modules.act.service.impl;
import org.jeecg.modules.act.entity.Page;
import org.jeecg.modules.act.entity.PageData;

import org.jeecg.modules.act.mapper.RuprocdefMapper;
import org.jeecg.modules.act.service.RuprocdefService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


@Service(value="ruprocdefServiceImpl")
@Transactional //开启事物
public class RuprocdefServiceImpl implements RuprocdefService {

	@Resource
	private RuprocdefMapper ruprocdefMapper;
	

	/**待办任务 or正在运行任务列表
	 * @param page
	 * @throws Exception
	 */
	@Override
	public List<PageData> list(Page page)throws Exception{
		return ruprocdefMapper.datalistPage(page);
	}
	
	/**流程变量列表
	 * @param page
	 * @throws Exception
	 */
	@Override
	public List<PageData> varList(PageData pd)throws Exception{
		return ruprocdefMapper.varList(pd);
	}
	
	/**历史任务节点列表
	 * @param page
	 * @throws Exception
	 */
	@Override
	public List<PageData> hiTaskList(PageData pd)throws Exception{
		return ruprocdefMapper.hiTaskList(pd);
	}
	
	/**已办任务列表列表
	 * @param page
	 * @throws Exception
	 */
	@Override
	public List<PageData> hitasklist(Page page)throws Exception{
		return ruprocdefMapper.hitaskdatalistPage(page);
	}
	
	/**激活or挂起任务(指定某个任务)
	 * @param pd
	 * @throws Exception
	 */
	@Override
	public void onoffTask(PageData pd)throws Exception{
		ruprocdefMapper.onoffTask(pd);;
	}
	
	/**激活or挂起任务(指定某个流程的所有任务)
	 * @param pd
	 * @throws Exception
	 */
	@Override
	public void onoffAllTask(PageData pd)throws Exception{
		ruprocdefMapper.onoffAllTask(pd);;
	}

}
