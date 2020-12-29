package org.jeecg.modules.flow.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ProcessDefinitionQuery;

import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.flow.base.Constants;
import org.jeecg.modules.flow.base.FileUploadUtils;
import org.jeecg.modules.flow.base.JeeThinkConfig;
import org.jeecg.modules.flow.domain.ProcessDefinition;
import org.jeecg.modules.flow.service.ProcessDefinitionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/definition")
@Api(tags="流程定义")
public class ProcessDefinitionController {

    private static final Logger log = LoggerFactory.getLogger(ProcessDefinitionController.class);

    private String prefix = "definition";

    @Autowired
    private ProcessDefinitionService processDefinitionService;
    @Autowired
    private RepositoryService repositoryService;

    @ApiOperation("获取流程定义列表")
    @GetMapping("/list")
    @ResponseBody
    public Result<Object> list(@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                               @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,ProcessDefinition processDefinition) {
        Page list = processDefinitionService.listProcessDefinition(processDefinition,pageNo,pageSize);
        return Result.OK(list);
    }

    /**
     * 部署流程定义
     */

    @PostMapping("/upload")
    @ApiOperation("部署流程定义")
    @ResponseBody
    public Result upload(MultipartFile file) {
        try {
            if (!file.isEmpty()) {
                String extensionName = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.') + 1);
                if (!"bpmn".equalsIgnoreCase(extensionName)
                        && !"zip".equalsIgnoreCase(extensionName)
                        && !"bar".equalsIgnoreCase(extensionName)) {
                    return Result.error("流程定义文件仅支持 bpmn, zip 和 bar 格式！");
                }
                // p.s. 此时 FileUploadUtils.upload() 返回字符串 fileName 前缀为 Constants.RESOURCE_PREFIX，需剔除
//                 详见: FileUploadUtils.getPathFileName(...)
                String fileName = FileUploadUtils.upload(JeeThinkConfig.getProfile()+ "/processDefiniton", file);

                if (StringUtils.isNotBlank(fileName)) {
                    String realFilePath = JeeThinkConfig.getProfile()+ fileName.substring(Constants.RESOURCE_PREFIX.length());
                    processDefinitionService.deployProcessDefinition(realFilePath);
                    return Result.OK();
                }
            }
            return Result.error("不允许上传空文件！");
        }
        catch (Exception e) {
            log.error("上传流程定义文件失败！", e);
            return Result.error(e.getMessage());
        }
    }

    @ApiOperation("删除流程定义")
    @DeleteMapping("/remove/{ids}")
    @ResponseBody
    public Result remove(@PathVariable String ids) {
        try {
            return Result.OK(processDefinitionService.deleteProcessDeploymentByIds(ids));
        }
        catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

//    @Log(title = "流程定义", businessType = BusinessType.EXPORT)
//    @PostMapping("/export")
//    @ResponseBody
//    public AjaxResult export() {
//        List<ProcessDefinition> list = processDefinitionService.listProcessDefinition(new ProcessDefinition());
//        ExcelUtil<ProcessDefinition> util = new ExcelUtil<>(ProcessDefinition.class);
//        return util.exportExcel(list, "流程定义数据");
//    }
    @ApiOperation("suspendOrActiveApply")
    @PostMapping( "/suspendOrActiveApply")
    @ResponseBody
    public Result suspendOrActiveApply(@RequestBody ProcessDefinition processDefinition) {
        processDefinitionService.suspendOrActiveApply(processDefinition.getId(), processDefinition.getSuspendState());
        return Result.OK();
    }

    /**
     * 读取流程资源
     *
     * @param processDefinitionId 流程定义ID
     * @param resourceName        资源名称
     */
    @RequestMapping(value = "/readResource")
    public void readResource(@RequestParam("processDefinitionId") String processDefinitionId, @RequestParam("resourceName") String resourceName, HttpServletResponse response)
            throws Exception {
        ProcessDefinitionQuery pdq = repositoryService.createProcessDefinitionQuery();
        org.activiti.engine.repository.ProcessDefinition pd = pdq.processDefinitionId(processDefinitionId).singleResult();

        // 通过接口读取
        InputStream resourceAsStream = repositoryService.getResourceAsStream(pd.getDeploymentId(), resourceName);

        // 输出资源内容到相应对象
        byte[] b = new byte[1024];
        int len = -1;
        while ((len = resourceAsStream.read(b, 0, 1024)) != -1) {
            response.getOutputStream().write(b, 0, len);
        }
    }

    /**
     * 转换流程定义为模型
     * @param processDefinitionId
     * @return
     * @throws UnsupportedEncodingException
     * @throws XMLStreamException
     */
    @PostMapping(value = "/convert2Model")
    @ResponseBody
    public Result convertToModel(@RequestBody String processDefinitionId)
            throws UnsupportedEncodingException, XMLStreamException {
        org.activiti.engine.repository.ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(processDefinitionId).singleResult();

        InputStream bpmnStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(),
                processDefinition.getResourceName());
        XMLInputFactory xif = XMLInputFactory.newInstance();
        InputStreamReader in = new InputStreamReader(bpmnStream, "UTF-8");
        XMLStreamReader xtr = xif.createXMLStreamReader(in);
        BpmnModel bpmnModel = new BpmnXMLConverter().convertToBpmnModel(xtr);

        BpmnJsonConverter converter = new BpmnJsonConverter();
        ObjectNode modelNode = converter.convertToJson(bpmnModel);
        Model modelData = repositoryService.newModel();
        modelData.setKey(processDefinition.getKey());
        modelData.setName(processDefinition.getResourceName());
        modelData.setCategory(processDefinition.getDeploymentId());

        ObjectNode modelObjectNode = new ObjectMapper().createObjectNode();
        modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, processDefinition.getName());
        modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, 1);
        modelObjectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, processDefinition.getDescription());
        modelData.setMetaInfo(modelObjectNode.toString());

        repositoryService.saveModel(modelData);

        repositoryService.addModelEditorSource(modelData.getId(), modelNode.toString().getBytes("utf-8"));

        return Result.OK();
    }

}
