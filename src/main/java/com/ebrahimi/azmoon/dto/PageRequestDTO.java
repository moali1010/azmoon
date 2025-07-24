package com.ebrahimi.azmoon.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageRequestDTO {
    private Integer page;
    private Integer size;

}
