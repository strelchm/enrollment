package ru.strelchm.enrollment.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.strelchm.enrollment.domain.ShopUnit;
import ru.strelchm.enrollment.domain.ShopUnitStatistics;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface ShopUnitStatisticsRepository extends JpaRepository<ShopUnitStatistics, UUID> {
  List<ShopUnitStatistics> findAllByUnit(ShopUnit unit); // todo sorting?
}
