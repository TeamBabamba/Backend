package babamba.blooming.src.repository;

import babamba.blooming.src.entity.ManageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManageRepository extends JpaRepository<ManageEntity, Long> {
}
