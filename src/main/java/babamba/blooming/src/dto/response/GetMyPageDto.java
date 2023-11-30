package babamba.blooming.src.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class GetMyPageDto {

    private String nickname;

    private String profileImgUrl;

    private String email;

}
