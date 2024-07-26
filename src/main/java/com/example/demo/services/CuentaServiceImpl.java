package com.example.demo.services;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.models.Banco;
import com.example.demo.models.Cuenta;
import com.example.demo.repo.BancoRepo;
import com.example.demo.repo.CuentaRepo;

@Service
public class CuentaServiceImpl implements CuentaService {

    private CuentaRepo cuentaRepo;
    private BancoRepo bancoRepo;

    public CuentaServiceImpl(CuentaRepo cuentaRepo, BancoRepo bancoRepo){
        this.cuentaRepo = cuentaRepo;
        this.bancoRepo = bancoRepo;
    }


    @Override
    public Cuenta findById(Integer id) {
        return cuentaRepo.findById(id).orElseThrow();
    }

    @Override
    public int revisarTotalTransferencia(Integer idBanco) {
        Banco banco = bancoRepo.findById(idBanco).orElseThrow();
        return banco.getTotalTransferencias();
    }

    @Override
    public BigDecimal revisarSaldo(Integer idCuenta) {
        Cuenta cuenta = cuentaRepo.findById(idCuenta).orElseThrow();
        return cuenta.getSaldo();
    }

    @Override
    public void transferir(Integer idBanco, Integer idCuentaOrigen, Integer idCuentaDestino, BigDecimal monto) {
        Cuenta cuentaOrigen = cuentaRepo.findById(idCuentaOrigen).orElseThrow();
        cuentaOrigen.debito(monto);
        cuentaRepo.save(cuentaOrigen);

        Cuenta cuentaDestino = cuentaRepo.findById(idCuentaDestino).orElseThrow();
        cuentaDestino.credito(monto);
        cuentaRepo.save(cuentaDestino);

        Banco banco = bancoRepo.findById(idBanco).orElseThrow();
        int totalTransferencias = banco.getTotalTransferencias();
        banco.setTotalTransferencias(++totalTransferencias);
        bancoRepo.save(banco);
    }


    public Cuenta createCuenta(Cuenta cuenta) {
        return cuentaRepo.save(cuenta);
    }


    public List<Cuenta> findAll() {
        return cuentaRepo.findAll();
    }
    
}
