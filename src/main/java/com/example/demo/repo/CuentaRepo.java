package com.example.demo.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.models.Cuenta;

public interface CuentaRepo extends JpaRepository<Cuenta, Integer>{

    @Query("select c from Cuenta c where c.persona=:persona")
    Optional<Cuenta> findByPersona(String persona);
}
