package com.bootdo.modules.flowable.shell;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;
import com.bootdo.ext.servicenode.BaseServiceNode;
import com.bootdo.ext.util.IConstants;
import com.bootdo.modules.flowable.domain.ExtDatasourceDO;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.model.ext.ExtChildNode;
import org.flowable.bpmn.model.ext.ExtModelEditor;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Map;

@Slf4j
@Component
public class ShellNode extends BaseServiceNode implements JavaDelegate {

    private static String DEFAULTCHART = "UTF-8";

    @Override
    public void execute(DelegateExecution execution) {


        // TODO Auto-generated method stub
        Map<String, Object> map = execution.getVariables();

        ExtModelEditor editorModel = (ExtModelEditor) map.get("model");
        String pid = execution.getId();
        String sid = execution.getCurrentFlowElement().getId();


        Map<String, String> myVars = getMyVars(pid, execution);
        myVars.entrySet().stream().forEach(e -> {
            if (-1 < e.getKey().indexOf(IConstants.PROCESS_VAR_PARA_SEP)) {

            } else {
                execution.setVariable(null == sid ? "" : sid + IConstants.SEP_PROCESS_PID_NAME + e.getKey(),
                        null == e.getValue() ? "" : e.getValue());
            }
        });
        String[] commond = getCommond(editorModel, sid);

        ExtDatasourceDO ds = (ExtDatasourceDO) map.get("dataSource");

        boolean bRet = false;
        try {
            bRet = executeCommond(ds, commond,map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Object objMap = execution.getVariable(IConstants.KEY_MAP_GLOBAL_FLOW_RESPONSE);
        if (null != objMap) {
            Map<String, String> gMap = (Map<String, String>) objMap;
            gMap.put(sid, bRet ? "true" : "false");
            execution.setVariable(IConstants.KEY_MAP_GLOBAL_FLOW_RESPONSE, gMap);
        } else {
            log.error(" get  KEY_MAP_GLOBAL_FLOW_RESPONSE   is null!!!");
        }

    }


	public String build_CMD(String sql, Map<String, Object> param) {
		for (String key : param.keySet()) {
			try {
				if (-1 < sql.indexOf("${" + key + "}")) {
					Object obj = param.get(key);
					String tmp = "";
					if (null != obj) {
						tmp += obj;
						sql = sql.replace("${" + key + "}", tmp);
					}

				}
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}

		}
		log.info("replace var commond::" + sql);
		return sql;
	}

    public boolean executeCommond(ExtDatasourceDO ds, String[] commonds,Map<String, Object> map) throws Exception {
        boolean bl = false;
        if (ds == null) {
            throw new Exception("目标服务器未获取成功");
        }

        /*构建目标服务器连接*/
        String ip = ds.getUrl();
        String username = ds.getUsername();
        String password = ds.getUserpwd();
        String cmd = "uname -a";
        Connection connection = login(ip, username, password);

        for (String commond : commonds) {

			String strCmd = build_CMD(commond,map);
            String execmd = execmd(connection, strCmd);
//            System.out.println(execmd);
        }
        bl = true;
        return bl;
    }

    /* 从json中获取 shell命令 */
    public String[] getCommond(ExtModelEditor editorModel, String sid) {
        String sql = null;
        for (ExtChildNode childShape : editorModel.getChildShapes()) {
            String id = childShape.getProperties().getOverrideid();
            if (!org.springframework.util.StringUtils.isEmpty(id)) {

                if (sid.equals(id)) {
                    sql = childShape.getProperties().getServicetaskexpression();
                    sql = sql.replace("\n", "");

                    break;
                }
            }
        }

        if (!org.springframework.util.StringUtils.isEmpty(sql)) {
            String[] commonds = sql.split("\n");
            return commonds;
        } else {
            return new String[]{"echo \"no command\" "};
        }
		/*
		String sql = null;
		for (ExtChildNode tem : editorModel.getChildShapes()) {
			if (tem != null) {
				if (tem.getProperties() != null) {
					if (tem.getProperties().getServicetaskexpression() != null) {
						sql = tem.getProperties().getServicetaskexpression();
					}
				}
			}
		}
		String[] commonds = sql.split("\n");
		return commonds;*/
    }

    private static Connection login(String ip, String username, String password) {
        boolean flag = false;
        Connection connection = null;
        try {
            connection = new Connection(ip);
            connection.connect();// 连接
            flag = connection.authenticateWithPassword(username, password);// 认证
            if (flag) {
                System.out.println("================登录成功==================");
                return connection;
            }
        } catch (IOException e) {
            System.out.println("=========登录失败=========" + e);
            connection.close();
        }
        return connection;
    }

    /**
     * * 远程执行shll脚本或者命令 *  * @param cmd *            即将执行的命令 * @return
     * 命令执行完后返回的结果值
     */

    private static String execmd(Connection connection, String cmd) {
        String result = "";
        try {
            if (connection != null) {
                Session session = connection.openSession();// 打开一个会话
                session.execCommand(cmd);// 执行命令
                result = processStdout(session.getStdout(), DEFAULTCHART);

                //System.out.println(result);




				// 如果为得到标准输出为空，说明脚本执行出错了
                /*
                 * if (StringUtils.isBlank(result)) {
                 * System.out.println("得到标准输出为空,链接conn:" + connection +
                 * ",执行的命令：" + cmd); result = processStdout(session.getStderr(),
                 * DEFAULTCHART); } else { System.out.println("执行命令成功,链接conn:" +
                 * connection + ",执行的命令：" + cmd); }
                 */
                connection.close();
                session.close();
            }
        } catch (IOException e) {
            System.out.println("执行命令失败,链接conn:" + connection + ",执行的命令：" + cmd + "  " + e);
            e.printStackTrace();
        }
        return result;

    }

    /**
     * * 解析脚本执行返回的结果集 * 
     * * @param in *            
     * 输入流对象 * @param charset *      
     *  编码 * @return 以纯文本的格式返回
     */

    private static String processStdout(InputStream in, String charset) {
        InputStream stdout = new StreamGobbler(in);
        StringBuffer buffer = new StringBuffer();
        ;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(stdout, charset));
            String line = null;
            while ((line = br.readLine()) != null) {
                buffer.append(line + "\n");
                System.out.println(line);
            }
            br.close();
        } catch (UnsupportedEncodingException e) {
            System.out.println("解析脚本出错：" + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("解析脚本出错：" + e.getMessage());
            e.printStackTrace();
        }
        return buffer.toString();
    }

}
