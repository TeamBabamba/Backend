package babamba.blooming.src.repository;

import babamba.blooming.config.Status;
import babamba.blooming.src.entity.PlantCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlantCategoryRepository extends JpaRepository<PlantCategoryEntity, Long> {

    Optional<PlantCategoryEntity> findByIdAndStatus(Long categoryId, Status status);

    List<PlantCategoryEntity> findAllByStatus(Status status);
}
