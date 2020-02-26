package com.bootdo.modules.bz.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bootdo.modules.bz.account.domain.AccountDO;
import com.bootdo.modules.bz.account.service.AccountService;
import com.bootdo.modules.bz.company.domain.CompanyDO;
import com.bootdo.modules.bz.company.service.CompanyService;

@RestController
@RequestMapping("/bz/company")
public class CopmanyRest {
	
	@Autowired
	private CompanyService companyService;
	
	@Autowired
	private AccountService accountService;
	
	/**
	 * 开户
	 */
	@CrossOrigin
	@RequestMapping(value = "/openAccount", method = RequestMethod.POST)
	public String save(@RequestBody CompanyDO company){
		System.out.println("open account:"+company.getComCode()+"  "+company.getComName());
		if(companyService.save(company)>0){
			AccountDO account = new AccountDO();
			account.setComCode(company.getComCode());
			account.setAccountCode(company.getComCode());
			if(accountService.save(account)>0){
				return "ok";
			}
		}
		return "fail";
	}

}
