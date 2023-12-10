package babamba.blooming.src.repository;

import babamba.blooming.config.Status;
import babamba.blooming.src.entity.PushAlarmEntity;
import babamba.blooming.src.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PushAlarmRepository extends JpaRepository<PushAlarmEntity, Long> {

    List<PushAlarmEntity> findAllByUserEntityAndStatusOrderByIdDesc(UserEntity userEntity, Status status);

}
