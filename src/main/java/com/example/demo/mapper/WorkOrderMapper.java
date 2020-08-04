package com.example.demo.mapper;

import com.example.demo.model.DO.WorkOrderDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface WorkOrderMapper {
    /**
     * where name = #{name}
     *
     * @return
     */
    @Select("select id, work_no as workNo , create_date as createDate from work_order ")
    List<WorkOrderDO> listWorkOrder();


    int insertWorkOrder(WorkOrderDO user);


}