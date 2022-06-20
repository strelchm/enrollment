package ru.strelchm.enrollment.mapper;


import org.mapstruct.*;
import ru.strelchm.enrollment.api.dto.ShopUnitImport;
import ru.strelchm.enrollment.api.dto.ShopUnitStatisticUnit;
import ru.strelchm.enrollment.api.dto.ShopUnitType;
import ru.strelchm.enrollment.domain.ShopUnit;
import ru.strelchm.enrollment.domain.ShopUnitStatistics;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class ImportsMapper {
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
    if ( list == null || list.isEmpty()) {
      return null;
    }
    return list.stream()
        .map(this::toShopUnitDto)
//        .sorted((v1, v2) -> {
//          if (v1.getPrice() != null && v2.getPrice() != null) {
//          return v1.getPrice().compareTo(v2.getPrice());
//          }
//          if (v1.getPrice() != null) {
//            return -1;
//          }
//          return 1;
//        })
        .collect(Collectors.toList());
  }

//  public Long countAvgPrice(ShopUnit dbo) {
//    if (dbo.getType() == ShopUnitType.OFFER) {
//      return dbo.getPrice();
//    }
//    if (dbo.getPrice() != null) { // TODO
//      throw new UnsupportedOperationException("FOO");
//    }
//    List<ShopUnit> children = dbo.getChildren();
//    if (children == null || children.isEmpty()) {
//      return null;
//    }
//
//
//  }



//    public PersonPageDto toPersonResponsePageDto(Page<Person> person) {
//        return new PersonPageDto(person.getTotalPages(), person.getTotalElements(), person.getSize(),
//                person.getContent().stream().map(this::toPersonResponseDto).collect(Collectors.toList()));
//    }
//
//    @Mapping(target = "telephone", expression = "java(contactService.getTelephoneNumberByPerson(person))")
//    @Mapping(target = "mainDocument", expression = "java(toIdentityDocumentDto(documentService.findMainDocument(person)))")
//    public abstract PersonResponseDto toPersonResponseDto(Person person);
//
//    @Mapping(target = "addresses", expression = "java(fromAddressDtoList(dto.getAddresses() ))")
//    public abstract Person fromPersonCreateDto(PersonCreateRequestDto dto);
//
//    @Mapping(target = "hidden", expression = "java(dto.getHidden() == null ? null : dto.getHidden().booleanValue())")
//    public abstract Person fromPersonUpdateDto(PersonUpdateRequestDto dto);
//
//    public abstract IdentityDocumentDto toIdentityDocumentDto(IdentityDocument document);
//
//    public abstract IdentityDocument fromIdentityDocumentDto(IdentityDocumentDto dto);
//
//    public abstract RegionDto toRegionDto(Region region);
//
//    @Mapping(target = "region", expression = "java(toRegionDto(address.getRegion()))")
//    public abstract AddressResponseDto toAddressDto(Address address);
//
//    @Mapping(target = "region", expression = "java(regionService.findById(dto.getRegionId()))")
//    public abstract Address fromAddressDto(AddressRequestDto dto);
//
//    public Set<Address> fromAddressDtoList(List<AddressRequestDto> dto) {
//        return dto != null ? dto.stream().map(v -> addressService.findByCityAndStreetAndNumberAndRegionId(v.getCity(),v.getStreet(),
//                v.getNumber(), v.getRegionId()).orElse(fromAddressDto(v))).collect(Collectors.toSet()) : null;
//    }
//
//    public abstract ContactDto toContactDto(Contact contact);
//
//    public abstract Contact fromContactDto(ContactDto dto);
}