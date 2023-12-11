package babamba.blooming.src.service;

import babamba.blooming.config.BaseException;
import babamba.blooming.config.ManageType;
import babamba.blooming.config.Status;
import babamba.blooming.src.dto.request.UpdatePlantStateDto;
import babamba.blooming.src.dto.request.PostCreatePlantDto;
import babamba.blooming.src.dto.request.PutPlantDetailsDto;
import babamba.blooming.src.dto.response.*;
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
import java.util.*;

import static babamba.blooming.config.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
public class PlantService {

    private final UserRepository userRepository;
    private final PlantRepository plantRepository;
    private final ManageRepository manageRepository;
    private final PlantCategoryRepository plantCategoryRepository;
    private final ColabService colabService;
    private final ChatService chatService;


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


            List<ManageEntity> manageEntities = manageRepository.findAllByPlantAndCreatedAtAfter(plantEntity, beforeWeek);

            List<Boolean> weekWater = Arrays.asList(false, false, false, false, false, false, false);

            for (ManageEntity manageEntity : manageEntities) {
                if (manageEntity.getManageType().equals(ManageType.WATER)) {
                    // 일요일 부터 토요일까지 0~6의 값
                    int dayOfWeek = manageEntity.getCreatedAt().getDayOfWeek().getValue() % 7;
                    weekWater.set(dayOfWeek, true);
                }
            }

            getHomeDto.setWeekWater(weekWater);

