package ru.strelchm.enrollment.api;

import ru.strelchm.enrollment.api.dto.ShopUnitStatisticResponse;

import java.time.OffsetDateTime;

public interface SalesApi {
  /**
   * GET /sales
   * Получение списка **товаров**, цена которых была обновлена за последние 24 часа включительно
   * [now() - 24h, now()] от времени переданном в запросе.
   * Обновление цены не означает её изменение.
   * Обновления цен удаленных товаров недоступны.
   *
   * При обновлении цены товара, средняя цена категории, которая содержит этот товар, тоже обновляется.
   *
   * @param date Дата и время запроса. Дата должна обрабатываться согласно ISO 8601 (такой придерживается OpenAPI).
   *             Если дата не удовлетворяет данному формату, необходимо отвечать 400 (required)
   * @return Список товаров, цена которых была обновлена. (status code 200)
   * or Невалидная схема документа или входные данные не верны. (status code 400)
   */
  ShopUnitStatisticResponse salesGet(OffsetDateTime date);
}
