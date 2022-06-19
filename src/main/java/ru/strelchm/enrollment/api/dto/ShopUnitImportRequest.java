package ru.strelchm.enrollment.api.dto;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import ru.strelchm.enrollment.api.dto.ShopUnitImport;
import java.time.OffsetDateTime;
import javax.validation.Valid;
import javax.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import javax.annotation.Generated;

/**
 * ShopUnitImportRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2022-06-19T17:47:30.973756+07:00[Asia/Novosibirsk]")
public class ShopUnitImportRequest {

  @JsonProperty("items")
  @Valid
  private List<ShopUnitImport> items = null;

  @JsonProperty("updateDate")
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime updateDate;

  public ShopUnitImportRequest items(List<ShopUnitImport> items) {
    this.items = items;
    return this;
  }

  public ShopUnitImportRequest addItemsItem(ShopUnitImport itemsItem) {
    if (this.items == null) {
      this.items = new ArrayList<>();
    }
    this.items.add(itemsItem);
    return this;
  }

  /**
   * Импортируемые элементы
   * @return items
  */
  @Valid 
  @Schema(name = "items", description = "Импортируемые элементы", required = false)
  public List<ShopUnitImport> getItems() {
    return items;
  }

  public void setItems(List<ShopUnitImport> items) {
    this.items = items;
  }

  public ShopUnitImportRequest updateDate(OffsetDateTime updateDate) {
    this.updateDate = updateDate;
    return this;
  }

  /**
   * Время обновления добавляемых товаров/категорий.
   * @return updateDate
  */
  @Valid 
  @Schema(name = "updateDate", example = "2022-05-28T21:12:01Z", description = "Время обновления добавляемых товаров/категорий.", required = false)
  public OffsetDateTime getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(OffsetDateTime updateDate) {
    this.updateDate = updateDate;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ShopUnitImportRequest shopUnitImportRequest = (ShopUnitImportRequest) o;
    return Objects.equals(this.items, shopUnitImportRequest.items) &&
        Objects.equals(this.updateDate, shopUnitImportRequest.updateDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(items, updateDate);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ShopUnitImportRequest {\n");
    sb.append("    items: ").append(toIndentedString(items)).append("\n");
    sb.append("    updateDate: ").append(toIndentedString(updateDate)).append("\n");
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

