package babamba.blooming.src.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GetHomeDto {

    private Long plantId;

    private String plantNickname;

    private String imgUrl;

    private List<Boolean> weekWater;

    private String createdAt;

}
