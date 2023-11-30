package babamba.blooming.src.controller;

import babamba.blooming.config.BaseException;
import babamba.blooming.config.BaseResponse;
import babamba.blooming.src.dto.request.DeletePlantDto;
import babamba.blooming.src.dto.response.GetHomeDto;
import babamba.blooming.src.dto.response.GetPlantDetailsDto;
import babamba.blooming.src.service.PlantService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/plants")
@RequiredArgsConstructor
public class PlantController {
    private final PlantService plantService;


    /**
     * 홈 화면 조회
     */
    @GetMapping("")
    @Operation(summary = "홈화면 조회", description = "")
    public BaseResponse<List<GetHomeDto>> getHome(@RequestParam("type") Long type) {
        try {
            Long userId = 1L;

            return new BaseResponse<>(plantService.getHome(userId, type));

        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    /**
     * 특정 식물 상세 조회
     */
    @GetMapping("/details/{plantId}")
    @Operation(summary = "특정 식물 상세 조회", description = "")
    public BaseResponse<GetPlantDetailsDto> getPlantDetails(@PathVariable("plantId") Long plantId) {
        try {
            Long userId = 1L;

            return new BaseResponse<>(plantService.getPlantDetails(userId, plantId));

        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    /**
     * 특정 식물 데이터 삭제
     */
    @DeleteMapping("/details")
    @Operation(summary = "특정 식물 데이터 삭제", description = "")
    public BaseResponse<String> deletePlantDetails(@RequestBody DeletePlantDto deletePlantDto) {
        try {
            Long userId = 1L;

            plantService.deletePlantDetails(userId, deletePlantDto.getPlantId());

            return new BaseResponse<>("삭제 성공!");

        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }


}
