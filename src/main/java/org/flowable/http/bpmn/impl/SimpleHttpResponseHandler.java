/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.flowable.http.bpmn.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bootdo.ext.util.IConstants;
import com.bootdo.modules.common.utils.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.model.FlowableHttpResponseHandler;
import org.flowable.common.engine.api.variable.VariableContainer;
import org.flowable.engine.impl.persistence.entity.ExecutionEntityImpl;
import org.flowable.http.HttpResponse;
import org.flowable.http.delegate.HttpResponseHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 249875211@qq.com
 */
@Slf4j
public class SimpleHttpResponseHandler extends FlowableHttpResponseHandler implements HttpResponseHandler {

    private static final long serialVersionUID = 1L;
    protected ObjectMapper objectMapper = new ObjectMapper();

    public SimpleHttpResponseHandler() {
        super.setImplementationType(org.flowable.bpmn.model.ImplementationType.IMPLEMENTATION_TYPE_CLASS);
        super.setImplementation(getClass().getName());
    }

    public String genVarsKey(String flowid, String key) {
        return new StringBuffer(flowid).append(IConstants.SEP_PROCESS_PID_NAME).append(key).toString();
    }

    /**
     * 1. 获取状态吗
     * 2. 设置变量值
     *
     * @param execution
     * @param httpResponse
     */
    @Override
    public void handleHttpResponse(VariableContainer execution, HttpResponse httpResponse) {
        try {
            //1.结果状态码
            //2.返回体
            //3.赋值变量
            ExecutionEntityImpl execDetail = (ExecutionEntityImpl) execution;
            String flowId = execDetail.getCurrentFlowElement().getId();

            Map<String, Object> testMap = execDetail.getVariables();

            execution.setVariable(genVarsKey(flowId, IConstants.KEY_MAP_STATUS_CODE_HTTP), String.valueOf(httpResponse.getStatusCode()));
            execution.setVariable(genVarsKey(flowId, IConstants.KEY_MAP_STATUS_REASON_HTTP), httpResponse.getReason());


            Object objMap = execution.getVariable(IConstants.KEY_MAP_GLOBAL_FLOW_RESPONSE);
            if (null != objMap) {
                Map<String, String> gMap = (Map<String, String>) objMap;
                gMap.put(flowId, httpResponse.getBody());
                execution.setVariable(IConstants.KEY_MAP_GLOBAL_FLOW_RESPONSE, gMap);
            } else {
                log.error(" get  KEY_MAP_GLOBAL_FLOW_RESPONSE   is null!!!");
            }

            if (null != httpResponse && 200 == httpResponse.getStatusCode()) {
                try {
                    String httpBody = httpResponse.getBody();
                    if (StringUtils.isEmpty(httpBody)) {
                        log.error("!!!!!!!!http body is empty !!!!");
                    } else {
                        JSONObject jsonObj = JSONObject.parseObject(httpBody);
                        Map<String, Object> tmpMap = new HashMap<>(1);
                        for (String key : execDetail.getVariables().keySet()) {
                            String tmpStr = flowId + IConstants.SEP_PROCESS_PID_NAME;
                            String tmpKey = key;
                            int index = 0;
                            if (-1 < (index = key.indexOf(tmpStr))) {
                                tmpKey = key.substring(tmpStr.length());
                                getAllKey(jsonObj, tmpMap, tmpKey);
                                if (!tmpMap.isEmpty()) {
                                    execution.setVariable(key, tmpMap.get(tmpKey));
                                }
                                tmpMap.clear();
                            }

                        }
                    }
                    /*Map<String, Object> jsonMap = JSONObject.parseObject(httpResponse.getBody());
                    if (null != jsonMap && !jsonMap.isEmpty()) {
                        for (String key : execDetail.getVariables().keySet()) {
                            int index = 0;
                            String tmpStr = flowId+IConstants.SEP_PROCESS_PID_NAME;
                            if (-1 < (index=key.indexOf(tmpStr))) {
                                Object tmp =jsonMap.get(key.substring(tmpStr.length()));
                                if(null!=tmp)
                                {
                                    execution.setVariable(key,tmp);
                                }
                            }
                        }
                    }*/
                } catch (Exception ex) {
                    ex.printStackTrace();
                    //log.error("!!!!!!!!http body is not Json!!!!");
                }
            } else {
                if (null != objMap) {
                    Map<String, String> gMap = (Map<String, String>) objMap;
                    gMap.put(flowId, httpResponse.getReason());
                    execution.setVariable(IConstants.KEY_MAP_GLOBAL_FLOW_RESPONSE, gMap);
                } else {
                    log.error(" get  KEY_MAP_GLOBAL_FLOW_RESPONSE   is null!!!");
                }
            }


//            execution.setVariable("firstName", responseNode.get("name").get("firstName").asText());
//            execution.setVariable("lastName", responseNode.get("name").get("lastName").asText());


            testMap = execDetail.getVariables();
            httpResponse.setBodyResponseHandled(true);

        } catch (Exception e) {
            // test handler
        }
    }

