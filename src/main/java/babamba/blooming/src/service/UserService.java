package babamba.blooming.src.service;

import babamba.blooming.config.BaseException;
import babamba.blooming.config.Status;
import babamba.blooming.src.dto.response.GetMyPageDto;
import babamba.blooming.src.entity.UserEntity;
import babamba.blooming.src.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static babamba.blooming.config.BaseResponseStatus.NOT_ACTIVATED_USER;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;


    public GetMyPageDto getMyPage(Long userId) {
        UserEntity userEntity = userRepository.findByIdAndStatus(userId, Status.ACTIVE)
                .orElseThrow(() -> new BaseException(NOT_ACTIVATED_USER));

        return new GetMyPageDto(userEntity.getNickname(), userEntity.getProfileImgUrl(), userEntity.getEmail());
    }
}
