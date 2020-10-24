package com.vaultionizer.vaultserver.resource;

import com.vaultionizer.vaultserver.model.SpaceModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpaceRepository extends JpaRepository<SpaceModel, Long> {
}
