package org.jeecg.modules.flow.modeler;//package org.jeecg.activiti.modeler;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;


import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.impl.persistence.entity.ModelEntityImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ModelQuery;
import org.apache.commons.lang3.StringUtils;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.flow.base.JeeThinkConfig;
import org.jeecg.modules.flow.domain.ModelerVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.*;
import java.util.List;

import static org.activiti.editor.constants.ModelDataJsonConstants.MODEL_DESCRIPTION;
import static org.activiti.editor.constants.ModelDataJsonConstants.MODEL_NAME;

@Slf4j
@RestController
public class ModelerController  {
    protected static final Logger LOGGER = LoggerFactory.getLogger(ModelEditorJsonRestResource.class);

    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private ObjectMapper objectMapper;


    @GetMapping("/modeler/list")
    @ResponseBody
    public Result list( @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                        @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,ModelEntityImpl modelEntity) {
        ModelQuery modelQuery = repositoryService.createModelQuery();
        modelQuery.orderByLastUpdateTime().desc();

        // 条件过滤
        if (StringUtils.isNotBlank(modelEntity.getKey())) {
            modelQuery.modelKey(modelEntity.getKey());
        }
        if (StringUtils.isNotBlank(modelEntity.getName())) {
            modelQuery.modelNameLike("%" + modelEntity.getName() + "%");
        }

        List<Model> resultList = modelQuery.listPage((pageNo - 1) * pageSize, pageSize);

        Page<Model> list = new Page<>();
        list.setRecords(resultList);

        list.setTotal(modelQuery.count());
        //list.setPageNum(pageNum);
        list.setCurrent(pageNo);
        //list.setPageSize(pageSize);
        list.setSize(pageSize);

        return Result.OK(list);
    }


    /**
     * 创建模型
     */
    @RequestMapping(value = "/modeler/create")
    @ResponseBody
    public Result<Object> create(@RequestBody ModelerVo modelerVo) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode editorNode = objectMapper.createObjectNode();
            editorNode.put("id", "canvas");
            editorNode.put("resourceId", "canvas");
            ObjectNode stencilSetNode = objectMapper.createObjectNode();
            stencilSetNode.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");
            editorNode.put("stencilset", stencilSetNode);

            ObjectNode modelObjectNode = objectMapper.createObjectNode();
            modelObjectNode.put(MODEL_NAME, modelerVo.getName());
            modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, 1);
            String description = StringUtils.defaultString(modelerVo.getDescription());
            modelObjectNode.put(MODEL_DESCRIPTION, description);

            Model newModel = repositoryService.newModel();
            newModel.setMetaInfo(modelObjectNode.toString());
            newModel.setName(modelerVo.getName());
            newModel.setKey(StringUtils.defaultString(modelerVo.getKey()));

            repositoryService.saveModel(newModel);
            repositoryService.addModelEditorSource(newModel.getId(), editorNode.toString().getBytes("utf-8"));

            return  Result.OK( "创建模型成功", newModel.getId());
        } catch (Exception e) {
            log.error("创建模型失败：", e);
            return Result.error("创建模型失败："+e.getMessage());
        }

    }

    /**
     * 根据Model部署流程
     */
    @RequestMapping(value = "/modeler/deploy/{modelId}")
    @ResponseBody
    public Result deploy(@PathVariable("modelId") String modelId, RedirectAttributes redirectAttributes) {
        try {
            Model modelData = repositoryService.getModel(modelId);
            ObjectNode modelNode = (ObjectNode) new ObjectMapper().readTree(repositoryService.getModelEditorSource(modelData.getId()));
            byte[] bpmnBytes = null;

            BpmnModel model = new BpmnJsonConverter().convertToBpmnModel(modelNode);
            bpmnBytes = new BpmnXMLConverter().convertToXML(model);

            String processName = modelData.getName() + ".bpmn20.xml";
            Deployment deployment = repositoryService.createDeployment().name(modelData.getName()).addString(processName, new String(bpmnBytes, "UTF-8")).deploy();
            LOGGER.info("部署成功，部署ID=" + deployment.getId());
            return Result.OK("部署成功");
        } catch (Exception e) {
            LOGGER.error("根据模型部署流程失败：modelId={}", modelId, e);
            return Result.error("部署失败"+e.getMessage());

        }

    }

    /**
     * 导出model的xml文件
     */
    @RequestMapping(value = "/modeler/export/{modelId}")
    @ResponseBody
    public Result export(@PathVariable("modelId") String modelId) {
        OutputStream out = null;
        try {
            Model modelData = repositoryService.getModel(modelId);
            BpmnJsonConverter jsonConverter = new BpmnJsonConverter();
            JsonNode editorNode = new ObjectMapper().readTree(repositoryService.getModelEditorSource(modelData.getId()));
            BpmnModel bpmnModel = jsonConverter.convertToBpmnModel(editorNode);

            // 流程非空判断
            if (!CollectionUtils.isEmpty(bpmnModel.getProcesses())) {
                BpmnXMLConverter xmlConverter = new BpmnXMLConverter();
                byte[] bpmnBytes = xmlConverter.convertToXML(bpmnModel);

                ByteArrayInputStream in = new ByteArrayInputStream(bpmnBytes);
                String filename = bpmnModel.getMainProcess().getId() + ".bpmn";

                File file  = new File(getAbsoluteFile(filename));
                if(file.exists()){
                    file.delete();
                }
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(bpmnBytes,0,bpmnBytes.length);
                fos.flush();
                fos.close();
                return Result.OK(filename);
            } else {
                return Result.error("操作失败！");
            }

        } catch (Exception e) {
            LOGGER.error("导出model的xml文件失败：modelId={}", modelId, e);
            return Result.error("导出model的xml文件失败：modelId="+ modelId);

        }
    }

    /**
     * 获取下载路径
     *
     * @param filename 文件名称
     */
    public String getAbsoluteFile(String filename)
    {
        String downloadPath = JeeThinkConfig.getDownloadPath() + filename;
        File desc = new File(downloadPath);
        if (!desc.getParentFile().exists())
        {
            desc.getParentFile().mkdirs();
        }
        return downloadPath;
    }


    @DeleteMapping("/modeler/remove/{ids}")
    @ResponseBody
    public Result remove(@PathVariable String ids) {
        try {
            repositoryService.deleteModel(ids);
            return Result.OK();
        }
        catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

}
