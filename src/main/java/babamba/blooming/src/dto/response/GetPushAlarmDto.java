package babamba.blooming.src.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GetPushAlarmDto {
    private String title;

    private String contents;

    private String createdAt;
}
