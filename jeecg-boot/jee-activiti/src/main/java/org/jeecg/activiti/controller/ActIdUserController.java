package org.jeecg.activiti.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.activiti.base.BaseController;
import org.jeecg.activiti.base.page.PageDomain;
import org.jeecg.activiti.base.page.TableDataInfo;
import org.jeecg.activiti.base.page.TableSupport;
import org.jeecg.activiti.domain.ActIdUser;


import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.User;
import org.activiti.engine.identity.UserQuery;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * 流程用户Controller
 *
 * @author Xianlu Tech
 * @date 2019-10-02
 */
@Controller
@RequestMapping("/activiti/actIdUser")
public class ActIdUserController extends BaseController {


    @Autowired
    private IdentityService identityService;
    @Autowired
    private ISysUserService userService;


    /**
     * 查询流程用户列表
     */
    @GetMapping("/list")
    @ResponseBody
    public Result<Object> list(ActIdUser query)
    {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();

        UserQuery userQuery = identityService.createUserQuery();
        if (StringUtils.isNotBlank(query.getId())) {
            userQuery.userId(query.getId());
        }
        if (StringUtils.isNotBlank(query.getFirst())) {
            userQuery.userFirstNameLike("%" + query.getFirst() + "%");
        }
        if (StringUtils.isNotBlank(query.getEmail())) {
            userQuery.userEmailLike("%" + query.getEmail() + "%");
        }
        List<User> userList = userQuery.listPage((pageNum - 1) * pageSize, pageSize);
        Page<ActIdUser> list = new Page<>();
        list.setTotal(userQuery.count());
        list.setCurrent(pageNum);
        list.setSize(pageSize);
        List<ActIdUser> ls =new ArrayList();
        for (User user: userList) {
            ActIdUser idUser = new ActIdUser();
            idUser.setId(user.getId());
            idUser.setFirst(user.getFirstName());
            idUser.setEmail(user.getEmail());
            ls.add(idUser);
        }
        list.setRecords(ls);
        return Result.OK(list);
    }

//    /**
//     * 选择系统用户
//     */
//    @GetMapping("/authUser/selectUser")
//    public String selectUser(String taskId, ModelMap mmap) {
//        mmap.put("taskId", taskId);
//        return prefix + "/selectUser";
//    }

//    @PostMapping("/systemUserList")
//    @ResponseBody
//    public TableDataInfo systemUserList(SysUser user) {
//        startPage();
//        List<SysUser> list = userService.selectUserList(user);
//        return getDataTable(list);
//    }

}
