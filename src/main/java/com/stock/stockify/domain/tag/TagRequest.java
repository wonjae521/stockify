package com.stock.stockify.domain.tag;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TagRequest {

    @NotBlank(message = "태그 이름은 필수입니다.")
    private String name;

    private String description;

    @NotNull(message = "태그 타입은 필수입니다.")
    private Tag.Type type; // SYSTEM 또는 CUSTOM
}
