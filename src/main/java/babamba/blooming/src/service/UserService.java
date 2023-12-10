package babamba.blooming.src.service;

import babamba.blooming.config.BaseException;
import babamba.blooming.config.Status;
import babamba.blooming.src.dto.response.GetMyPageDto;
import babamba.blooming.src.dto.response.GetPushAlarmDto;
import babamba.blooming.src.entity.PushAlarmEntity;
import babamba.blooming.src.entity.UserEntity;
import babamba.blooming.src.repository.PushAlarmRepository;
import babamba.blooming.src.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static babamba.blooming.config.BaseResponseStatus.NOT_ACTIVATED_USER;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PushAlarmRepository pushAlarmRepository;


    public GetMyPageDto getMyPage(Long userId) {
        UserEntity userEntity = userRepository.findByIdAndStatus(userId, Status.ACTIVE)
                .orElseThrow(() -> new BaseException(NOT_ACTIVATED_USER));

        return new GetMyPageDto(userEntity.getNickname(), userEntity.getProfileImgUrl(), userEntity.getEmail());
    }

    public List<GetPushAlarmDto> getPushAlarms(Long userId) {
        UserEntity userEntity = userRepository.findByIdAndStatus(userId, Status.ACTIVE)
                .orElseThrow(() -> new BaseException(NOT_ACTIVATED_USER));

        List<GetPushAlarmDto> response = new ArrayList<>();

        List<PushAlarmEntity> pushAlarmEntities = pushAlarmRepository.findAllByUserEntityAndStatusOrderByIdDesc(userEntity, Status.ACTIVE);

        for (PushAlarmEntity pushAlarmEntity: pushAlarmEntities) {
            response.add(new GetPushAlarmDto(
                    pushAlarmEntity.getTitle(),
                    pushAlarmEntity.getContents(),
                    pushAlarmEntity.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))));
        }

        return response;
    }
}
