//package org.jeecg.flow.leave.service.impl;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
//import org.jeecg.activiti.base.SecurityUtils;
//import org.jeecg.activiti.base.page.PageDomain;
//import org.jeecg.activiti.base.page.TableSupport;
//import org.jeecg.activiti.base.utils.DateUtils;
//import org.jeecg.activiti.base.utils.StringUtils;
//import org.jeecg.activiti.service.IProcessService;
//
//import org.jeecg.flow.leave.domain.BizLeave;
//import org.jeecg.flow.leave.mapper.BizLeaveMapper;
//import org.jeecg.flow.leave.service.IBizLeaveService;
//
//
//import org.activiti.engine.HistoryService;
//import org.activiti.engine.RuntimeService;
//import org.activiti.engine.TaskService;
//import org.activiti.engine.history.HistoricProcessInstance;
//import org.activiti.engine.history.HistoricTaskInstance;
//import org.activiti.engine.impl.persistence.entity.TaskEntityImpl;
//import org.activiti.engine.runtime.ProcessInstance;
//import org.activiti.engine.task.Task;
//import org.jeecg.modules.system.entity.SysAnnouncement;
//import org.jeecg.modules.system.entity.SysUser;
//import org.jeecg.modules.system.mapper.SysAnnouncementMapper;
//import org.jeecg.modules.system.service.ISysUserService;
//import org.springframework.beans.BeanUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.util.CollectionUtils;
//
//import javax.annotation.Resource;
//
///**
// * 请假流程Service业务层处理
// *
// * @author jeethink
// * @date 2020-09-17
// */
//@Service
//public class BizLeaveServiceImpl extends ServiceImpl<BizLeaveMapper, BizLeave> implements IBizLeaveService {
//
//    @Resource
//    private BizLeaveMapper bizLeaveMapper;
//    @Autowired
//    private ISysUserService sysUserService;
//    @Autowired
//    private TaskService taskService;
//    @Autowired
//    private IProcessService processService;
//    @Autowired
//    private RuntimeService runtimeService;
//    @Autowired
//    private HistoryService historyService;
//    /**
//     * 查询请假流程
//     *
//     * @param id 请假流程ID
//     * @return 请假流程
//     */
//    @Override
//    public BizLeave selectBizLeaveById(Long id) {
//        BizLeave leave = bizLeaveMapper.selectBizLeaveById(id);
//        //SysUser sysUser = userMapper.selectUserByUserName(leave.getApplyUser());
//        SysUser sysUser=sysUserService.getUserByName(leave.getApplyUser());
//        if (sysUser != null) {
//            leave.setApplyUserName(sysUser.getUsername());
//        }
//        return leave;
//    }
//
//    /**
//     * 查询请假流程列表
//     *
//     * @param bizLeave 请假流程
//     * @return 请假流程
//     */
//    @Override
//    public List<BizLeave> selectBizLeaveList(BizLeave bizLeave) {
//        PageDomain pageDomain = TableSupport.buildPageRequest();
//        Integer pageNum = pageDomain.getPageNum();
//        Integer pageSize = pageDomain.getPageSize();
//return null;
//        // PageHelper 仅对第一个 List 分页
////        Page<BizLeave> list = (Page<BizLeave>) bizLeaveMapper.selectBizLeaveList(bizLeave);
////        Page<BizLeave> returnList = new Page<>();
////        for (BizLeave leave: list) {
////            //SysUser sysUser = userMapper.selectUserByUserName(leave.getCreateBy());
////            SysUser sysUser=sysUserService.getUserByName(leave.getCreateBy());
////            if (sysUser != null) {
////                leave.setCreateUserName(sysUser.getUsername());
////            }
////            //SysUser sysUser2 = userMapper.selectUserByUserName(leave.getApplyUser());
////            SysUser sysUser2=sysUserService.getUserByName(leave.getApplyUser());
////            if (sysUser2 != null) {
////                leave.setApplyUserName(sysUser2.getUsername());
////            }
////            // 当前环节
////            if (StringUtils.isNotBlank(leave.getInstanceId())) {
////                List<Task> taskList = taskService.createTaskQuery()
////                        .processInstanceId(leave.getInstanceId())
//////                        .singleResult();
////                        .list();    // 例如请假会签，会同时拥有多个任务
////                if (!CollectionUtils.isEmpty(taskList)) {
////                    TaskEntityImpl task = (TaskEntityImpl) taskList.get(0);
////                    leave.setTaskId(task.getId());
////                    if (task.getSuspensionState() == 2) {
////                        leave.setTaskName("已挂起");
////                        leave.setSuspendState("2");
////                    } else {
////                        leave.setTaskName(task.getName());
////                        leave.setSuspendState("1");
////                    }
////                } else {
////                    // 已办结或者已撤销
////                    leave.setTaskName("已结束");
////                }
////            } else {
////                leave.setTaskName("未启动");
////            }
////            returnList.add(leave);
////        }
////        returnList.setTotal(CollectionUtils.isEmpty(list) ? 0 : list.getTotal());
////        returnList.setPageNum(pageNum);
////        returnList.setPageSize(pageSize);
////        return returnList;
//    }
//
//    /**
//     * 新增请假流程
//     *
//     * @param bizLeave 请假流程
//     * @return 结果
//     */
//    @Override
//    public int insertBizLeave(BizLeave bizLeave) {
//        bizLeave.setCreateBy(SecurityUtils.getUsername());
//        bizLeave.setCreateTime(DateUtils.getNowDate());
//        return bizLeaveMapper.insertBizLeave(bizLeave);
//    }
//
//    /**
//     * 修改请假流程
//     *
//     * @param bizLeave 请假流程
//     * @return 结果
//     */
//    @Override
//    public int updateBizLeave(BizLeave bizLeave) {
//        bizLeave.setUpdateTime(DateUtils.getNowDate());
//        return bizLeaveMapper.updateBizLeave(bizLeave);
//    }
//
//    /**
//     * 批量删除请假流程
//     *
//     * @param ids 需要删除的请假流程ID
//     * @return 结果
//     */
//    @Override
//    public int deleteBizLeaveByIds(Long[] ids) {
//        return bizLeaveMapper.deleteBizLeaveByIds(ids);
//    }
//
//    /**
//     * 删除请假流程信息
//     *
//     * @param id 请假流程ID
//     * @return 结果
//     */
//    @Override
//    public int deleteBizLeaveById(Long id) {
//        return bizLeaveMapper.deleteBizLeaveById(id);
//    }
//
//    /**
//     * 启动流程
//     * @param entity
//     * @param applyUserId
//     * @return
//     */
//    @Override
//    public ProcessInstance submitApply(BizLeave entity, String applyUserId, String key, Map<String, Object> variables) {
//        entity.setApplyUser(applyUserId);
//        entity.setApplyTime(DateUtils.getNowDate());
//        entity.setUpdateBy(applyUserId);
//        bizLeaveMapper.updateBizLeave(entity);
//        String businessKey = entity.getId().toString(); // 实体类 ID，作为流程的业务 key
//
//        ProcessInstance processInstance = processService.submitApply(applyUserId, businessKey, entity.getTitle(), entity.getReason(), key, variables);
//
//        String processInstanceId = processInstance.getId();
//        entity.setInstanceId(processInstanceId); // 建立双向关系
//        bizLeaveMapper.updateBizLeave(entity);
//
//        return processInstance;
//    }
//
//
//    /**
//     * 查询待办任务
//     */
//    @Override
//    public List<BizLeave> findTodoTasks(BizLeave leave, String userId) {
//        // 手动分页
////        PageDomain pageDomain = TableSupport.buildPageRequest();
////        Integer pageNum = pageDomain.getPageNum();
////        Integer pageSize = pageDomain.getPageSize();
////        Page<BizLeave> list = new Page<>();
////
////        List<BizLeave> results = new ArrayList<>();
////        List<Task> tasks = processService.findTodoTasks(userId, leave.getType());
////        // 根据流程的业务ID查询实体并关联
////        for (Task task : tasks) {
////            TaskEntityImpl taskImpl = (TaskEntityImpl) task;
////            String processInstanceId = taskImpl.getProcessInstanceId();
////            // 条件过滤 1
////            if (StringUtils.isNotBlank(leave.getInstanceId()) && !leave.getInstanceId().equals(processInstanceId)) {
////                continue;
////            }
////            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
////            String businessKey = processInstance.getBusinessKey();
////            BizLeave leave2 = bizLeaveMapper.selectBizLeaveById(new Long(businessKey));
////            // 条件过滤 2
////            if (StringUtils.isNotBlank(leave.getType()) && !leave.getType().equals(leave2.getType())) {
////                continue;
////            }
////            leave2.setTaskId(taskImpl.getId());
////            if (taskImpl.getSuspensionState() == 2) {
////                leave2.setTaskName("已挂起");
////            } else {
////                leave2.setTaskName(taskImpl.getName());
////            }
////
////            //SysUser sysUser = userMapper.selectUserByUserName(leave2.getApplyUser());
////            SysUser sysUser=sysUserService.getUserByName(leave2.getApplyUser());
////            leave2.setApplyUserName(sysUser.getUsername());
////            results.add(leave2);
////        }
////
////        List<BizLeave> tempList;
////        if (pageNum != null && pageSize != null) {
////            int maxRow = (pageNum - 1) * pageSize + pageSize > results.size() ? results.size() : (pageNum - 1) * pageSize + pageSize;
////            tempList = results.subList((pageNum - 1) * pageSize, maxRow);
////            list.setTotal(results.size());
////            list.setPageNum(pageNum);
////            list.setPageSize(pageSize);
////        } else {
////            tempList = results;
////        }
////
////        list.addAll(tempList);
////
////        return list;
//        return null;
//    }
//    /**
//     * 查询已办列表
//     * @param bizLeave
//     * @param userId
//     * @return
//     */
//    @Override
//    public List<BizLeave> findDoneTasks(BizLeave bizLeave, String userId) {
//        return null;
//        // 手动分页
////        PageDomain pageDomain = TableSupport.buildPageRequest();
////        Integer pageNum = pageDomain.getPageNum();
////        Integer pageSize = pageDomain.getPageSize();
////        Page<BizLeave> list = new Page<>();
////
////        List<BizLeave> results = new ArrayList<>();
////        List<HistoricTaskInstance> hisList = processService.findDoneTasks(userId, bizLeave.getType());
////        // 根据流程的业务ID查询实体并关联
////        for (HistoricTaskInstance instance : hisList) {
////            String processInstanceId = instance.getProcessInstanceId();
////            // 条件过滤 1
////            if (StringUtils.isNotBlank(bizLeave.getInstanceId()) && !bizLeave.getInstanceId().equals(processInstanceId)) {
////                continue;
////            }
////            HistoricProcessInstance processInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
////            String businessKey = processInstance.getBusinessKey();
////            BizLeave leave2 = bizLeaveMapper.selectBizLeaveById(new Long(businessKey));
////            BizLeave newLeave = new BizLeave();
////            BeanUtils.copyProperties(leave2, newLeave);
////            // 条件过滤 2
////            if (StringUtils.isNotBlank(bizLeave.getType()) && !bizLeave.getType().equals(leave2.getType())) {
////                continue;
////            }
////            newLeave.setTaskId(instance.getId());
////            newLeave.setTaskName(instance.getName());
////            newLeave.setDoneTime(instance.getEndTime());
////            SysUser sysUser = sysUserService.getUserByName(leave2.getApplyUser());
////            newLeave.setApplyUserName(sysUser.getUsername());
////            results.add(newLeave);
////        }
////
////        List<BizLeave> tempList;
////        if (pageNum != null && pageSize != null) {
////            int maxRow = (pageNum - 1) * pageSize + pageSize > results.size() ? results.size() : (pageNum - 1) * pageSize + pageSize;
////            tempList = results.subList((pageNum - 1) * pageSize, maxRow);
////            list.setTotal(results.size());
////            list.setPageNum(pageNum);
////            list.setPageSize(pageSize);
////        } else {
////            tempList = results;
////        }
////
////        list.addAll(tempList);
////
////        return list;
//    }
//}
