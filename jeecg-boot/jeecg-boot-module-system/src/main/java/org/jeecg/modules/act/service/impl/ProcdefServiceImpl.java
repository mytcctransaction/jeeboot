package org.jeecg.modules.act.service.impl;

import org.jeecg.modules.act.entity.Page;
import org.jeecg.modules.act.entity.PageData;

import org.jeecg.modules.act.mapper.ProcdefMapper;
import org.jeecg.modules.act.service.ProcdefService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


@Service(value="procdefServiceImpl")
@Transactional //开启事物
public class ProcdefServiceImpl implements ProcdefService {

	@Resource
	private ProcdefMapper procdefMapper;
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@Override
	public List<PageData> list(Page page)throws Exception{
		return procdefMapper.datalistPage(page);
	}
	
}

