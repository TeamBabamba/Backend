package babamba.blooming.src.controller;

import babamba.blooming.config.BaseException;
import babamba.blooming.config.BaseResponse;
import babamba.blooming.src.dto.request.DeletePlantDto;
import babamba.blooming.src.dto.request.PostCreatePlantDto;
import babamba.blooming.src.dto.response.GetHomeDto;
import babamba.blooming.src.dto.response.GetManagementDto;
import babamba.blooming.src.dto.response.GetPlantDetailsDto;
import babamba.blooming.src.dto.response.GetTreatmentDto;
import babamba.blooming.src.service.PlantService;
import babamba.blooming.src.service.S3UploadService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/plants")
@RequiredArgsConstructor
public class PlantController {
    private final PlantService plantService;
    private final S3UploadService s3UploadService;


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

    /**
     * 식물 사진 분석(AI 추천 치료법)
     */
    @PostMapping("/treatments")
    @Operation(summary = "식물 사진 분석(AI 추천 치료법)", description = "")
    public BaseResponse<GetTreatmentDto> getTreatment(@RequestPart("file") MultipartFile multipartFile) {
        try {
            Long userId = 1L;

            String imgUrl = s3UploadService.saveFile(multipartFile);

            return new BaseResponse<>(plantService.getTreatment(userId, imgUrl));

        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }


    /**
     * 식물 사진 분석(관리하기 탭)
     */
    @GetMapping("/managements")
    @Operation(summary = "식물 사진 분석(관리하기 탭) 조회", description = "")
    public BaseResponse<List<GetManagementDto>> getManagement() {
        try {
            Long userId = 1L;

            return new BaseResponse<>(plantService.getManagement(userId));

        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    /**
     * 식물 관리 시작하기
     */
    @PostMapping("/managements")
    @Operation(summary = "식물 관리 시작하기", description = "")
    public BaseResponse<String> createPlant(@RequestBody PostCreatePlantDto postCreatePlantDto) {
        try {
            Long userId = 1L;

            plantService.createPlant(userId, postCreatePlantDto);

            return new BaseResponse<>("식물 관리 목록에 추가되었습니다");

        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

}
