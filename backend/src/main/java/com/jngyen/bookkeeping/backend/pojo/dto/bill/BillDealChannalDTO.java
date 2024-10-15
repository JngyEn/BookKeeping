package com.jngyen.bookkeeping.backend.pojo.dto.bill;

import io.micrometer.common.lang.NonNull;
import lombok.Data;

@Data
public class BillDealChannalDTO {
    @NonNull
    private String dealChannal;
    @NonNull
    private String userUuid;
    private String dealChannalColor;
    private String newDealChannalName;
    private String newDealChannalColor;
}
