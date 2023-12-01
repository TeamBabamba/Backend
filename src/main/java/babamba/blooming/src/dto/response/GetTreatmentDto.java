package babamba.blooming.src.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GetTreatmentDto {
    private String imgUrl;

    private List<String> plantState;

    private List<String> recommendManagement;
}
