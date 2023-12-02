package babamba.blooming.src.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class PutPlantDetailsDto {

    private Long plantId;

    private String imgUrl;

    private String plantNickname;

    private Long plantCategoryId;

    private List<String> plantStates;

    private List<String> recommendManagement;

}
