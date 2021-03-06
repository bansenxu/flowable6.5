package com.bootdo.modules.flowable.dao;

import com.bootdo.modules.flowable.domain.ExtClassDO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 
 * @author wangxing
 * @email 358402521@qq.com
 * @date 2019-08-15 15:59:38
 */
@Mapper
public interface ExtClassDao {

	ExtClassDO get(String id);
	
	List<ExtClassDO> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(ExtClassDO extClass);
	
	int update(ExtClassDO extClass);
	
	int remove(String id);
	
	int batchRemove(String[] ids);
}
