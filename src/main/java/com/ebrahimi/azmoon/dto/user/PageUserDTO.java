package com.ebrahimi.azmoon.dto.user;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageUserDTO {
    private List<UserAccountDTO> data;
    private Long total;

}
