package ru.strelchm.enrollment.api.impl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.strelchm.enrollment.api.NodesApi;
import ru.strelchm.enrollment.api.dto.Error;
import ru.strelchm.enrollment.api.dto.ShopUnit;
import ru.strelchm.enrollment.mapper.ImportsMapper;
import ru.strelchm.enrollment.service.ShopUnitService;

import java.util.UUID;

@RestController("/nodes")
@RequestMapping("/nodes")
@Validated
@Tag(name = "nodes", description = "the nodes API")
public class NodesController implements NodesApi {

  private ShopUnitService importsService;
  private ImportsMapper importsMapper;

  @Autowired
  public NodesController(ShopUnitService shopUnitService, ImportsMapper importsMapper) {
    this.importsService = shopUnitService;
    this.importsMapper = importsMapper;
  }

  @Operation(
      operationId = "nodesIdGet",
      tags = {"Базовые задачи"},
      responses = {
          @ApiResponse(responseCode = "200", description = "Информация об элементе.", content = {
              @Content(mediaType = "application/json", schema = @Schema(implementation = ShopUnit.class))
          }),
          @ApiResponse(responseCode = "400", description = "Невалидная схема документа или входные данные не верны.", content = {
              @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))
          }),
          @ApiResponse(responseCode = "404", description = "Категория/товар не найден.", content = {
              @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))
          })
      }
  )
  @GetMapping(value = "/{id}", produces = {"application/json"})
  @Override
  public ShopUnit nodesIdGet(
      @Parameter(name = "id", description = "Идентификатор элемента", required = true) @PathVariable("id") UUID id
  ) {
    return importsMapper.toShopUnitDto(importsService.getById(id)); // todo {agregating}
  }
}
