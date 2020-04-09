package com.bootdo.modules.flowable.rest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bootdo.ext.util.IConstants;
import com.bootdo.modules.common.utils.Result;
import com.bootdo.modules.flowable.domain.DeModel;
import com.bootdo.modules.flowable.domain.ExtDatasourceDO;
import com.bootdo.modules.flowable.service.DeModelService;
import com.bootdo.modules.flowable.service.ExtDatasourceService;
import com.bootdo.modules.flowable.utils.TransUtil;
import org.flowable.bpmn.model.ext.ExtChildNode;
import org.flowable.bpmn.model.ext.ExtModelEditor;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/*
@RestController 标明此Controller提供RestAPI
@RequestMapping及其变体。映射http请求url到java方法
@RequestParam 映射请求参数到java方法的参数
@PageableDefault 指定分页参数默认值
@PathVariable 映射url片段到java方法的参数
在url声明中使用正则表达式
@JsonView控制json输出内容
*/
@RestController
@RequestMapping("/flowable")
public class FlowableRest {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private DeModelService deModelService;

    @Autowired
    private ExtDatasourceService extDatasourceService;

    private String getUserTokenIfExist(HttpServletRequest request) {
        String token = request.getHeader(IConstants.KEY_MAP_TOKEN);
        if (StringUtils.isEmpty(token)) {
            token = request.getHeader(IConstants.KEY_MAP_TOKEN_02);
        }
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        /*int index = token.indexOf(" ");
        if (-1 < index) {
            token = token.substring(index + " ".length());
        }*/

        return token;
    }

    @CrossOrigin
    @RequestMapping(value = "/start", method = RequestMethod.POST)
    //@RequestHeader(value="Authorization") String Authorization,
    public Result startFlow(@RequestBody String json, HttpServletRequest request) {
        String token = getUserTokenIfExist(request);
        if (StringUtils.isEmpty(token)) {
            return Result.error("获取token失败 !  401");
        }


        //获取流程名称
        JSONObject jsonObject = (JSONObject) JSON.parse(json);
        String flowName = jsonObject.getString("flowname");
        System.out.println("flowName=====" + flowName);

        //查询流程部署json
        DeModel b = deModelService.get(flowName);
        System.out.println(b.getModelEditerJson());
        ExtModelEditor editorModel = TransUtil.transExtModelEditor(b.getModelEditerJson());

        //前端传入的参数
        String paramString = jsonObject.getString("param");
        System.out.println("param=====" + paramString);
        HashMap<String, Object> map = JSON.parseObject(paramString, HashMap.class);
        map.put("flowName", flowName);
        map.put("model", editorModel);

        /**设置参数 start*/
        Map<String, String> flowResponse = new HashMap<>();
        map.put(IConstants.KEY_MAP_GLOBAL_FLOW_RESPONSE, flowResponse);
        map.put(IConstants.KEY_MAP_TOKEN, token);
        //map.put(IConstants.KEY_MAP_TOKEN_02, token);
        /**设置参数 end*/


        //流程参数构建
        //数据源
        String dataSource_Id = getDSFromModel(editorModel);
        if (dataSource_Id != null) {
            ExtDatasourceDO ds = extDatasourceService.get(dataSource_Id);
            map.put("dataSource", ds);
        }
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(flowName, map);

        Object response = map.get(IConstants.KEY_MAP_GLOBAL_FLOW_RESPONSE);
        if (null != response) {
            return Result.ok((Map<String, Object>) response);
        }

        return Result.ok("提交成功.流程Id为：" + processInstance.getId());


//		String[] res = jwts.split(".");
//
//		String header = jwts.substring(0, jwts.indexOf("."));
//		jwts = jwts.substring(jwts.indexOf(".") + 1);
//		String payload = jwts.substring(0, jwts.indexOf("."));
//		String sign = jwts.substring(jwts.indexOf(".") + 1);

//		System.out.println(new String(Base64.getDecoder().decode(header)));
//		System.out.println(new String(Base64.getDecoder().decode(payload)));
    }

    public static void main(String[] ss) {
        ProcessEngineConfiguration cfg = new StandaloneProcessEngineConfiguration()
                .setJdbcUrl("jdbc:mysql://localhost:3306/workflow?useUnicode=true&zeroDateTimeBehavior=convertToNull")
                .setJdbcUsername("root")
                .setJdbcPassword("123")
                .setJdbcDriver("com.mysql.jdbc.Driver")
                .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);

        ProcessEngine processEngine = cfg.buildProcessEngine();
        RuntimeService runtimeService = processEngine.getRuntimeService();

        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("name", "wangxing");

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("test", variables);
        System.out.println("提交成功.流程Id为：" + processInstance.getId());
    }

    /*从json中获取数据源*/
    public String getDSFromModel(ExtModelEditor editorModel) {
        String dataSource_Id = null;
        for (ExtChildNode tem : editorModel.getChildShapes()) {
            if (tem != null) {
                if (tem.getProperties() != null) {
                    if (tem.getProperties().getServicetaskdelegateexpression() != null) {
                        dataSource_Id = tem.getProperties().getServicetaskdelegateexpression();
                    }
                }
            }
        }
        return dataSource_Id;
    }

}
