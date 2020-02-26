package com.bootdo.modules.flowable.resources;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bootdo.modules.flowable.domain.PulishKeyDO;
import com.bootdo.modules.flowable.service.PulishKeyService;
import com.bootdo.common.utils.PageUtils;
import com.bootdo.common.utils.Query;
import com.bootdo.common.utils.R;

/**
 * 
 * 
 * @author chglee
 * @email 1992lcg@163.com
 * @date 2019-10-09 15:39:25
 */
 
@Controller
@RequestMapping("/flowable/pulishKey")
public class PulishKeyController {
	@Autowired
	private PulishKeyService pulishKeyService;
	
	@GetMapping()
//	@RequiresPermissions("flowable:pulishKey:pulishKey")
	String PulishKey(){
	    return "flowable/pulishKey/pulishKey";
	}
	
	@ResponseBody
	@GetMapping("/list")
//	@RequiresPermissions("flowable:pulishKey:pulishKey")
	public PageUtils list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<PulishKeyDO> pulishKeyList = pulishKeyService.list(query);
		int total = pulishKeyService.count(query);
		PageUtils pageUtils = new PageUtils(pulishKeyList, total);
		return pageUtils;
	}
	
	@GetMapping("/add")
//	@RequiresPermissions("flowable:pulishKey:add")
	String add(){
	    return "flowable/pulishKey/add";
	}

	@GetMapping("/edit/{id}")
//	@RequiresPermissions("flowable:pulishKey:edit")
	String edit(@PathVariable("id") String id,Model model){
		PulishKeyDO pulishKey = pulishKeyService.get(id);
		model.addAttribute("pulishKey", pulishKey);
	    return "flowable/pulishKey/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
//	@RequiresPermissions("flowable:pulishKey:add")
	public R save( PulishKeyDO pulishKey){
		if(pulishKeyService.save(pulishKey)>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
//	@RequiresPermissions("flowable:pulishKey:edit")
	public R update( PulishKeyDO pulishKey){
		pulishKeyService.update(pulishKey);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
//	@RequiresPermissions("flowable:pulishKey:remove")
	public R remove( String id){
		if(pulishKeyService.remove(id)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
//	@RequiresPermissions("flowable:pulishKey:batchRemove")
	public R remove(@RequestParam("ids[]") String[] ids){
		pulishKeyService.batchRemove(ids);
		return R.ok();
	}
	
}
