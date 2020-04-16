package org.flowable.http.bpmn.impl;

import com.bootdo.ext.util.IConstants;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.model.FlowableHttpRequestHandler;
import org.flowable.bpmn.model.FlowableHttpResponseHandler;
import org.flowable.bpmn.model.ext.ExtChildNode;
import org.flowable.bpmn.model.ext.ExtModelEditor;
import org.flowable.bpmn.model.ext.ExtProperty;
import org.flowable.common.engine.api.delegate.Expression;
import org.flowable.common.engine.api.variable.VariableContainer;
import org.flowable.engine.delegate.DelegateExecution;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Slf4j
public class RealHttpTask extends HttpActivityBehaviorImpl {
    private static final long serialVersionUID = 1L;

    public static String getStringFromField(Expression expression, VariableContainer execution) {
        if (expression != null) {
            Object value = expression.getValue(execution);
            if (value != null) {
                return value.toString();
            }
        }

        return null;
    }

    public static int getIntFromField(Expression expression, VariableContainer execution) {
        if (expression != null) {
            Object value = expression.getValue(execution);
            if (value != null) {
                return Integer.parseInt(value.toString());
            }
        }

        return 0;
    }

    public static boolean getBooleanFromField(Expression expression, VariableContainer execution) {
        if (expression != null) {
            Object value = expression.getValue(execution);
            return parseBoolean(value);
        } else {
            return false;
        }
    }

    protected static boolean parseBoolean(Object value) {
        if (value != null) {
            if (value instanceof String) {
                String stringValue = (String) value;
                if (!stringValue.equalsIgnoreCase("true") && !stringValue.equalsIgnoreCase("false")) {
                    throw new RuntimeException("String value \"" + value + "\" is not alloved in boolean expression");
                } else {
                    return Boolean.parseBoolean(value.toString());
                }
            } else if (value instanceof Boolean) {
                return (Boolean) value;
            } else {
                throw new RuntimeException("Value \"" + value + "\" can not be converted into boolean");
            }
        } else {
            return false;
        }
    }

//    private Map<String, String> getMyVars(String pid, ExtModelEditor editorModel) {
//        Map<String, String> map = new HashMap<>();
//        for (ExtChildNode childShape : editorModel.getChildShapes()) {
//            String docu = childShape.getProperties().getDocumentation();
//            if (!StringUtils.isEmpty(docu)) {
//                String[] strAry = docu.trim().split("\\,");
//                for (String s : strAry) {
//                    //此处界定各个节点的变量不重复
//                    map.put(s, null);
//                }
//            }
//        }
//
//        return map;
//    }

    private Map<String, String> getMyVars(String pid, DelegateExecution execution) {
        Map<String, String> map = new HashMap<>();
        String docu = execution.getCurrentFlowElement().getDocumentation();
        if (!StringUtils.isEmpty(docu)) {
            String[] strAry = docu.trim().split("\\,");
            for (String s : strAry) {
                //此处界定各个节点的变量不重复
                if (-1 < s.indexOf(IConstants.PROCESS_VAR_PARA_SEP)) {

                } else {
                    //只保留定义变量
                    map.put(s, null);
                }

            }
        }
        return map;
    }

    private String getMethod(String sid, ExtModelEditor editorModel) {
        Map<String, String> map = new HashMap<>();
        for (ExtChildNode childShape : editorModel.getChildShapes()) {
            String docu = childShape.getResourceId();
            if (!StringUtils.isEmpty(docu)) {
                if (sid.equals(docu)) {
                    return childShape.getProperties().getHttptaskrequestmethod();
                }
            }
        }
        return null;
    }

    private String getHttpBody(String sid, ExtModelEditor editorModel) {
        Map<String, String> map = new HashMap<>();
        for (ExtChildNode childShape : editorModel.getChildShapes()) {
            String stencil = childShape.getStencil();
            if (!StringUtils.isEmpty(stencil)) {
                stencil = stencil.replace("id", "").replace(":", "").replace("{", "")
                        .replace("}", "").replace("\"", "");
                if (sid.equals(stencil)) {
                    return childShape.getProperties().getHttptaskrequestbody();
                }
            }
        }
        return null;
    }

