package com.jngyen.bookkeeping.backend.pojo.dto.bill;

import io.micrometer.common.lang.NonNull;
import lombok.Data;

@Data
public class BillDealTypeDTO {
    
    @NonNull
    private String userUuid;
    @NonNull
    private String dealType;
    private String dealTypeColor;
    private String newDealTypeName;
    private String newDealTypeColor;
}
