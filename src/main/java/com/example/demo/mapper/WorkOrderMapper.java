package com.example.demo.mapper;

import com.example.demo.model.DO.WorkOrderDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkOrderMapper {
    List<WorkOrderDO> listWorkOrder(@Param(value = "workNo") String workNo);


    int insertWorkOrder(WorkOrderDO user);


}