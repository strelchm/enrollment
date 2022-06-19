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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.strelchm.enrollment.api.SalesApi;
import ru.strelchm.enrollment.api.dto.Error;
import ru.strelchm.enrollment.api.dto.ShopUnitStatisticResponse;
import ru.strelchm.enrollment.mapper.ImportsMapper;
import ru.strelchm.enrollment.service.ShopUnitService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

@RestController("/sales")
@RequestMapping("/sales")
@Validated
@Tag(name = "sales", description = "the sales API")
public class SalesController {
  private final ShopUnitService importsService;
  private final ImportsMapper importsMapper;

  @Autowired
  public SalesController(ShopUnitService importsService, ImportsMapper importsMapper) {
    this.importsService = importsService;
    this.importsMapper = importsMapper;
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
  @GetMapping(produces = {"application/json"})
  public ShopUnitStatisticResponse salesGet(
      @NotNull @Parameter(name = "date", description = "Дата и время запроса. " +
          "Дата должна обрабатываться согласно ISO 8601 (такой придерживается OpenAPI). " +
          "Если дата не удовлетворяет данному формату, необходимо отвечать 400", required = true) @Valid
      @RequestParam(value = "date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime date
  ) {
    return new ShopUnitStatisticResponse().items(
        importsService.salesGet(date.minus(24, ChronoUnit.HOURS), date).stream()
            .map(importsMapper::toShopUnitStatisticUnitDto)
            .collect(Collectors.toList())
    );
  }
}
