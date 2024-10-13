package com.jngyen.bookkeeping.backend.pojo.po.bill;

import java.time.LocalDateTime;

import lombok.Data; 

@Data
public class BillDealTypePO {
    private int id;
    private String userUuid;
    private String dealType;
    private String dealTypeColor;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;
}
