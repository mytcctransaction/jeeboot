/*
 *
 *      Copyright (c) 2018-2099, qiqucode.com All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the fly2you.cn developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: qiqucode.com (qiqucode@foxmail.com)
 *
 */
package org.jeecg.modules.act.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Model;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.dto.OnlineAuthDTO;
import org.jeecg.modules.act.RestResponse;
import org.jeecg.modules.act.entity.ActForm;
import org.jeecg.modules.act.service.IActFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * ActServiceController
 *
 * @author  lzj
 */
@RestController
@RequestMapping("/form")
@Slf4j
public class ActDynamicFormController {
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private IActFormService actFormService;
    /**
     * get form
     */
    @RequestMapping("/get")
    public RestResponse get(@RequestParam("formKey") String formKey,HttpServletRequest request) {
        log.info(JSON.toJSONString(formKey));
        QueryWrapper<ActForm> queryWrapper = new QueryWrapper<ActForm>();
        queryWrapper.eq("code", formKey);
        ActForm form=actFormService.getOne(queryWrapper);
        return RestResponse.success().put("formdb", form);
    }



    /**
     * 保存 form
     *
     * @param params
     */
    @PostMapping("/save")
    public RestResponse saveModel(@RequestBody JSONObject params) {
        try {
            String formKey=params.getString("formKey");
            String formJson=params.getString("formJson");
            QueryWrapper<ActForm> queryWrapper = new QueryWrapper<ActForm>();
            queryWrapper.eq("code", formKey);
            ActForm form=actFormService.getOne(queryWrapper);
            form.setFormJson(formJson);
            actFormService.updateById(form);
            return RestResponse.success().put("params", form);
        } catch (Exception e) {
            throw new ActivitiException("Error saving model form !", e);
        }
    }
}
