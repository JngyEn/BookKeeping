package com.jngyen.bookkeeping.backend.pojo.dto.bill;

import io.micrometer.common.lang.NonNull;
import lombok.Data;

@Data
public class BillDealTypeDTO {
    @NonNull
    private String dealType;
    @NonNull
    private String userUuid;
    private String newDealTypeName;
}
