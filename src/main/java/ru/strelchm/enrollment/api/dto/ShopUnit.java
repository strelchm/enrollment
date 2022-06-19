package ru.strelchm.enrollment.api.dto;

import java.net.URI;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import org.springframework.format.annotation.DateTimeFormat;
import ru.strelchm.enrollment.api.dto.ShopUnitType;
import java.time.OffsetDateTime;
import javax.validation.Valid;
import javax.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import javax.annotation.Generated;

/**
 * ShopUnit
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2022-06-19T17:47:30.973756+07:00[Asia/Novosibirsk]")
public class ShopUnit {

  @JsonProperty("id")
  private UUID id;

  @JsonProperty("name")
  private String name;

  @JsonProperty("date")
  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
  private OffsetDateTime date;

  @JsonProperty("parentId")
  private UUID parentId = null;

  @JsonProperty("type")
  private ShopUnitType type;

  @JsonProperty("price")
  private Long price = null;

  @JsonProperty("children")
  @Valid
  private List<ShopUnit> children = null;

  public ShopUnit id(UUID id) {
    this.id = id;
    return this;
  }

  /**
   * Уникальный идентфикатор
   * @return id
  */
  @NotNull @Valid 
  @Schema(name = "id", example = "3fa85f64-5717-4562-b3fc-2c963f66a333", description = "Уникальный идентфикатор", required = true)
  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public ShopUnit name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Имя категории
   * @return name
  */
  @NotNull 
  @Schema(name = "name", description = "Имя категории", required = true)
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public ShopUnit date(OffsetDateTime date) {
    this.date = date;
    return this;
  }

  /**
   * Время последнего обновления элемента.
   * @return date
  */
  @NotNull @Valid 
  @Schema(name = "date", example = "2022-05-28T21:12:01Z", description = "Время последнего обновления элемента.", required = true)
  public OffsetDateTime getDate() {
    return date;
  }

  public void setDate(OffsetDateTime date) {
    this.date = date;
  }

  public ShopUnit parentId(UUID parentId) {
    this.parentId = parentId;
    return this;
  }

  /**
   * UUID родительской категории
   * @return parentId
  */
  @Valid 
  @Schema(name = "parentId", example = "3fa85f64-5717-4562-b3fc-2c963f66a333", description = "UUID родительской категории", required = false)
  public UUID getParentId() {
    return parentId;
  }

  public void setParentId(UUID parentId) {
    this.parentId = parentId;
  }

  public ShopUnit type(ShopUnitType type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
  */
  @NotNull @Valid 
  @Schema(name = "type", required = true)
  public ShopUnitType getType() {
    return type;
  }

  public void setType(ShopUnitType type) {
    this.type = type;
  }

  public ShopUnit price(Long price) {
    this.price = price;
    return this;
  }

  /**
   * Целое число, для категории - это средняя цена всех дочерних товаров(включая товары подкатегорий). Если цена является не целым числом, округляется в меньшую сторону до целого числа. Если категория не содержит товаров цена равна null.
   * @return price
  */
  
  @Schema(name = "price", description = "Целое число, для категории - это средняя цена всех дочерних товаров(включая товары подкатегорий). Если цена является не целым числом, округляется в меньшую сторону до целого числа. Если категория не содержит товаров цена равна null.", required = false)
  public Long getPrice() {
    return price;
  }

  public void setPrice(Long price) {
    this.price = price;
  }

  public ShopUnit children(List<ShopUnit> children) {
    this.children = children == null || children.isEmpty() ? null : children;
    return this;
  }

  public ShopUnit addChildrenItem(ShopUnit childrenItem) {
    if (this.children == null) {
      this.children = new ArrayList<>();
    }
    this.children.add(childrenItem);
    return this;
  }

  /**
   * Список всех дочерних товаров\\категорий. Для товаров поле равно null.
   * @return children
  */
  @Valid 
  @Schema(name = "children", description = "Список всех дочерних товаров\\категорий. Для товаров поле равно null.", required = false)
  public List<ShopUnit> getChildren() {
    return children;
  }

  public void setChildren(List<ShopUnit> children) {
    this.children = children;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ShopUnit shopUnit = (ShopUnit) o;
    return Objects.equals(this.id, shopUnit.id) &&
        Objects.equals(this.name, shopUnit.name) &&
        Objects.equals(this.date, shopUnit.date) &&
        Objects.equals(this.parentId, shopUnit.parentId) &&
        Objects.equals(this.type, shopUnit.type) &&
        Objects.equals(this.price, shopUnit.price) &&
        Objects.equals(this.children, shopUnit.children);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, date, parentId, type, price, children);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ShopUnit {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    date: ").append(toIndentedString(date)).append("\n");
    sb.append("    parentId: ").append(toIndentedString(parentId)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    price: ").append(toIndentedString(price)).append("\n");
    sb.append("    children: ").append(toIndentedString(children)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

