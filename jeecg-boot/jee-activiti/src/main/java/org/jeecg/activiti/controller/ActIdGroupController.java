package org.jeecg.activiti.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.activiti.base.BaseController;
import org.jeecg.activiti.base.page.PageDomain;
import org.jeecg.activiti.base.page.TableDataInfo;
import org.jeecg.activiti.base.page.TableSupport;
import org.jeecg.activiti.base.utils.StringUtils;
import org.jeecg.activiti.domain.ActIdGroup;


import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.GroupQuery;
import org.jeecg.activiti.base.BaseController;
import org.jeecg.activiti.base.page.PageDomain;
import org.jeecg.activiti.base.page.TableDataInfo;
import org.jeecg.activiti.base.page.TableSupport;
import org.jeecg.activiti.base.utils.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * 流程用户组Controller
 *
 * @author Xianlu Tech
 * @date 2019-10-02
 */
@Controller
@RequestMapping("/activiti/actIdGroup")
public class ActIdGroupController extends BaseController
{


    @Autowired
    private IdentityService identityService;


    /**
     * 查询流程用户组列表
     */
    @GetMapping("/list")
    @ResponseBody
    public Result<Object> list(ActIdGroup query)
    {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();

        GroupQuery groupQuery = identityService.createGroupQuery();
        if (StringUtils.isNotBlank(query.getId())) {
            groupQuery.groupId(query.getId());
        }
        if (StringUtils.isNotBlank(query.getName())) {
            groupQuery.groupNameLike("%" + query.getName() + "%");
        }
        List<Group> groupList = groupQuery.listPage((pageNum - 1) * pageSize, pageSize);
        Page<ActIdGroup> list = new Page<>();
        list.setTotal(groupQuery.count());
        //list.setPageNum(pageNum);
        list.setCurrent(pageNum);
        //list.setPageSize(pageSize);
        list.setSize(pageSize);
        List<ActIdGroup> ls=new ArrayList();
        for (Group group: groupList) {
            ActIdGroup idGroup = new ActIdGroup();
            idGroup.setId(group.getId());
            idGroup.setName(group.getName());
            ls.add(idGroup);
        }
        list.setRecords(ls);
        return Result.OK(list);
    }

}
