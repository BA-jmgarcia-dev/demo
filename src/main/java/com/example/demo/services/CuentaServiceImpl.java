package com.example.demo.services;

import java.math.BigDecimal;

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
    public Cuenta findById(Long id) {
        return cuentaRepo.findById(id);
    }

    @Override
    public int revisarTotalTransferencia(Long idBanco) {
        Banco banco = bancoRepo.findById(idBanco);
        return banco.getTotalTransferencias();
    }

    @Override
    public BigDecimal revisarSaldo(Long idCuenta) {
        Cuenta cuenta = cuentaRepo.findById(idCuenta);
        return cuenta.getSaldo();
    }

    @Override
    public void transferir(Long idBanco, Long idCuentaOrigen, Long idCuentaDestino, BigDecimal monto) {
        Cuenta cuentaOrigen = cuentaRepo.findById(idCuentaOrigen);
        cuentaOrigen.debito(monto);
        cuentaRepo.update(cuentaOrigen);

        Cuenta cuentaDestino = cuentaRepo.findById(idCuentaDestino);
        cuentaDestino.credito(monto);
        cuentaRepo.update(cuentaDestino);

        Banco banco = bancoRepo.findById(idBanco);
        int totalTransferencias = banco.getTotalTransferencias();
        banco.setTotalTransferencias(++totalTransferencias);
        bancoRepo.update(banco);
    }
    
}
