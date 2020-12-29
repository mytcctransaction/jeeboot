package org.jeecg.modules.flow.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.GroupQuery;
import org.apache.commons.lang3.StringUtils;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.flow.domain.ActIdGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 流程用户组Controller
 *
 * @author Xianlu Tech
 * @date 2019-10-02
 */
@RestController
@RequestMapping("/activiti/actIdGroup")
@Slf4j
public class ActIdGroupController
{


    @Autowired
    private IdentityService identityService;


    /**
     * 查询流程用户组列表
     */
    @GetMapping("/list")
    @ResponseBody
    public Result<Object> list(@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                               @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,ActIdGroup query)
    {


        GroupQuery groupQuery = identityService.createGroupQuery();
        if (StringUtils.isNotBlank(query.getId())) {
            groupQuery.groupId(query.getId());
        }
        if (StringUtils.isNotBlank(query.getName())) {
            groupQuery.groupNameLike("%" + query.getName() + "%");
        }
        List<Group> groupList = groupQuery.listPage((pageNo - 1) * pageSize, pageSize);
        Page<ActIdGroup> list = new Page<>();
        list.setTotal(groupQuery.count());
        //list.setPageNum(pageNum);
        list.setCurrent(pageNo);
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
