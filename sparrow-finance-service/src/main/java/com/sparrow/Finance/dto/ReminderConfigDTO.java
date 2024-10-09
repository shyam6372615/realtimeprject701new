package com.sparrow.Finance.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReminderConfigDTO {

    private Long id;
    private String activityType;
    private String days;
    private Boolean isThroughEmail;
    private Boolean isThroughApp;
    private Boolean isThroughSms;
    private Boolean isDefault;
    private Long instituteId;
}
