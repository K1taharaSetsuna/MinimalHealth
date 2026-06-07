package com.minimalhealth.model.dto.request;

import com.minimalhealth.model.enums.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProfileBasicRequest {
    @NotBlank(message = "姓名不能为空")
    private String name;

    @NotNull(message = "性别不能为空")
    private Gender gender;

    @NotBlank(message = "出生日期不能为空")
    private String birthDate; // yyyy-MM-dd
}
