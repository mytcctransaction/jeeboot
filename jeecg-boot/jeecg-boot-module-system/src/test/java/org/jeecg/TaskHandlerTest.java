package org.jeecg;

import org.activiti.engine.task.Task;
import org.jeecg.modules.flow.base.TaskHandler;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class TaskHandlerTest {

    private Logger logger = LoggerFactory.getLogger(TaskHandlerTest.class);
    private TaskHandler ts = new TaskHandler();

    @Test
    public void getAllTask() {
        List<Task> list = ts.getAllTask();
        Assert.assertNotNull(list);
        printTaskList(list);
    }

    @Test
    public void getTaskByUserID() {
        List<Task> list = ts.getTaskByUserID("smallyu");
        Assert.assertNotNull(list);
        printTaskList(list);
    }

    @Test
    public void taskAssignee() {
        Assert.assertFalse(ts.taskAssignee("aabbcc", "aabbcc"));
        Assert.assertTrue(ts.taskAssignee("2505", "smallyu"));
    }

    @Test
    public void completeTaskByTaskID(){
//        Assert.assertFalse(ts.completeTaskByTaskID("6666"));
        Assert.assertTrue(ts.completeTaskByTaskID("25005"));
    }

    @Test
    public void deleteTaskByTaskID(){
        Assert.assertFalse(ts.deleteTaskByTaskID("25005"));
    }

    private void printTaskList(List<Task> list) {
        for(Task task : list){
            logger.info("------------------------");
            logger.info("Task ID: " + task.getId());
            logger.info("Task Name: " + task.getName());
            logger.info("Task CreateTime: " + task.getCreateTime());
            logger.info("Task Assignee: " + task.getAssignee());
            logger.info("ProcessInstance ID: " + task.getProcessInstanceId());
            logger.info("Excution ID: " + task.getExecutionId());
            logger.info("ProcessDefinitionHandler ID: " + task.getProcessDefinitionId());
        }
    }
}