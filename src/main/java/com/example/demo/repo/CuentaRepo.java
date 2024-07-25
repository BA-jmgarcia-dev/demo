package com.example.demo.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.models.Cuenta;

public interface CuentaRepo extends JpaRepository<Cuenta, Integer>{

}
