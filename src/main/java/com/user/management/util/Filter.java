package com.user.management.util;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class Filter {
    private String sortBy;
    private Integer pageNumber;
    private Integer pageSize;
    private String sortDirection;
}