            response.add(getHomeDto);
        }

        return response;
    }

    public GetPlantDetailsDto getPlantDetails(Long userId, Long plantId) throws BaseException {
        UserEntity userEntity = userRepository.findByIdAndStatus(userId, Status.ACTIVE)
                .orElseThrow(() -> new BaseException(NOT_ACTIVATED_USER));

        PlantEntity plantEntity = plantRepository.findByIdAndStatus(plantId, Status.ACTIVE)
                .orElseThrow(() -> new BaseException(NOT_ACTIVATED_PLANT));

        GetPlantDetailsDto response = new GetPlantDetailsDto();

        response.setPlantId(plantId);
        response.setPlantNickname(plantEntity.getPlantNickname());
        response.setCategoryName(plantEntity.getPlantCategoryEntity().getCategoryName());
        response.setCreatedAt(plantEntity.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")));
        response.setImgUrl(plantEntity.getImgUrl());
        response.setPlantState(plantEntity.getPlantState());
        response.setRecommendManagement(plantEntity.getRecommendManagement());

        // 온도 갱신
        if (plantEntity.getTemperature() == null) {
            response.setTemperature("잠시 후에 업데이트");
        }
        else {
            response.setTemperature(plantEntity.getTemperature().toString() + "도");
        }

        // 습도 갱신
        if (plantEntity.getHumidity() == null) {
            response.setHumidity("잠시 후에 업데이트");
        }
        else {
            response.setHumidity(plantEntity.getHumidity().toString() + "%");
        }

        // 조도 갱신
        if (plantEntity.getLight() == null) {
            response.setLight("잠시 후에 업데이트");
        }
        else {
            response.setLight(plantEntity.getLight().toString());
        }

        List<ManageEntity> manageEntities = manageRepository.findAllByPlantAndStatusOrderByCreatedAtDesc(plantEntity, Status.ACTIVE);

        List<ManageLog> manageLogs = new ArrayList<>();

        for (ManageEntity manageEntity : manageEntities) {
            ManageLog manageLog = new ManageLog();

            String message = plantEntity.getPlantNickname() + " \'" + manageEntity.getContents() + "\'를 완료했어요";
            manageLog.setMessage(message);

            manageLog.setCreatedAt(manageEntity.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")));
            manageLogs.add(manageLog);
        }

        response.setManageLogs(manageLogs);

        return response;
    }

    public void deletePlantDetails(Long userId, Long plantId) {
        UserEntity userEntity = userRepository.findByIdAndStatus(userId, Status.ACTIVE)
                .orElseThrow(() -> new BaseException(NOT_ACTIVATED_USER));

        PlantEntity plantEntity = plantRepository.findByIdAndStatus(plantId, Status.ACTIVE)
                .orElseThrow(() -> new BaseException(NOT_ACTIVATED_PLANT));

        plantEntity.setStatus(Status.INACTIVE);

        plantRepository.save(plantEntity);
    }

    public GetTreatmentDto getTreatment(Long userId, String imgUrl) throws BaseException {
        UserEntity userEntity = userRepository.findByIdAndStatus(userId, Status.ACTIVE)
                .orElseThrow(() -> new BaseException(NOT_ACTIVATED_USER));

        List<String> plantAnalytics = colabService.getPlantAnalytics(imgUrl);
        String plantCategory = plantAnalytics.get(0);
        String plantState = plantAnalytics.get(1);

        List<String> numbers = Arrays.asList("1", "2", "3", "4", "5");

        GetTreatmentDto response = new GetTreatmentDto();
        response.setImgUrl(imgUrl);

        // 잎의 상태 정보 넣기
        List<String> plantStates = new ArrayList<>();
        if (plantState.equals("healthy")) {
            plantStates.add("잎의 색이 밝고 생기가 있으며, 잎의 형태 또한 정상이에요");
            plantStates.add("식물이 매우 건강한 상태에요");
            response.setPlantState(plantStates);
        }
        else {
            String question = plantState + "에 걸린" + plantCategory + " 잎의 대표적인 특징을 3가지만 말해줘. 답변의 말투는 해요체로 작성해줘. 마지막으로 답변은 부가 설명 없이 엔터로 구분하여 작성해줘";
            String gptAnswer = chatService.getChatResponse(question, 0.5f, 1000);

            System.out.println("GPT 답변 : " + gptAnswer);

            String[] splitAnswer = gptAnswer.split("\\n");

            for (String ele : splitAnswer) {
                if (ele.length() == 0) {
                    continue;
                }

                String firstString = ele.substring(0, 1);
                for (int i = 0; i < 5; i++) {
                    if (firstString.equals(numbers.get(i))) {
                        plantStates.add(ele.substring(3));
                    }
                    else if (firstString.equals("-")) {
                        plantStates.add(ele.substring(2));
                    }
                }
            }

            response.setPlantState(plantStates);
        }

        // AI 추천 치료법 넣기
        List<String> recommendManagement = new ArrayList<>();
        if (plantState.equals("healthy")) {
            recommendManagement.add("식물의 상태가 매우 건강해요");
            recommendManagement.add("지금처럼 계속 관리해주세요");
            response.setPlantState(recommendManagement);
        }
        else {
            String question = plantState + "에 걸린" +  plantCategory + "를 치료할 수 있는 대표적인 방법을 구체적으로 5가지만 말해줘. 하나의 방법 당 글자수는 40자 이내로 말해줘. 답변의 말투는 해요체로 작성해줘. 마지막으로 답변은 부가 설명 없이 엔터로 구분하여 작성해줘";

            String gptAnswer = chatService.getChatResponse(question, 0.5f, 1000);

            System.out.println("GPT 답변 : " + gptAnswer);

            String[] splitAnswer = gptAnswer.split("\\n");

            for (String ele : splitAnswer) {
                if (ele.length() == 0) {
                    continue;
                }

                String firstString = ele.substring(0, 1);
                for (int i = 0; i < 5; i++) {
                    if (firstString.equals(numbers.get(i))) {
                        recommendManagement.add(ele.substring(3));
                    }
                    else if (firstString.equals("-")) {
                        plantStates.add(ele.substring(2));
                    }
                }
            }

            response.setRecommendManagement(recommendManagement);
        }

        return response;
    }


    public List<GetManagementDto> getManagement(Long userId) {
        UserEntity userEntity = userRepository.findByIdAndStatus(userId, Status.ACTIVE)
                .orElseThrow(() -> new BaseException(NOT_ACTIVATED_USER));

        List<PlantCategoryEntity> plantCategoryEntities = plantCategoryRepository.findAllByStatus(Status.ACTIVE);

        List<GetManagementDto> response = new ArrayList<>();

        for (PlantCategoryEntity plantCategoryEntity : plantCategoryEntities) {
            response.add(new GetManagementDto(plantCategoryEntity.getId(), plantCategoryEntity.getCategoryName(), plantCategoryEntity.getWateringCycle()));
        }

        return response;
    }

    public void createPlant(Long userId, PostCreatePlantDto postCreatePlantDto) {
        UserEntity userEntity = userRepository.findByIdAndStatus(userId, Status.ACTIVE)
                .orElseThrow(() -> new BaseException(NOT_ACTIVATED_USER));

        PlantCategoryEntity plantCategoryEntity = plantCategoryRepository.findByIdAndStatus(postCreatePlantDto.getPlantCategoryId(), Status.ACTIVE)
                .orElseThrow(() -> new BaseException(INVALID_PLANT_CATEGORY));

        StringBuilder stringBuilder = new StringBuilder();
        List<String> planStates = postCreatePlantDto.getPlantStates();
        for (int i = 0; i < planStates.size(); i++) {
            stringBuilder.append(planStates.get(i));

            // 마지막 인덱스가 아닌 경우 : 맨 끝에 개행문자 넣기
            if (i != planStates.size() - 1) {
                stringBuilder.append("\n");
            }
        }

        String plantState = stringBuilder.toString();

        // StringBuilder 초기화
        stringBuilder.setLength(0);

        List<String> recommendManagements = postCreatePlantDto.getRecommendManagement();
        for (int i = 0; i < recommendManagements.size(); i++) {
            stringBuilder.append(recommendManagements.get(i));

            // 마지막 인덱스가 아닌 경우 : 맨 끝에 개행문자 넣기
            if (i != recommendManagements.size() - 1) {
                stringBuilder.append("\n");
            }
        }

        String recommendManagement = stringBuilder.toString();

        PlantEntity plantEntity = new PlantEntity(
                plantCategoryEntity.getCategoryName(),
                postCreatePlantDto.getPlantNickname(),
                plantState,
                postCreatePlantDto.getImgUrl(),
                recommendManagement,
                userEntity,
                plantCategoryEntity);

        plantRepository.save(plantEntity);
    }

    public void updatePlantDetails(Long userId, PutPlantDetailsDto putPlantDetailsDto) {
        UserEntity userEntity = userRepository.findByIdAndStatus(userId, Status.ACTIVE)
                .orElseThrow(() -> new BaseException(NOT_ACTIVATED_USER));

        PlantCategoryEntity plantCategoryEntity = plantCategoryRepository.findByIdAndStatus(putPlantDetailsDto.getPlantCategoryId(), Status.ACTIVE)
                .orElseThrow(() -> new BaseException(INVALID_PLANT_CATEGORY));

        PlantEntity plantEntity = plantRepository.findByIdAndStatus(putPlantDetailsDto.getPlantId(), Status.ACTIVE)
                .orElseThrow(() -> new BaseException(NOT_ACTIVATED_PLANT));

        StringBuilder stringBuilder = new StringBuilder();
        List<String> planStates = putPlantDetailsDto.getPlantStates();
        for (int i = 0; i < planStates.size(); i++) {
            stringBuilder.append(planStates.get(i));

            // 마지막 인덱스가 아닌 경우 : 맨 끝에 개행문자 넣기
            if (i != planStates.size() - 1) {
                stringBuilder.append("\n");
            }
        }

        String plantState = stringBuilder.toString();

        // StringBuilder 초기화
        stringBuilder.setLength(0);

        List<String> recommendManagements = putPlantDetailsDto.getRecommendManagement();
        for (int i = 0; i < recommendManagements.size(); i++) {
            stringBuilder.append(recommendManagements.get(i));

            // 마지막 인덱스가 아닌 경우 : 맨 끝에 개행문자 넣기
            if (i != recommendManagements.size() - 1) {
                stringBuilder.append("\n");
            }
        }

        String recommendManagement = stringBuilder.toString();

        plantEntity.updatePlantEntity(
                plantCategoryEntity.getCategoryName(),
                putPlantDetailsDto.getPlantNickname(),
                plantState,
                putPlantDetailsDto.getImgUrl(),
                recommendManagement,
                plantCategoryEntity
        );

        plantRepository.save(plantEntity);
    }

    public void updatePlantState(Long userId, UpdatePlantStateDto updatePlantState) {
        UserEntity userEntity = userRepository.findByIdAndStatus(userId, Status.ACTIVE)
                .orElseThrow(() -> new BaseException(NOT_ACTIVATED_USER));

        PlantEntity plantEntity = plantRepository.findByIdAndStatus(updatePlantState.getPlantId(), Status.ACTIVE)
                .orElseThrow(() -> new BaseException(NOT_ACTIVATED_PLANT));

        plantEntity.updatePlantState(updatePlantState.getTemperature(), updatePlantState.getHumidity());
    }
}
