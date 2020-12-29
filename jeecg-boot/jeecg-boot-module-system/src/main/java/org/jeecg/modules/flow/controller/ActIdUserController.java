package org.jeecg.modules.flow.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.User;
import org.activiti.engine.identity.UserQuery;
import org.apache.commons.lang3.StringUtils;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.flow.domain.ActIdUser;
import org.jeecg.modules.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 流程用户Controller
 *
 * @author Xianlu Tech
 * @date 2019-10-02
 */
@RestController
@RequestMapping("/activiti/actIdUser")
public class ActIdUserController  {


    @Autowired
    private IdentityService identityService;
    @Autowired
    private ISysUserService userService;


    /**
     * 查询流程用户列表
     */
    @GetMapping("/list")
    @ResponseBody
    public Result<Object> list(@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                               @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,ActIdUser query)
    {


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
        List<User> userList = userQuery.listPage((pageNo - 1) * pageSize, pageSize);
        Page<ActIdUser> list = new Page<>();
        list.setTotal(userQuery.count());
        list.setCurrent(pageNo);
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
