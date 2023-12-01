package babamba.blooming.src.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostCreatePlantDto {
    private String imgUrl;

    private String plantNickname;

    private Long plantCategoryId;

    private List<String> plantStates;

    private List<String> recommendManagement;
}