    public String appendToken(String url, String token) {
        StringBuffer sb = new StringBuffer(url);
        if (-1 < sb.indexOf("?")) {
            sb.append("access_token=").append(token);
        } else {
            sb.append("?access_token=").append(token);
        }
        return sb.toString();
    }

    private String getUrl(String sid, ExtModelEditor editorModel) {
        Map<String, String> map = new HashMap<>();
        for (ExtChildNode childShape : editorModel.getChildShapes()) {
            String docu = childShape.getResourceId();
            if (!StringUtils.isEmpty(docu)) {
                if (sid.equals(docu)) {
                    return childShape.getProperties().getHttptaskrequesturl();
                }
            }
        }
        return null;
    }

    private boolean setUrl(String sid, ExtModelEditor editorModel, String token) {
        Map<String, String> map = new HashMap<>();
        for (ExtChildNode childShape : editorModel.getChildShapes()) {
            String docu = childShape.getResourceId();
            if (!StringUtils.isEmpty(docu)) {
                if (sid.equals(docu)) {
                    String url = appendToken(childShape.getProperties().getHttptaskrequesturl(), token);
                    childShape.getProperties().setHttptaskrequesturl(url);
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public void execute(DelegateExecution execution) {
        String token = null;
        Object obj = execution.getVariable(IConstants.KEY_MAP_TOKEN);
        if (null != obj) {
            token = (String) obj;
        } else {
            //TODO 错误处理
        }

        Map<String, Object> map_para = execution.getVariables();
        ExtModelEditor editorModel = (ExtModelEditor) map_para.get("model");

        final String pid = execution.getId();
        final String sid = execution.getCurrentFlowElement().getId();


        FlowableHttpRequestHandler oriHttpRequstHandler = httpServiceTask.getHttpRequestHandler();
        FlowableHttpResponseHandler oriHttpResponseHandler = httpServiceTask.getHttpResponseHandler();

        SimpleHttpRequestHandler simpleHttpRequestHandler = new SimpleHttpRequestHandler();
        httpServiceTask.setHttpRequestHandler(simpleHttpRequestHandler);

        SimpleHttpResponseHandler simpleHttpResponseHandler = new SimpleHttpResponseHandler();
        httpServiceTask.setHttpResponseHandler(simpleHttpResponseHandler);

        Map<String, String> myVars = getMyVars(pid, execution);
        myVars.entrySet().stream().forEach(e -> {
            if (-1 < e.getKey().indexOf(IConstants.PROCESS_VAR_PARA_SEP)) {

            } else {
                execution.setVariable(null == sid ? "" : sid + IConstants.SEP_PROCESS_PID_NAME + e.getKey(),
                        null == e.getValue() ? "" : e.getValue());
            }
        });

        map_para = execution.getVariables();
        super.execute(execution);


        //恢复现场
        httpServiceTask.setHttpRequestHandler(oriHttpRequstHandler);
        httpServiceTask.setHttpResponseHandler(oriHttpResponseHandler);
        leave(execution);
    }

    public void executeOld(DelegateExecution execution) {
//        Map<String, Object> map_para = execution.getVariables();
//        ExtModelEditor editorModel = (ExtModelEditor) map_para.get("model");
//        HttpRequest request = new HttpRequest();
//
//        try {
//            ExtProperty extProperty = this.getExtProperty(editorModel, execution.getCurrentActivityId());
//            request.setMethod(extProperty.getHttptaskrequestmethod());
//            request.setUrl(VarReplaceUtil.replace_var(extProperty.getHttptaskrequesturl(), map_para));
//            request.setHeaders(VarReplaceUtil.replace_var(extProperty.getHttptaskrequestheaders(), map_para));
//            request.setBody(VarReplaceUtil.replace_var(extProperty.getHttptaskrequestbody(), map_para));
//            request.setBodyEncoding(extProperty.getHttptaskrequestbodyencoding());
//            request.setSaveResponse(true);
//            request.setTimeout(getIntFromField(this.requestTimeout, execution));
//            request.setNoRedirects(getBooleanFromField(this.disallowRedirects, execution));
//            request.setIgnoreErrors(getBooleanFromField(this.ignoreException, execution));
//            request.setSaveRequest(getBooleanFromField(this.saveRequestVariables, execution));
//            request.setSaveResponseTransient(getBooleanFromField(this.saveResponseParametersTransient, execution));
//            request.setSaveResponseAsJson(getBooleanFromField(this.saveResponseVariableAsJson, execution));
//            request.setPrefix(getStringFromField(this.resultVariablePrefix, execution));
//            String failCodes = getStringFromField(this.failStatusCodes, execution);
//            String handleCodes = getStringFromField(this.handleStatusCodes, execution);
//            if (failCodes != null) {
//                request.setFailCodes(ExpressionUtils.getStringSetFromField(failCodes));
//            }
//
//            if (handleCodes != null) {
//                request.setHandleCodes(ExpressionUtils.getStringSetFromField(handleCodes));
//            }
//
//            if (request.getPrefix() == null) {
//                request.setPrefix(execution.getCurrentFlowElement().getId());
//            }
//
//            if (request.isSaveRequest()) {
//                execution.setVariable(request.getPrefix() + "RequestMethod", request.getMethod());
//                execution.setVariable(request.getPrefix() + "RequestUrl", request.getUrl());
//                execution.setVariable(request.getPrefix() + "RequestHeaders", request.getHeaders());
//                execution.setVariable(request.getPrefix() + "RequestBody", request.getBody());
//                execution.setVariable(request.getPrefix() + "RequestBodyEncoding", request.getBodyEncoding());
//                execution.setVariable(request.getPrefix() + "RequestTimeout", request.getTimeout());
//                execution.setVariable(request.getPrefix() + "DisallowRedirects", request.isNoRedirects());
//                execution.setVariable(request.getPrefix() + "FailStatusCodes", failCodes);
//                execution.setVariable(request.getPrefix() + "HandleStatusCodes", handleCodes);
//                execution.setVariable(request.getPrefix() + "IgnoreException", request.isIgnoreErrors());
//                execution.setVariable(request.getPrefix() + "SaveRequestVariables", request.isSaveRequest());
//                execution.setVariable(request.getPrefix() + "SaveResponseParameters", request.isSaveResponse());
//            }
//        } catch (Exception var8) {
//            if (var8 instanceof FlowableException) {
//                throw (FlowableException) var8;
//            }
//
//            throw new FlowableException("request fields are invalid in execution " + execution.getId(), var8);
//        }
//
//        this.httpActivityExecutor.validate(request);
//        ProcessEngineConfigurationImpl processEngineConfiguration = CommandContextUtil.getProcessEngineConfiguration();
//        HttpClientConfig httpClientConfig = CommandContextUtil.getProcessEngineConfiguration().getHttpClientConfig();
//        this.httpActivityExecutor.execute(request, execution, execution.getId(),
//                this.createHttpRequestHandler(this.httpServiceTask.getHttpRequestHandler(),
//                        processEngineConfiguration), this.createHttpResponseHandler(this.httpServiceTask.getHttpResponseHandler(),
//                        processEngineConfiguration), getStringFromField(this.responseVariableName, execution),
//                this.mapExceptions, httpClientConfig.getSocketTimeout(),
//                httpClientConfig.getConnectTimeout(),
//                httpClientConfig.getConnectionRequestTimeout());
//
//        System.out.println("this is my http task............" + execution.getVariable(request.getPrefix() + "ResponseStatusCode"));
//        System.out.println("this is my http task............" + execution.getVariable(request.getPrefix() + "ResponseReason"));
//        System.out.println("this is my http task............" + execution.getVariable(request.getPrefix() + "ResponseBody"));
//        this.leave(execution);
    }

    public ExtProperty getExtProperty(ExtModelEditor editorModel, String activiId) {
        ExtProperty result = null;
        Iterator var4 = editorModel.getChildShapes().iterator();

        while (var4.hasNext()) {
            ExtChildNode tem = (ExtChildNode) var4.next();
            if (tem != null && tem.getProperties().getOverrideid().equals(activiId) && tem.getProperties() != null) {
                result = tem.getProperties();
            }
        }

        return result;
    }

    public static void main(String[] args) {
        Map<String, String> para = new HashMap();
        para.put("cont", "String");
        para.put("type", "gbk");
        System.out.println(para.toString());
    }
}

