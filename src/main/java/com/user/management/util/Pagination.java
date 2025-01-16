package com.user.management.util;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class Pagination {
    public static @NotNull Pageable getPageable(Filter filter) {
        int pageNumber = (filter.getPageNumber() == null || filter.getPageNumber() < 1) ? 0 : filter.getPageNumber() - 1;
        int pageSize = (filter.getPageSize() == null || filter.getPageSize() < 1) ? Integer.MAX_VALUE : filter.getPageSize();

        String sortDirection = filter.getSortDirection() != null &&
                (filter.getSortDirection().equalsIgnoreCase("asc") || filter.getSortDirection().equalsIgnoreCase("desc"))
                ? filter.getSortDirection()
                : "asc";
        String sortBy = (filter.getSortBy() == null || filter.getSortBy().trim().isEmpty()) ? "name" : filter.getSortBy();
        Sort sort = Sort.by(Sort.Order.by(sortBy).with(Sort.Direction.fromString(sortDirection)));
        return PageRequest.of(pageNumber, pageSize, sort);
    }
}
