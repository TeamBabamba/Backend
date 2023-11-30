package babamba.blooming.src.controller;

import babamba.blooming.config.BaseException;
import babamba.blooming.config.BaseResponse;
import babamba.blooming.src.dto.request.DeletePlantDto;
import babamba.blooming.src.dto.response.GetMyPageDto;
import babamba.blooming.src.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
}
