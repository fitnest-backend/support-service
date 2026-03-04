package az.fitnest.support.dto;

import java.util.List;
import lombok.Builder;
import org.springframework.data.domain.Page;

@Builder
public record PaginatedResponse<T>(
    List<T> items,
    long total,
    int page,
    int pageSize
) {
    public static <T> PaginatedResponse<T> of(Page<T> pageResult) {
        return PaginatedRespons
        e.<T>builder()
                .items(pageResult.getContent())
                .total(pageResult.getTotalElements())
                .page(pageResult.getNumber() + 1)
                .pageSize(pageResult.getSize())
                .build();
    }
}
