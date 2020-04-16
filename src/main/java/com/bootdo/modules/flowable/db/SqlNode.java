package com.bootdo.modules.flowable.db;

import com.bootdo.ext.servicenode.BaseServiceNode;
import com.bootdo.ext.util.IConstants;
import com.bootdo.modules.common.utils.StringUtils;
import com.bootdo.modules.flowable.domain.ExtDatasourceDO;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.model.ext.ExtChildNode;
import org.flowable.bpmn.model.ext.ExtModelEditor;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class SqlNode extends BaseServiceNode implements JavaDelegate {


    @Override
    public void execute(DelegateExecution execution) {
        // TODO Auto-generated method stub
        Map<String, Object> map = execution.getVariables();

        String pid = execution.getId();
        String sid = execution.getCurrentFlowElement().getId();


        ExtModelEditor editorModel = (ExtModelEditor) map.get("model");
        String sql = getSql(editorModel, sid);


        Map<String, String> myVars = getMyVars(pid, execution);
        myVars.entrySet().stream().forEach(e -> {
            if (-1 < e.getKey().indexOf(IConstants.PROCESS_VAR_PARA_SEP)) {

            } else {
                execution.setVariable(null == sid ? "" : sid + IConstants.SEP_PROCESS_PID_NAME + e.getKey(),
                        null == e.getValue() ? "" : e.getValue());
            }
        });

        DBFactory dbf = new DBFactory();

        if (!StringUtils.isEmpty(sql)) {
            String[] sqls = sql.split("\\;");
            for (String strSQL : sqls) {
                if (StringUtils.isEmpty(strSQL.trim())) {
                    continue;
                }
                boolean bRet = dbf.execSql((ExtDatasourceDO) map.get("dataSource"), strSQL, map);
                Object objMap = execution.getVariable(IConstants.KEY_MAP_GLOBAL_FLOW_RESPONSE);
                if (null != objMap) {
                    Map<String, String> gMap = (Map<String, String>) objMap;
                    gMap.put(sid, bRet ? "true" : "false");
                    execution.setVariable(IConstants.KEY_MAP_GLOBAL_FLOW_RESPONSE, gMap);
                } else {
                    log.error(" get  KEY_MAP_GLOBAL_FLOW_RESPONSE   is null!!!");
                }
            }

        }

    }

    /*从json中获取sql*/
    public String getSql(ExtModelEditor editorModel, String sid) {
        String sql = null;
        for (ExtChildNode childShape : editorModel.getChildShapes()) {
            String id = childShape.getProperties().getOverrideid();
            if (!org.springframework.util.StringUtils.isEmpty(id)) {

                if (sid.equals(id)) {
                    sql = childShape.getProperties().getServicetaskexpression();
                    sql = sql.replace("\n", "");
                    return sql;
                }
            }
        }
        return sql;
    }

}
