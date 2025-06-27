package com.stock.stockify.domain.inventory;

import com.stock.stockify.domain.inventory.InventoryUnitRequest;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class InventoryItemRequest {

    /**
     * 재고 이름 (중복 불가, 어드민 단위)
     */
    @NotBlank(message = "재고 이름은 필수입니다.")
    @Size(max = 50, message = "재고 이름은 50자 이하여야 합니다.")
    private String name;

    /**
     * 재고 단위 (예: 개, 박스 등)
     */
    @NotBlank(message = "단위는 필수입니다.")
    @Size(max = 10, message = "단위는 10자 이하여야 합니다.")
    private String unit;

    /**
     * 재고 임계 수량
     */
    @NotNull(message = "임계 수량은 필수입니다.")
    @Min(value = 0, message = "임계 수량은 0 이상이어야 합니다.")
    private Integer thresholdQuantity;

    /**
     * 재고 가격
     */
    @NotNull(message = "가격은 필수입니다.")
    @PositiveOrZero(message = "가격은 0 이상이어야 합니다.")
    private Double price;

    /**
     * 카테고리 ID (필수)
     */
    @NotNull(message = "카테고리 ID는 필수입니다.")
    private Long categoryId;

    /**
     * 메모 (선택)
     */
    @Size(max = 255, message = "메모는 255자 이하여야 합니다.")
    private String memo;

    /**
     * 개별 유닛 리스트 (RFID/바코드/유통기한 관리)
     */
    @NotEmpty(message = "최소 1개 이상의 유닛이 필요합니다.")
    private List<InventoryUnitRequest> units;
}
