package com.bootdo.modules.flowable.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface SQLExecDao {
    List<Map<String, Object>> list(@Param("_parameter") String strSQL);

    int delete(@Param("_parameter") String strSQL);

    int update(@Param("_parameter") String strSQL);
}
