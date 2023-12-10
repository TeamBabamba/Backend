package babamba.blooming.src.repository;

import babamba.blooming.config.Status;
import babamba.blooming.src.entity.ManageEntity;
import babamba.blooming.src.entity.PlantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ManageRepository extends JpaRepository<ManageEntity, Long> {

    List<ManageEntity> findAllByPlantAndStatusOrderByCreatedAtDesc(PlantEntity plantEntity, Status status);

    List<ManageEntity> findAllByPlantAndCreatedAtAfter(PlantEntity plantEntity, LocalDateTime createdAt);
}
