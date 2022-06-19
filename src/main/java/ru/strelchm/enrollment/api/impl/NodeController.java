package ru.strelchm.enrollment.api.impl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.strelchm.enrollment.api.NodeApi;
import ru.strelchm.enrollment.api.dto.Error;
import ru.strelchm.enrollment.api.dto.ShopUnitStatisticResponse;
import ru.strelchm.enrollment.mapper.ImportsMapper;
import ru.strelchm.enrollment.service.ShopUnitService;

import javax.validation.Valid;
import java.time.OffsetDateTime;
import java.util.UUID;
import java.util.stream.Collectors;

@Validated
@RestController("/node")
@RequestMapping("/node")
@Tag(name = "node", description = "the node API")
public class NodeController implements NodeApi {
  private final ShopUnitService importsService;
  private final ImportsMapper importsMapper;

  @Autowired
  public NodeController(ShopUnitService importsService, ImportsMapper importsMapper) {
    this.importsService = importsService;
    this.importsMapper = importsMapper;
  }

  @Override
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
  @GetMapping(value = "/{id}/statistic", produces = {"application/json"})
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
        importsService.nodeIdStatisticGet(id).stream()
            .map(importsMapper::toShopUnitStatisticUnitDto)
            .collect(Collectors.toList())
    );
  }
}
