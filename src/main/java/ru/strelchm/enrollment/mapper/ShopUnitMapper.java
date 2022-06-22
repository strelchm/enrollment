package ru.strelchm.enrollment.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.strelchm.enrollment.api.dto.ShopUnitImport;
import ru.strelchm.enrollment.api.dto.ShopUnitStatisticUnit;
import ru.strelchm.enrollment.domain.ShopUnit;
import ru.strelchm.enrollment.domain.ShopUnitStatistics;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class ShopUnitMapper {
  @Mapping(target = "parent", expression = "java(dto.getParentId() == null ? null : toShopUnit(dto.getParentId()))")
  public abstract ShopUnit toShopUnit(ShopUnitImport dto);

  public ShopUnit toShopUnit(UUID parentId) {
    ShopUnit shopUnit = new ShopUnit();
    shopUnit.setId(parentId);
    return shopUnit;
  }

  @Mapping(target = "date", expression = "java(dbo.getUpdated())")
  public abstract ShopUnitStatisticUnit toShopUnitStatisticUnitDto(ShopUnitStatistics dbo);

  @Mapping(target = "date", expression = "java(dbo.getUpdated())")
  @Mapping(target = "parentId", expression = "java(dbo.getParent() == null ? null : dbo.getParent().getId())")
//  @Mapping(target = "children", expression = "java(dbo.getChildren().isEmpty() ? null : dbo.getChildren())")
  public abstract ShopUnitStatisticUnit toShopUnitStatisticUnitDto(ShopUnit dbo);

  @Mapping(target = "date", expression = "java(dbo.getUpdated())")
  @Mapping(target = "parentId", expression = "java(dbo.getParent() == null ? null : dbo.getParent().getId())")
  @Mapping(target = "children", expression = "java(shopUnitListToShopUnitList(dbo.getChildren()))")
  public abstract ru.strelchm.enrollment.api.dto.ShopUnit toShopUnitDto(ShopUnit dbo);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "created", ignore = true)
  @Mapping(target = "parentId", expression = "java(dbo.getParent() == null ? null : dbo.getParent().getId())")
  @Mapping(target = "unit", expression = "java(dbo)")
  public abstract ShopUnitStatistics toShopUnitStatistics(ShopUnit dbo);

  protected List<ru.strelchm.enrollment.api.dto.ShopUnit> shopUnitListToShopUnitList(List<ShopUnit> list) {
    return list == null || list.isEmpty() ? null : list.stream().map(this::toShopUnitDto).collect(Collectors.toList());
  }
}