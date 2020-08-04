package com.example.demo.model.DO;

import lombok.Data;

import java.util.Date;

@Data
public class WorkOrderDO {
    private Long id;
    private String workNo;
    private Date createDate;


}
