//package org.jeecg.flow.leave.controller;
//
//import java.util.HashMap;
//import java.util.List;
//
//import org.jeecg.activiti.base.AjaxResult;
//import org.jeecg.activiti.base.BaseController;
//import org.jeecg.activiti.base.SecurityUtils;
//import org.jeecg.activiti.base.page.TableDataInfo;
//import org.jeecgframework.poi.util.ExcelUtil;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import org.jeecg.flow.leave.domain.BizLeave;
//import org.jeecg.flow.leave.service.IBizLeaveService;
//
///**
// * 请假流程Controller
// *
// * @author jeethink
// * @date 2020-09-17
// */
//@RestController
//@RequestMapping("/workflow/leave")
//public class BizLeaveController extends BaseController {
//    @Autowired
//    private IBizLeaveService bizLeaveService;
//
//
//    /**
//     * 查询请假流程列表
//     */
//    @PreAuthorize("@ss.hasPermi('workflow:leave')")
//    @GetMapping("/list")
//    public TableDataInfo list(BizLeave bizLeave) {
////        if (!SysUser.isAdmin(SecurityUtils.getUserId())) {
////            bizLeave.setCreateBy(SecurityUtils.getUsername());
////        }
//        bizLeave.setType("leave");
//        startPage();
//        List<BizLeave> list = bizLeaveService.selectBizLeaveList(bizLeave);
//        return getDataTable(list);
//    }
//
//
//
//    /**
//     * 导出请假流程列表
//     */
//    @PreAuthorize("@ss.hasPermi('workflow:leave')")
//
//    @GetMapping("/export")
//    public AjaxResult export(BizLeave bizLeave) {
//        bizLeave.setType("leave");
//        List<BizLeave> list = bizLeaveService.selectBizLeaveList(bizLeave);
//        ExcelUtil util = new ExcelUtil();
//        return null;
//    }
//
//    /**
//     * 获取请假流程详细信息
//     */
//    @PreAuthorize("@ss.hasPermi('workflow:leave')")
//    @GetMapping(value = "/{id}")
//    public AjaxResult getInfo(@PathVariable("id") Long id) {
//        return AjaxResult.success(bizLeaveService.selectBizLeaveById(id));
//    }
//
//    /**
//     * 新增请假流程
//     */
//    @PreAuthorize("@ss.hasPermi('workflow:leave')")
//
//    @PostMapping
//    public AjaxResult add(@RequestBody BizLeave bizLeave) {
//
//        bizLeave.setType("leave");
//        return toAjax(bizLeaveService.insertBizLeave(bizLeave));
//    }
//
//    /**
//     * 修改请假流程
//     */
//    @PreAuthorize("@ss.hasPermi('workflow:leave')")
//
//    @PutMapping
//    public AjaxResult edit(@RequestBody BizLeave bizLeave) {
//        return toAjax(bizLeaveService.updateBizLeave(bizLeave));
//    }
//
//    /**
//     * 删除请假流程
//     */
//    @PreAuthorize("@ss.hasPermi('workflow:leave')")
//
//    @DeleteMapping("/{ids}")
//    public AjaxResult remove(@PathVariable Long[] ids) {
//        return toAjax(bizLeaveService.deleteBizLeaveByIds(ids));
//    }
//
//    /**
//     * 提交申请
//     */
//
//    @PostMapping( "/submitApply/{id}")
//    @ResponseBody
//    public AjaxResult submitApply(@PathVariable Long id) {
//        BizLeave leave = bizLeaveService.selectBizLeaveById(id);
//        String applyUserId = SecurityUtils.getUsername();
//        bizLeaveService.submitApply(leave, applyUserId, "leave", new HashMap<>());
//        return AjaxResult.success();
//    }
//
//    /**
//     * 我的待办列表
//     * @return
//     */
//    @GetMapping("/taskList")
//    @ResponseBody
//    public TableDataInfo taskList(BizLeave bizLeave) {
//        bizLeave.setType("leave");
//        List<BizLeave> list = bizLeaveService.findTodoTasks(bizLeave, SecurityUtils.getUsername());
//        return getDataTable(list);
//    }
//
//    /**
//     * 我的已办列表
//     * @param bizLeave
//     * @return
//     */
//    @GetMapping("/taskDoneList")
//    @ResponseBody
//    public TableDataInfo taskDoneList(BizLeave bizLeave) {
//        bizLeave.setType("leave");
//        List<BizLeave> list = bizLeaveService.findDoneTasks(bizLeave, SecurityUtils.getUsername());
//        return getDataTable(list);
//    }
//
//
//}
