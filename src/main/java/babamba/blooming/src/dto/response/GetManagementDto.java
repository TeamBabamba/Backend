package babamba.blooming.src.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class GetManagementDto {
    private Long categoryId;

    private String categoryName;

    private Integer wateringCycle;
}
