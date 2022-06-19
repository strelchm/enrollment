package ru.strelchm.enrollment.api.impl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.strelchm.enrollment.api.ImportsApi;
import ru.strelchm.enrollment.api.dto.Error;
import ru.strelchm.enrollment.api.dto.ShopUnitImportRequest;
import ru.strelchm.enrollment.domain.ShopUnit;
import ru.strelchm.enrollment.mapper.ImportsMapper;
import ru.strelchm.enrollment.service.ShopUnitService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Validated
@RestController("/imports")
@RequestMapping("/imports")
@Tag(name = "imports", description = "the imports API")
public class ImportsController {

  private final ShopUnitService importsService;
  private final ImportsMapper importsMapper;

  @Autowired
  public ImportsController(ShopUnitService importsService, ImportsMapper importsMapper) {
    this.importsService = importsService;
    this.importsMapper = importsMapper;
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
  @PostMapping(produces = {"application/json"}, consumes = {"application/json"})
  public void importsPost(
      @Parameter(name = "ShopUnitImportRequest") @Valid @RequestBody(required = false) ShopUnitImportRequest unitImportRequest
  ) {
    List<ShopUnit> shopUnits = unitImportRequest.getItems().stream()
        .map(importsMapper::toShopUnit)
        .collect(Collectors.toList());
    importsService.importItems(shopUnits, unitImportRequest.getUpdateDate());
  }
}
