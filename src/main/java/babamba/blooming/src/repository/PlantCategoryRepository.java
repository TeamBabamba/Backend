package babamba.blooming.src.repository;

import babamba.blooming.src.entity.PlantCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlantCategoryRepository extends JpaRepository<PlantCategoryEntity, Long> {

}
