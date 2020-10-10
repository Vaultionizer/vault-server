package com.vaultionizer.vaultserver.resource;

import com.vaultionizer.vaultserver.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserModel, Long> {

}
