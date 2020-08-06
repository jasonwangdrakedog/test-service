package com.teg.analysis.model.DTO;

import lombok.Data;

import java.util.Date;

@Data
public class WorkOrderDTO {


    private Long id;

    private String workNo;

    private Date createDate;


}
