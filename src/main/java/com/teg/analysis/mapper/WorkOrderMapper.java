package com.teg.analysis.mapper;

import com.teg.analysis.model.DO.WorkOrderDO;
import com.teg.analysis.model.REQ.WorkOrderQueryREQ;
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
    List<WorkOrderDO> listWorkOrder(WorkOrderQueryREQ workOrderQueryREQ);


    int insertWorkOrder(WorkOrderDO user);


}