package ru.strelchm.enrollment.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.strelchm.enrollment.domain.ShopUnit;
import ru.strelchm.enrollment.domain.ShopUnitStatistics;

import java.util.List;
import java.util.UUID;

@Repository
public interface ShopUnitStatisticsRepository extends JpaRepository<ShopUnitStatistics, UUID>, JpaSpecificationExecutor<ShopUnitStatistics> {
}
