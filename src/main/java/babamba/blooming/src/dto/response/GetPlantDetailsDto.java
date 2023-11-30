package babamba.blooming.src.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class GetPlantDetailsDto {
    private Long plantId;

    private String plantNickname;

    private String categoryName;

    private String createdAt;

    private String imgUrl;

    private String plantState;

    private String recommendManagement;

    private String temperature;

    private String humidity;

    private String light;

    private List<ManageLog> manageLogs;
}
