package com.bootdo.modules.flowable.rest;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

public class STaskTest implements JavaDelegate{

	@Override
	public void execute(DelegateExecution execution) {
		// TODO Auto-generated method stub
		 System.out.println("hello "+execution.getVariable("name") +" .");
	}

}
