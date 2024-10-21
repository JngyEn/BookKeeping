package com.jngyen.bookkeeping.backend.pojo.dto.bill;

import lombok.Data;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

@Data
public class BillDealChannelDTO {
    @NotBlank
    private String dealChannel;
    @NotBlank
    private String userUuid;
    private String dealChannelColor;
    private String newDealChannelName;
    private String newDealChannelColor;
}
