package ru.strelchm.enrollment.api.impl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.strelchm.enrollment.api.dto.Error;
import ru.strelchm.enrollment.api.dto.ShopUnitImportRequest;
import ru.strelchm.enrollment.api.dto.ShopUnitStatisticResponse;
import ru.strelchm.enrollment.domain.ShopUnit;
import ru.strelchm.enrollment.mapper.ShopUnitMapper;
import ru.strelchm.enrollment.service.ShopUnitService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Validated
@RestController
@RequestMapping
public class ShopUnitController {

  private final ShopUnitService shopUnitService;
  private final ShopUnitMapper shopUnitMapper;

  @Autowired
  public ShopUnitController(ShopUnitService importsService, ShopUnitMapper shopUnitMapper) {
    this.shopUnitService = importsService;
    this.shopUnitMapper = shopUnitMapper;
  }

  @Operation(
      operationId = "importsPost",
      tags = {"Базовые задачи"},
      responses = {
          @ApiResponse(responseCode = "200", description = "Вставка или обновление прошли успешно."),
          @ApiResponse(responseCode = "400", description = "Невалидная схема документа или входные данные не верны.", content = {
              @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))
          })
      }
  )
  @PostMapping(value = "/imports", produces = {"application/json"}, consumes = {"application/json"})
  public void importsPost(
      @Parameter(name = "ShopUnitImportRequest") @Valid @RequestBody(required = false) ShopUnitImportRequest unitImportRequest
  ) {
    List<ShopUnit> shopUnits = unitImportRequest.getItems().stream()
        .map(shopUnitMapper::toShopUnit)
        .collect(Collectors.toList());
    shopUnitService.importItems(shopUnits, unitImportRequest.getUpdateDate());
  }

  @Operation(
      operationId = "deleteIdDelete",
      tags = {"Базовые задачи"},
      responses = {
          @ApiResponse(responseCode = "200", description = "Удаление прошло успешно."),
          @ApiResponse(responseCode = "400", description = "Невалидная схема документа или входные данные не верны.", content = {
              @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))
          }),
          @ApiResponse(responseCode = "404", description = "Категория/товар не найден.", content = {
              @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))
          })
      }
  )
  @DeleteMapping(value = "/delete/{id}", produces = {"application/json"})
  public void deleteIdDelete(
      @Parameter(name = "id", description = "Идентификатор", required = true) @PathVariable("id") UUID id
  ) {
    shopUnitService.deleteById(id);
  }

  @Operation(
      operationId = "nodeIdStatisticGet",
      tags = {"Дополнительные задачи"},
      responses = {
          @ApiResponse(responseCode = "200", description = "Статистика по элементу.", content = {
              @Content(mediaType = "application/json", schema = @Schema(implementation = ShopUnitStatisticResponse.class))
          }),
          @ApiResponse(responseCode = "400", description = "Некорректный формат запроса или некорректные даты интервала.", content = {
              @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))
          }),
          @ApiResponse(responseCode = "404", description = "Категория/товар не найден.", content = {
              @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))
          })
      }
  )
  @GetMapping(value = "/node/{id}/statistic", produces = {"application/json"})
  public ShopUnitStatisticResponse nodeIdStatisticGet(
      @Parameter(name = "id", description = "UUID товара/категории для которой будет отображаться статистика", required = true)
      @PathVariable("id") UUID id,
      @Parameter(name = "dateStart", description = "Дата и время начала интервала, для которого считается статистика. " +
          "Дата должна обрабатываться согласно ISO 8601 (такой придерживается OpenAPI). " +
          "Если дата не удовлетворяет данному формату, необходимо отвечать 400.") @Valid
      @RequestParam(value = "dateStart", required = false)
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime dateStart,
      @Parameter(name = "dateEnd", description = "Дата и время конца интервала, для которого считается статистика. " +
          "Дата должна обрабатываться согласно ISO 8601 (такой придерживается OpenAPI). " +
          "Если дата не удовлетворяет данному формату, необходимо отвечать 400.")
      @Valid @RequestParam(value = "dateEnd", required = false)
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime dateEnd
  ) {
    return new ShopUnitStatisticResponse().items(
        shopUnitService.nodeIdStatisticGet(id, dateStart, dateEnd).stream()
            .map(shopUnitMapper::toShopUnitStatisticUnitDto)
            .collect(Collectors.toList())
    );
  }

  @Operation(
      operationId = "nodesIdGet",
      tags = {"Базовые задачи"},
      responses = {
          @ApiResponse(responseCode = "200", description = "Информация об элементе.", content = {
              @Content(mediaType = "application/json", schema = @Schema(implementation = ru.strelchm.enrollment.api.dto.ShopUnit.class))
          }),
          @ApiResponse(responseCode = "400", description = "Невалидная схема документа или входные данные не верны.", content = {
              @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))
          }),
          @ApiResponse(responseCode = "404", description = "Категория/товар не найден.", content = {
              @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))
          })
      }
  )
  @GetMapping(value = "/nodes/{id}", produces = {"application/json"})
  public ru.strelchm.enrollment.api.dto.ShopUnit nodesIdGet(
      @Parameter(name = "id", description = "Идентификатор элемента", required = true) @PathVariable("id") UUID id
  ) {
    return shopUnitMapper.toShopUnitDto(shopUnitService.getById(id));
  }

  @Operation(
      operationId = "salesGet",
      tags = {"Дополнительные задачи"},
      responses = {
          @ApiResponse(responseCode = "200", description = "Список товаров, цена которых была обновлена.", content = {
              @Content(mediaType = "application/json", schema = @Schema(implementation = ShopUnitStatisticResponse.class))
          }),
          @ApiResponse(responseCode = "400", description = "Невалидная схема документа или входные данные не верны.", content = {
              @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))
          })
      }
  )
  @GetMapping(value = "/sales", produces = {"application/json"})
  public ShopUnitStatisticResponse salesGet(
      @NotNull @Parameter(name = "date", description = "Дата и время запроса. " +
          "Дата должна обрабатываться согласно ISO 8601 (такой придерживается OpenAPI). " +
          "Если дата не удовлетворяет данному формату, необходимо отвечать 400", required = true) @Valid
      @RequestParam(value = "date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
          OffsetDateTime date // todo - date is not iso
  ) {
    return new ShopUnitStatisticResponse().items(
        shopUnitService.salesGet(date.minus(24, ChronoUnit.HOURS), date).stream()
            .map(shopUnitMapper::toShopUnitStatisticUnitDto)
            .collect(Collectors.toList())
    );
  }
}
