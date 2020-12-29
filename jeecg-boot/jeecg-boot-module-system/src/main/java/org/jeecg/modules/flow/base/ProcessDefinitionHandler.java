/* CopyRight UMF */
package org.jeecg.modules.flow.base;

import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @description 部署流程
 * @author wangyu
 * @date 2019/11/1 10:46
 */
public class ProcessDefinitionHandler {

    private Logger logger = LoggerFactory.getLogger(ProcessDefinitionHandler.class);
//    private ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
//    private RepositoryService repositoryService = processEngine.getRepositoryService();
    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private ProcessEngine processEngine;


    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;
    /**
     * @description 部署流程模板
     * @param name
     * @return
     */
    public boolean deploymentProcessDefinition_classpath(String name){

        Deployment deployment = null;//完成部署
        try {
            deployment = repositoryService //与流程定义和部署对象相关的Service
                    .createDeployment()//创建一个部署对象
                    .name(name)//添加部署的名称
                    .addClasspathResource("processes/" + name + ".bpmn") //从classpath的资源中加载，一次只能加载一个文件
                    //.addClasspathResource("diagrams/" + name + ".png") //从classpath的资源中加载，一次只能加载一个文件
                    .deploy();
        } catch (ActivitiIllegalArgumentException e) {
            logger.error("[部署失败] " + e.getMessage());
            return false;
        }
        logger.info("[部署成功] ID：" + deployment.getId() + ", 名称：" + deployment.getName());
        return true;
    }

    /**
     * @description 查询所有流程模板
     * @return
     */
    public List<ProcessDefinition> getAllProcessDefinition() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        List<ProcessDefinition> list = repositoryService//与流程定义和部署对象相关的Service
                .createProcessDefinitionQuery()//创建一个流程定义的查询
                .list();
        logger.info("[查询全部流程模板] 共 " + list.size() + " 条记录");
        return list;
    }

    /**
     * @description 根据deploymentID删除流程模板
     * @param deploymentID
     */
    public boolean deleteProcessDefinitionByDeploymentID(String deploymentID) {
        // 第二个参数开启级联删除，否则流程实例会自动删除，但任务不会
        // 在流程模板和流程实例被删除的情况下，任务无法完成，也无法删除
        try {
            ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
            RepositoryService repositoryService = processEngine.getRepositoryService();
            repositoryService.deleteDeployment(deploymentID, true);
        } catch (ActivitiObjectNotFoundException e) {
            logger.info("[删除流程模板失败] [deploymentID=" + deploymentID + "]");
            return false;
        }
        logger.info("[删除流程模板成功] [deploymentID=" + deploymentID + "]");
        return true;
    }
}
