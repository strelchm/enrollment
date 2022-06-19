package ru.strelchm.enrollment.domain;

import lombok.*;
import ru.strelchm.enrollment.api.dto.ShopUnitType;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
//@Table(indexes = {
//    @Index(columnList = "city, street, number, region_id", name = "address_city_street_number_region_id_idx"),
//})
public class ShopUnit extends BaseEntity<UUID> {
  @Id
  private UUID id;

  private String name;

  @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
  @JoinColumn(name = "parent_id")
  private ShopUnit parent;

  @Enumerated(EnumType.STRING)
  private ShopUnitType type;

  private Long price;

  @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
  private List<ShopUnit> children;

//  private Long averagePrice;
}
