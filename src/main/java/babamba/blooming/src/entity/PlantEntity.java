package babamba.blooming.src.entity;

import babamba.blooming.config.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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

    @Column
    private Double temperature;

    @Column
    private Double humidity;

    @Column
    private Integer light;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plant_category_id")
    private PlantCategoryEntity plantCategoryEntity;

    @OneToMany(mappedBy = "plant", cascade = CascadeType.ALL)
    private List<ManageEntity> manageEntities = new ArrayList<>();


    public PlantEntity(String plantName,
                       String plantNickname,
                       String plantState,
                       String imgUrl,
                       String recommendManagement,
                       UserEntity user,
                       PlantCategoryEntity plantCategoryEntity) {
        this.plantName = plantName;
        this.plantNickname = plantNickname;
        this.plantState = plantState;
        this.imgUrl = imgUrl;
        this.recommendManagement = recommendManagement;
        this.user = user;
        this.plantCategoryEntity = plantCategoryEntity;
    }

    public void updatePlantEntity(String plantName,
                                  String plantNickname,
                                  String plantState,
                                  String imgUrl,
                                  String recommendManagement,
                                  PlantCategoryEntity plantCategoryEntity) {
        this.plantName = plantName;
        this.plantNickname = plantNickname;
        this.plantState = plantState;
        this.imgUrl = imgUrl;
        this.recommendManagement = recommendManagement;
        this.plantCategoryEntity = plantCategoryEntity;
    }

    public void updatePlantState(Double temperature, Double humidity) {
        this.temperature = temperature;
        this.humidity = humidity;
    }
}
