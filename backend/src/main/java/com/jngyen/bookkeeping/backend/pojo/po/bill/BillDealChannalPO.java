package com.jngyen.bookkeeping.backend.pojo.po.bill;

import lombok.Data;

import java.time.LocalDateTime;

import io.micrometer.common.lang.NonNull;

@Data
public class BillDealChannalPO {
        private Integer id;
        @NonNull
        private String dealChannal;
        @NonNull
        private String userUuid;
        private LocalDateTime gmtCreate;
        private LocalDateTime gmtModified;
}
