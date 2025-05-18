package com.stock.stockify.domain.user;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserPermissionId implements Serializable {
    private Long user;
    private Long permission;
}
