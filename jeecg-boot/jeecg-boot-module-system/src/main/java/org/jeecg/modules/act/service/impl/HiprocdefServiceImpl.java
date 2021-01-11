package org.jeecg.modules.act.service.impl;

import org.jeecg.modules.act.entity.Page;
import org.jeecg.modules.act.entity.PageData;

import org.jeecg.modules.act.mapper.HiprocdefMapper;
import org.jeecg.modules.act.service.HiprocdefService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;


@Service(value="hiprocdefServiceImpl")
@Transactional //开启事物
public class HiprocdefServiceImpl implements HiprocdefService {

	@Resource
	private HiprocdefMapper hiprocdefMapper;
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@Override
	public List<PageData> list(Page page) throws Exception {
		return hiprocdefMapper.datalistPage(page);
	}

	/**历史流程变量列表
	 * @param page
	 * @throws Exception
	 */
	@Override
	public List<PageData> hivarList(PageData pd) throws Exception {
		return hiprocdefMapper.hivarList(pd);
	}

}