    public void getAllKey(JSONArray jsonAry, Map<String, Object> map, String myKey) {
        if (null != jsonAry) {
            for (Object json : jsonAry) {
                if (json instanceof JSONObject) {
                    JSONObject innerObject = (JSONObject) json;
                    if (innerObject.containsKey(myKey)) {
                        map.put(myKey, innerObject.get(myKey));
                    } else {
                        getAllKey(innerObject, map, myKey);
                    }
                } else if (json instanceof JSONArray) {
                    JSONArray innerObject = (JSONArray) json;
                    getAllKey(innerObject, map, myKey);
                }
            }
        }
    }


    public void getAllKey(JSONObject jsonObj, Map<String, Object> map, String myKey) {
        for (String key : jsonObj.keySet()) {
            if (myKey.equals(key)) {
                map.put(myKey, jsonObj.get(key));
                break;
            }
            if (jsonObj.get(key) instanceof JSONObject) {
                {
                    if (jsonObj.get(key) instanceof JSONObject) {
                        JSONObject innerObject = (JSONObject) jsonObj.get(key);
                        if (myKey.equals(key)) {
                            map.put(myKey, innerObject);
                            break;
                        }
                        getAllKey(innerObject, map, myKey);
                    } else if (jsonObj.get(key) instanceof JSONArray) {
                        JSONArray innerObject = (JSONArray) jsonObj.get(key);
                        getAllKey(innerObject, map, myKey);
                    }
                }
            }
        }
    }


    public static void main(String[] args) {


        String json = "{\"id\":\"6c52e576-3f26-4434-bd96-aadfd7afac02\",\"createdTimestamp\":1566267992436,\"username\":\"admin\",\"enabled\":true,\n" +
                "\"totp\":false,\"emailVerified\":false,\"firstName\":\"\",\"lastName\":\"\",\"attributes\":{\"skin\":[\"red\",\"blue\",\"green\"],\"locale\":[\"zh-CN\"]},\"disableableCredentialTypes\":[],\"requiredActions\":[\"VERIFY_EMAIL\"],\"notBefore\":1579157933,\"access\":{\"manageGroupMembership\":true,\"view\":true,\"mapRoles\":true,\"impersonate\":true,\"manage\":true}}";


        json = "{\"id\":\"6c52e576-3f26-4434-bd96-aadfd7afac02\",\"attributes\":{\"skin\":[\"red\",\"blue\",\"green\"],\"locale\":[\"zh-CN\"]}}";
        try {
            JSONObject jsonObj = JSONObject.parseObject(json);

            String mykey = "skin";
            Map<String, Object> map = new HashMap<>();
            new SimpleHttpResponseHandler().getAllKey(jsonObj, map, mykey);
            if (null != map.get(mykey)) {
                System.out.println(map.get(mykey));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
