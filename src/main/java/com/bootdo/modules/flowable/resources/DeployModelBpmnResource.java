package com.bootdo.modules.flowable.resources;

import org.flowable.bpmn.model.BpmnModel;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.flowable.engine.repository.Deployment;
import org.flowable.ui.common.service.exception.BadRequestException;
import org.flowable.ui.modeler.domain.Model;
import org.flowable.ui.modeler.rest.app.AbstractModelBpmnResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import javax.servlet.http.HttpServletResponse;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.List;

@RestController
@RequestMapping("/deploy")
public class DeployModelBpmnResource extends AbstractModelBpmnResource {

    @Autowired
    private RepositoryService repositoryService;


    /**
     * http://127.0.0.1:8001/delpoy/rest/models/3eabbfe0-c390-11e8-a370-0242ac110005/bpmn20
     * GET /rest/models/{modelId}/bpmn20 -> Get BPMN 2.0 xml
     */
    @RequestMapping(value = "/rest/models/{processModelId}/bpmn20", method = RequestMethod.GET)
    public void getProcessModelBpmn20Xml(HttpServletResponse response, @PathVariable String processModelId) throws IOException {
        //super.getProcessModelBpmn20Xml(response, processModelId);
        getProcessModelBpmn20XmlToModel(response,processModelId);
    }

    public void getProcessModelBpmn20XmlToModel(HttpServletResponse response, String processModelId) throws IOException {

        if (processModelId == null) {
            throw new BadRequestException("No process model id provided");
        }

        Model model = modelService.getModel(processModelId);

        BpmnModel bpmnModel = modelService.getBpmnModel(model);
        byte[] xmlBytes = modelService.getBpmnXML(bpmnModel);
        //System.out.println(new String(xmlBytes,"utf-8"));
        
        xmlBytes = xmlFactory(xmlBytes).getBytes();
        
        Deployment deployment= repositoryService.createDeployment().addBytes("wangxing.bpmn20.xml",xmlBytes).deploy();
    }
    
    //文件存储xml
    public void fileFactory(byte[] xmlBytes)
    {
    	//写文件
        String filePath = this.getClass().getClassLoader().getResource("").toString()+"wangxing.bpmn20.xml";
        if(filePath!=null)
        {
        	if(filePath.substring(0, 4).equals("file"))
        	{
        		filePath = filePath.substring(6);
        	}
        }
        File file = new File(filePath);
        if(file.exists())
        {
        	file.delete();
        }
        
		try {
			OutputStream out = new FileOutputStream(filePath);
			out.write(xmlBytes);
		    out.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
        if(file.exists())
        {
        	System.out.println("file is ok");
        }
        ProcessEngineConfiguration cfg = new StandaloneProcessEngineConfiguration()
                .setJdbcUrl("jdbc:mysql://localhost:3306/workflow?useUnicode=true&zeroDateTimeBehavior=convertToNull")
                .setJdbcUsername("root")
                .setJdbcPassword("123")
                .setJdbcDriver("com.mysql.jdbc.Driver")
                .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);

    	ProcessEngine processEngine = cfg.buildProcessEngine();
    	RuntimeService runtimeService = processEngine.getRuntimeService();

    	RepositoryService repositoryService = processEngine.getRepositoryService();
    	Deployment deployment = repositoryService.createDeployment()
    	  .addClasspathResource("wangxing.bpmn20.xml")
    	  .deploy();
    }
    
    //构建所需的xml
    public String xmlFactory(byte[] xmlBytes)
    {
    	String rs = "";
    	InputStream input = new ByteArrayInputStream(xmlBytes);
        SAXReader saxReader = new SAXReader();
        try {
			Document document = saxReader.read(input);
			Element root = document.getRootElement();
			//System.out.println(root.getName());
			//获取proccess节点
			List list = root.elements("process");
			if(list!=null)
			{
				Iterator it = list.iterator();
			    while(it.hasNext()){
			     Element e = (Element)it.next();
			     System.out.println(e.getName());
			     //获取serviceTask 节点
			     List serviceTask_list = e.elements("serviceTask");
			     Iterator it2 = serviceTask_list.iterator();
				    while(it2.hasNext()){
				     Element serviceTask = (Element)it2.next();
				    // System.out.println(serviceTask.getName());
				    // System.out.println(serviceTask.attribute("name").getStringValue());
				     Attribute flowType = serviceTask.attribute("type");
				     
				     if(flowType!=null && "http".equals(flowType.getStringValue()))
				     {
				    	 //该节点新增自定义http解析类型
				    	 Element tem = serviceTask.element("extensionElements");
				    	 tem.addElement("flowable:field").addAttribute("name", "httpActivityBehaviorClass")
				    	 .addElement("flowable:string").addCDATA("org.flowable.http.bpmn.impl.RealHttpTask");
				     }
			    }

			}
			}
			rs =  root.asXML();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return rs;
    }
}
