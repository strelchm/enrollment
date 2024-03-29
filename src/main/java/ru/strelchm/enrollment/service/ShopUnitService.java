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
import ru.strelchm.enrollment.mapper.ShopUnitMapper;

import javax.persistence.criteria.Predicate;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ShopUnitService {
  private final ShopUnitRepository shopUnitRepository;
  private final ShopUnitStatisticsRepository shopUnitStatisticsRepository;
  private final ShopUnitMapper shopUnitMapper;

  @Autowired
  public ShopUnitService(ShopUnitMapper shopUnitMapper, ShopUnitRepository shopUnitRepository,
                         ShopUnitStatisticsRepository shopUnitStatisticsRepository) {
    this.shopUnitMapper = shopUnitMapper;
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
    Set<ShopUnit> recalculationCategories = new HashSet<>();
    Set<ShopUnitStatistics> shopUnitStatistics = new HashSet<>();

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

      if (unit.getType() == ShopUnitType.CATEGORY && unit.getPrice() != null) {
        throw new BadRequestException();
      }

      if (current != null) {
        if (current.getType() != unit.getType()) {
          throw new BadRequestException();
        }
        if (current.getType() == ShopUnitType.CATEGORY) {
          unit.setPrice(current.getPrice());
        }
        if (!statEquality(current, unit)) {
          shopUnitStatistics.add(shopUnitMapper.toShopUnitStatistics(current));
        }
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
        if (!updateExistedChild(unit, parent)) {
          parent.getChildren().add(unit);
        }
        shopUnitRepository.save(parent);
      }

      unit.setUpdated(updateDate);

      shopUnitRepository.save(unit);

      if (unit.getType() == ShopUnitType.OFFER) {
        fillRecalculationCategories(unit.getParent(), recalculationCategories);
      }
    }

    shopUnitStatisticsRepository.saveAll(shopUnitStatistics);
    recalculationCategories.forEach(unit -> recalculateParent(updateDate, unit));
  }

  private boolean statEquality(ShopUnit unit, ShopUnit parent) {
    UUID unitId = Optional.ofNullable(unit).map(ShopUnit::getId).orElse(null);
    UUID parentId = Optional.ofNullable(parent).map(ShopUnit::getId).orElse(null);
    return unit.getName().equals(parent.getName()) && Objects.equals(unitId, parentId) &&
        (unit.getType() == ShopUnitType.CATEGORY || unit.getPrice().equals(parent.getPrice()));
  }

  private boolean updateExistedChild(ShopUnit unit, ShopUnit parent) {
    ListIterator<ShopUnit> iterator = parent.getChildren().listIterator();
    boolean existed = false;
    while (iterator.hasNext()) {
      ShopUnit next = iterator.next();
      if (next.getId().equals(unit.getId())) {
        iterator.set(unit);
        existed = true;
        break;
      }
    }
    return existed;
  }

  private void recalculateParent(OffsetDateTime updateDate, ShopUnit unit) {
    List<ShopUnit> offers = new ArrayList<>();
    fillOffers(unit, offers);
    if (!offers.isEmpty()) {
      OptionalDouble avg = offers.stream().mapToLong(ShopUnit::getPrice).average();
      if (avg.isPresent()) {
        unit.setPrice((long) avg.getAsDouble());
      }
      if (updateDate != null) {
        unit.setUpdated(updateDate);
      }
    } else {
      unit.setPrice(null);
    }
    shopUnitRepository.save(unit);
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

  public void fillRecalculationCategories(ShopUnit parent, Collection<ShopUnit> recalculationCategories) {
    while (parent != null) {
      if (parent.getType() == ShopUnitType.CATEGORY) {
        recalculationCategories.add(parent);
      }
      parent = parent.getParent();
    }
  }

  @Transactional
  public void deleteById(UUID id) {
    ShopUnit unit = getById(id);
    ShopUnit parent = unit.getParent();

    shopUnitRepository.delete(unit);
    Optional.ofNullable(parent).ifPresent(p -> p.getChildren().removeIf(v -> v.getId().equals(id)));

    while (parent != null) {
      if (parent.getType() == ShopUnitType.CATEGORY) {
        recalculateParent(null, parent);
      }
      parent = parent.getParent();
    }
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
  public List<ShopUnitStatistics> nodeIdStatisticGet(UUID id, OffsetDateTime from, OffsetDateTime to) {
    ShopUnit unit = getById(id);

    List<ShopUnitStatistics> stat = shopUnitStatisticsRepository.findAll((root, query, cb) -> {
      List<Predicate> predicates = new ArrayList<>();
      if (from != null) {
        predicates.add(cb.greaterThanOrEqualTo(root.get("updated"), from));
      }
      if (to != null) {
        predicates.add(cb.lessThan(root.get("updated"), to));
      }
      predicates.add(cb.equal(root.join("unit").get("id"), id));

      query.orderBy(cb.desc(root.get("updated")), (cb.desc(root.get("created"))));

      return cb.and(predicates.toArray(new Predicate[0]));
    });

    if (((from == null || unit.getUpdated().isAfter(from) || unit.getUpdated().isEqual(from)) && (to == null || unit.getUpdated().isBefore(to)))) {
      ShopUnitStatistics currentStatItem = shopUnitMapper.toShopUnitStatistics(unit);
      currentStatItem.setId(unit.getId());
      stat.add(0, currentStatItem);
    }

    return stat;
  }
}
