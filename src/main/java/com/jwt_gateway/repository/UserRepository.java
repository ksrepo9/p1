package com.jwt_gateway.repository;

import com.jwt_gateway.entity.UserData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserData, Long> {
   UserData findByUsername(String username);


   @Query("SELECT u FROM UserData u LEFT JOIN FETCH u.userRoles ur LEFT JOIN FETCH ur.role WHERE u.username = :username")
   Optional<UserData> findByUsernameWithRoles(@Param("username") String username);
}
