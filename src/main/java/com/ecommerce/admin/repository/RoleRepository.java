package com.ecommerce.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.admin.entity.Roles;

@Repository
public interface RoleRepository extends JpaRepository<Roles, Integer>{

}
