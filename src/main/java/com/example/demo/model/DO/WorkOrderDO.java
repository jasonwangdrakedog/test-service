package com.example.demo.model.DO;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

@Data
public class WorkOrderDO {
    private Long id;
    private String workNo;
    private Date createDate;


}
