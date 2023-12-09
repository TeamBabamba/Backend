package babamba.blooming.src.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UpdatePlantStateDto {
    private Long plantId;

    private Double temperature;

    private Double humidity;
}
