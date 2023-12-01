package babamba.blooming.src.repository;

import babamba.blooming.config.Status;
import babamba.blooming.src.entity.PlantCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlantCategoryRepository extends JpaRepository<PlantCategoryEntity, Long> {
    List<PlantCategoryEntity> findAllByStatus(Status status);
}
