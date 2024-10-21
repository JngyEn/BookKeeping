package com.jngyen.bookkeeping.backend.pojo.po.bill;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BillDealChannelPO {
        private Integer id;
        private String dealChannel;
        private String userUuid;
        private String dealChannelColor;
        private LocalDateTime gmtCreate;
        private LocalDateTime gmtModified;
}
