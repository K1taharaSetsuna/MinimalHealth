package com.minimalhealth.model.dto.request;

import lombok.Data;

@Data
public class ReminderUpdateRequest {
    private Boolean waterEnabled;
    private Boolean sleepEnabled;
    private Boolean exerciseEnabled;
}
