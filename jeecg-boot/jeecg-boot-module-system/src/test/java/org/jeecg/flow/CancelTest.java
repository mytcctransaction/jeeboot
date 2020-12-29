package org.jeecg.flow;

import org.jeecg.JeecgSystemApplication;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.jeecg.JeecgSystemApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = JeecgSystemApplication.class)
public class CancelTest {

	@Autowired
	private RepositoryService repositoryService;

	@Autowired
	private RuntimeService runtimeService;

	@Test
	public void cancel() {
		runtimeService.deleteProcessInstance("122505", "测试测试");
	}
}