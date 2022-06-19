package ru.strelchm.enrollment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.strelchm.enrollment.api.dto.ShopUnitType;
import ru.strelchm.enrollment.dao.ShopUnitRepository;
import ru.strelchm.enrollment.dao.ShopUnitStatisticsRepository;
import ru.strelchm.enrollment.domain.ShopUnit;
import ru.strelchm.enrollment.domain.ShopUnitStatistics;
import ru.strelchm.enrollment.exception.BadRequestException;
import ru.strelchm.enrollment.exception.NotFoundException;

import javax.persistence.criteria.Predicate;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ShopUnitService {
  private final ShopUnitRepository shopUnitRepository;
  private final ShopUnitStatisticsRepository shopUnitStatisticsRepository;

  @Autowired
  public ShopUnitService(ShopUnitRepository shopUnitRepository, ShopUnitStatisticsRepository shopUnitStatisticsRepository) {
    this.shopUnitRepository = shopUnitRepository;
    this.shopUnitStatisticsRepository = shopUnitStatisticsRepository;
  }

  @Transactional(readOnly = true)
  public ShopUnit getById(UUID id) {
    return shopUnitRepository.findById(id).orElseThrow(NotFoundException::new);
  }

  @Transactional
  public void importItems(List<ShopUnit> shopUnits, OffsetDateTime updateDate) {
    Set<UUID> existedUnitIds = new HashSet<>();
    List<UUID> parentUnitIds = new ArrayList<>();
    Set<UUID> recalculationCategories = new HashSet<>();

    shopUnits.forEach(v -> {
      if (v.getId() != null) {
        existedUnitIds.add(v.getId());
      }
      if (v.getParent() != null) {
        parentUnitIds.add(v.getParent().getId());
      }
    });

    if (shopUnits.size() != existedUnitIds.size()) {
      throw new BadRequestException();
    }

    Map<UUID, ShopUnit> existedUnits = shopUnitRepository.findAllByIdIn(
            Stream.concat(existedUnitIds.stream(), parentUnitIds.stream()).collect(Collectors.toList())
        ).stream()
        .collect(Collectors.toMap(ShopUnit::getId, v -> v));

    for (ShopUnit unit : shopUnits) {
      ShopUnit current = existedUnits.get(unit.getId());
      if (current != null && current.getType() != unit.getType()) {
        throw new BadRequestException();
      }

      if (unit.getType() == ShopUnitType.CATEGORY && unit.getPrice() != null) {
        throw new BadRequestException();
      }

      if (unit.getType() == ShopUnitType.OFFER && (unit.getPrice() == null || unit.getPrice() <= 0)) {
        throw new BadRequestException();
      }

      if (unit.getParent() != null) {
        ShopUnit parent = existedUnits.get(unit.getParent().getId());
        if (parent == null) {
          parent = shopUnits.stream()
              .filter(v -> v.getId().equals(unit.getParent().getId()))
              .findFirst()
              .orElseThrow(BadRequestException::new);
        }
        unit.setParent(parent);
        if (parent.getChildren() == null) {
          parent.setChildren(new ArrayList<>());
        }
        parent.getChildren().add(unit);
        shopUnitRepository.save(parent);
      }

      unit.setUpdated(updateDate);

      shopUnitRepository.save(unit);

      if (unit.getType() == ShopUnitType.OFFER) {
        fillRecalculationCategories(unit.getParent(), recalculationCategories);
      }
    }

    for (ShopUnit unit : shopUnitRepository.findAllByIdIn(recalculationCategories)) {
      List<ShopUnit> offers = new ArrayList<>();
      fillOffers(unit, offers);
      if (!offers.isEmpty()) {
        unit.setPrice((long) offers.stream().mapToLong(ShopUnit::getPrice).average().orElseThrow(RuntimeException::new));
        unit.setUpdated(updateDate);
      }
      shopUnitRepository.save(unit);
    }
  }

  public void fillOffers(ShopUnit root, List<ShopUnit> offers) {
    List<ShopUnit> children = root.getChildren();
    if (children == null) {
      return;
    }
    for (ShopUnit child : children) {
      if (child.getType() == ShopUnitType.OFFER) {
        offers.add(child);
      } else {
        fillOffers(child, offers);
      }
    }
  }

  public void fillRecalculationCategories(ShopUnit parent, Collection<UUID> recalculationCategories) {
    while (parent != null) {
      if (parent.getType() == ShopUnitType.CATEGORY) {
        recalculationCategories.add(parent.getId());
      }
      parent = parent.getParent();
    }
  }

  @Transactional
  public void deleteById(UUID id) {
    shopUnitRepository.delete(getById(id));
  }

  @Transactional(readOnly = true)
  public List<ShopUnit> salesGet(OffsetDateTime from, OffsetDateTime to) {
    return shopUnitRepository.findAll((root, query, cb) -> {
      List<Predicate> predicates = new ArrayList<>();
      if (from != null) {
        predicates.add(cb.greaterThanOrEqualTo(root.get("updated"), from));
      }
      if (to != null) {
        predicates.add(cb.lessThanOrEqualTo(root.get("updated"), to));
      }
      predicates.add(cb.equal(root.get("type"), ShopUnitType.OFFER));
      return cb.and(predicates.toArray(new Predicate[0]));
    });
  }

  @Transactional(readOnly = true)
  public List<ShopUnitStatistics> nodeIdStatisticGet(UUID id) {
    return shopUnitStatisticsRepository.findAllByUnit(getById(id));
  }
}
