package ru.strelchm.enrollment.api.dto;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.ArrayList;
import java.util.List;
import ru.strelchm.enrollment.api.dto.ShopUnitStatisticUnit;
import java.time.OffsetDateTime;
import javax.validation.Valid;
import javax.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import javax.annotation.Generated;

/**
 * ShopUnitStatisticResponse
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2022-06-19T17:47:30.973756+07:00[Asia/Novosibirsk]")
public class ShopUnitStatisticResponse {

  @JsonProperty("items")
  @Valid
  private List<ShopUnitStatisticUnit> items = null;

  public ShopUnitStatisticResponse items(List<ShopUnitStatisticUnit> items) {
    this.items = items;
    return this;
  }

  public ShopUnitStatisticResponse addItemsItem(ShopUnitStatisticUnit itemsItem) {
    if (this.items == null) {
      this.items = new ArrayList<>();
    }
    this.items.add(itemsItem);
    return this;
  }

  /**
   * История в произвольном порядке.
   * @return items
  */
  @Valid 
  @Schema(name = "items", description = "История в произвольном порядке.", required = false)
  public List<ShopUnitStatisticUnit> getItems() {
    return items;
  }

  public void setItems(List<ShopUnitStatisticUnit> items) {
    this.items = items;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ShopUnitStatisticResponse shopUnitStatisticResponse = (ShopUnitStatisticResponse) o;
    return Objects.equals(this.items, shopUnitStatisticResponse.items);
  }

  @Override
  public int hashCode() {
    return Objects.hash(items);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ShopUnitStatisticResponse {\n");
    sb.append("    items: ").append(toIndentedString(items)).append("\n");
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

