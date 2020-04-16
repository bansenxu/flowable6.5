package com.bootdo.ext.servicenode;

import org.flowable.engine.delegate.DelegateExecution;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseServiceNode {

    protected Map<String, String> getMyVars(String pid, DelegateExecution execution) {
        Map<String, String> map = new HashMap<>();
        String docu = execution.getCurrentFlowElement().getDocumentation();
        if (!StringUtils.isEmpty(docu)) {
            String[] strAry = docu.trim().split("\\,");
            for (String s : strAry) {
                //此处界定各个节点的变量不重复
                map.put(s, null);
            }
        }
        return map;
    }

}
