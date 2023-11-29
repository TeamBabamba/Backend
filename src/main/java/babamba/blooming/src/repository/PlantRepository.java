package babamba.blooming.src.repository;

import babamba.blooming.config.Status;
import babamba.blooming.src.entity.PlantCategoryEntity;
import babamba.blooming.src.entity.PlantEntity;
import babamba.blooming.src.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlantRepository extends JpaRepository<PlantEntity, Long> {

    List<PlantEntity> findAllByUserAndStatus(UserEntity user, Status status);

    List<PlantEntity> findAllByUserAndPlantCategoryEntityAndStatus(UserEntity user, PlantCategoryEntity plantCategory, Status status);

}
