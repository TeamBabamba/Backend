package babamba.blooming.src.entity;

import babamba.blooming.config.BaseEntity;
import babamba.blooming.config.ManageType;
import babamba.blooming.config.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "Manage")
@NoArgsConstructor
public class ManageEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "INT UNSIGNED")
    private Long id;

    @Column
    private String imgUrl;

    @Column(nullable = false)
    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plant_id")
    private PlantEntity plant;

    @Enumerated(EnumType.STRING)
    @Column(name = "manage_type", nullable = false, length = 8)
    protected ManageType manageType = ManageType.WATER;

    public void setManageType(ManageType manageType) {
        this.manageType = manageType;
    }
}
