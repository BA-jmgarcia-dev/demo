package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.demo.models.Cuenta;
import com.example.demo.models.TransaccionDto;
import com.example.demo.services.CuentaService;

@RestController
@RequestMapping("/api/cuentas")
public class CuentaController {
    

    @Autowired
    private CuentaService cuentaService;


    @GetMapping("/{id}")
    @ResponseStatus(OK)
    public Cuenta detalle(@PathVariable Integer id){
        return cuentaService.findById(id);
    }
    
    @PostMapping("transaction")
    public ResponseEntity<?> transaction(@RequestBody TransaccionDto transaccionDto){

        cuentaService.transferir(transaccionDto.getIdBanco(), transaccionDto.getCuentaOrigenId(), 
            transaccionDto.getCuentaDestinoId(), transaccionDto.getMonto());
        Map<String, Object> response = new HashMap<>();
        response.put("date", LocalDate.now().toString());
        response.put("status", "ok");
        response.put("mensaje", "transferencia realizada con exito");
        response.put("transaccion", transaccionDto);
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("create-account")
    public ResponseEntity<?> crear(@RequestBody Cuenta cuenta){

        Cuenta cuentaCreada = cuentaService.createCuenta(cuenta);
        
        Map<String, Object> response = new HashMap<>();
        response.put("date", LocalDate.now().toString());
        response.put("status", "ok");
        response.put("mensaje", "cuenta creada con exito");
        response.put("cuenta", cuentaCreada);
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("cuentas")
    @ResponseStatus(OK)
    public List<Cuenta> cuentas(){
        return cuentaService.findAll();
    }
}
