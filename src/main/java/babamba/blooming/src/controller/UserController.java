package babamba.blooming.src.controller;

import babamba.blooming.config.BaseException;
import babamba.blooming.config.BaseResponse;
import babamba.blooming.src.dto.request.DeletePlantDto;
import babamba.blooming.src.dto.response.GetMyPageDto;
import babamba.blooming.src.dto.response.GetPushAlarmDto;
import babamba.blooming.src.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 마이페이지 조회
     */
    @GetMapping("")
    @Operation(summary = "마이페이지 조회", description = "")
    public BaseResponse<GetMyPageDto> getMyPage() {
        try {
            Long userId = 1L;

            return new BaseResponse<>(userService.getMyPage(userId));

        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    /**
     * 푸시알림 조회
     */
    @GetMapping("/push-alarms")
    @Operation(summary = "푸시알림 조회", description = "")
    public BaseResponse<List<GetPushAlarmDto>> getPushAlarms() {
        try {
            Long userId = 1L;

            return new BaseResponse<>(userService.getPushAlarms(userId));

        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
}
