package ru.strelchm.enrollment.api.impl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.strelchm.enrollment.api.DeleteApi;
import ru.strelchm.enrollment.api.dto.Error;
import ru.strelchm.enrollment.service.ShopUnitService;

import java.util.UUID;

@RestController("/delete")
@RequestMapping("/delete")
@Validated
@Tag(name = "delete", description = "the delete API")
public class DeleteController implements DeleteApi {
  private final ShopUnitService shopUnitService;

  @Autowired
  public DeleteController(ShopUnitService shopUnitService) {
    this.shopUnitService = shopUnitService;
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
  @Override
  @DeleteMapping(value = "/{id}", produces = {"application/json"})
  public void deleteIdDelete(
      @Parameter(name = "id", description = "Идентификатор", required = true) @PathVariable("id") UUID id
  ) {
    shopUnitService.deleteById(id);
  }
}
