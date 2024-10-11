package com.jngyen.bookkeeping.backend.pojo.po;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BillDealChannalPO {
        private Integer id;
        private String dealChannal;
        private String userUuid;
        private LocalDateTime gmtCreate;
        private LocalDateTime gmtModified;
}
