package ru.strelchm.enrollment.dao;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.strelchm.enrollment.domain.ShopUnit;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface ShopUnitRepository extends JpaRepository<ShopUnit, UUID>, JpaSpecificationExecutor<ShopUnit> {
  Set<ShopUnit> findAllByIdIn(Collection<UUID> ids);

  @EntityGraph(attributePaths = {"children"})
  Optional<ShopUnit> findById(UUID id);
}
