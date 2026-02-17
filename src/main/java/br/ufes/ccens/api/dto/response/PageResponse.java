package br.ufes.ccens.api.dto.response;

import java.util.List;

public record PageResponse<T> (
    List<T> content,
    Integer page,
    Integer pageSize,
    Long totalElements,
    Integer totalPages
){}
