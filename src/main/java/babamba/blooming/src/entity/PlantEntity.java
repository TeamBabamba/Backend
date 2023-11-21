package babamba.blooming.src.entity;

import babamba.blooming.config.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "Plant")
@NoArgsConstructor
public class PlantEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "INT UNSIGNED")
    private Long id;

    @Column(nullable = false)
    private String plantName;

    @Column(nullable = false)
    private String plantNickname;

    @Column(nullable = false)
    private String plantState;

    @Column(nullable = false)
    private String imgUrl;

    @Column(nullable = false)
    private String recommendManagement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plant_category_id")
    private PlantCategoryEntity plantCategoryEntity;

}