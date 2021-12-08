package com.shiro.demo.service.mapper;

import com.shiro.demo.service.entity.Operation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Cxx
 * @since 2021-12-06
 */
@Repository
public interface OperationMapper extends BaseMapper<Operation> {
    // 根据操作ID获取操作信息
    @Select("select * from operation where id=#{operationID}")
    @Results(id = "SimpleOperationMap", value = {
            @Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER, id = true),
            @Result(column = "operation_name", property = "operationName", jdbcType = JdbcType.VARCHAR),
            @Result(column = "operation_description", property = "operationDescription", jdbcType = JdbcType.VARCHAR)
    })
    Operation selectOperationByOperationID(Integer operationID);
}
