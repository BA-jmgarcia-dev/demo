package com.example.demo.repo;


import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.models.Banco;

public interface BancoRepo extends JpaRepository<Banco, Integer> { 

}
