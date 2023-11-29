package babamba.blooming.src.service;

import babamba.blooming.config.BaseException;
import babamba.blooming.config.Status;
import babamba.blooming.src.dto.response.GetHomeDto;
import babamba.blooming.src.entity.ManageEntity;
import babamba.blooming.src.entity.PlantCategoryEntity;
import babamba.blooming.src.entity.PlantEntity;
import babamba.blooming.src.entity.UserEntity;
import babamba.blooming.src.repository.ManageRepository;
import babamba.blooming.src.repository.PlantCategoryRepository;
import babamba.blooming.src.repository.PlantRepository;
import babamba.blooming.src.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static babamba.blooming.config.BaseResponseStatus.INVALID_PLANT_CATEGORY;
import static babamba.blooming.config.BaseResponseStatus.NOT_ACTIVATED_USER;

@Service
@RequiredArgsConstructor
public class PlantService {

    private final UserRepository userRepository;
    private final PlantRepository plantRepository;
    private final ManageRepository manageRepository;
    private final PlantCategoryRepository plantCategoryRepository;


    public List<GetHomeDto> getHome(Long userId, Long plantCategoryId) throws BaseException {

        UserEntity userEntity = userRepository.findByIdAndStatus(userId, Status.ACTIVE)
                .orElseThrow(() -> new BaseException(NOT_ACTIVATED_USER));

        List<PlantEntity> plantEntities;

        // 전체 보기인 경우
        if (plantCategoryId == 0) {
            plantEntities = plantRepository.findAllByUserAndStatus(userEntity, Status.ACTIVE);
        }
        else {
            PlantCategoryEntity plantCategoryEntity = plantCategoryRepository.findById(plantCategoryId)
                    .orElseThrow(() -> new BaseException(INVALID_PLANT_CATEGORY));

            plantEntities = plantRepository.findAllByUserAndPlantCategoryEntityAndStatus(userEntity, plantCategoryEntity, Status.ACTIVE);

        }

        // 리턴 변수 정의
        List<GetHomeDto> response = new ArrayList<>();

        for (PlantEntity plantEntity : plantEntities) {
            GetHomeDto getHomeDto = new GetHomeDto();

            getHomeDto.setPlantId(plantEntity.getId());
            getHomeDto.setImgUrl(plantEntity.getImgUrl());
            getHomeDto.setPlantNickname(plantEntity.getPlantNickname());
            getHomeDto.setCreatedAt(plantEntity.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")));

            // 일주일 간 물주기 확인
            LocalDateTime beforeWeek = LocalDate.now().atStartOfDay();

            // 일요일 부터 토요일까지 0~6의 값
            int weekValue = beforeWeek.getDayOfWeek().getValue() % 7;

            beforeWeek = beforeWeek.minusDays(weekValue % 7);


            List<ManageEntity> manageEntities = manageRepository.findAllByPlantAndCreatedAtAfterAndStatus(plantEntity, beforeWeek, Status.ACTIVE);

            List<Boolean> weekWater = Arrays.asList(false, false, false, false, false, false, false);

            for (ManageEntity manageEntity : manageEntities) {
                // 일요일 부터 토요일까지 0~6의 값
                int dayOfWeek = manageEntity.getCreatedAt().getDayOfWeek().getValue() % 7;
                weekWater.set(dayOfWeek, true);
            }

            getHomeDto.setWeekWater(weekWater);

            response.add(getHomeDto);
        }

        return response;
    }
}
