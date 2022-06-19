package ru.strelchm.enrollment.domain;

import lombok.*;
import ru.strelchm.enrollment.api.dto.ShopUnitType;

import javax.persistence.*;
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
public class ShopUnitStatistics extends BaseEntity<UUID> {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  private String name;

  @ManyToOne
  @JoinColumn(name = "parent_id")
  private ShopUnitStatistics parent;

  @Enumerated(EnumType.STRING)
  private ShopUnitType type;

  private Long price;

  @ManyToOne
  @JoinColumn(name = "unit_id")
  private ShopUnit unit;
}
