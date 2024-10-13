package com.jngyen.bookkeeping.backend.pojo.po.bill;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BillDealChannalPO {
        private Integer id;
        private String dealChannal;
        private String userUuid;
        private String dealChannalColor;
        private LocalDateTime gmtCreate;
        private LocalDateTime gmtModified;
}
