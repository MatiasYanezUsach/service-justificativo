package proyecto.mingeso.microservicejustificativo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import proyecto.mingeso.microservicejustificativo.entities.JustificativoEntity;
import proyecto.mingeso.microservicejustificativo.repositories.JustificativoRepository;
import proyecto.mingeso.microservicejustificativo.services.JustificativoService;

import java.util.ArrayList;

@RestController
@RequestMapping("/justificativo")
public class JustificativoController {
    @Autowired
    JustificativoService justificativoService;
    @Autowired
    JustificativoRepository justificativoRepository;

    @PostMapping("/subirJustificativo")
    public ResponseEntity<JustificativoEntity> subirJustificativo(@RequestBody JustificativoEntity justificativo) {
        int cantidadJustificados = 0;
        ArrayList<JustificativoEntity> justificativos = justificativoRepository.findByRut(justificativo.getRut_empleado());
        for(JustificativoEntity Justificados : justificativos) {
            cantidadJustificados++;
        }
        for(int i = 0; i< cantidadJustificados;i++){
            if(justificativos.get(i).getFecha_cubridora().equals(justificativo.getFecha_cubridora())){
                return ResponseEntity.badRequest().build();
            }
        }
        JustificativoEntity justificativoFinal = justificativoService.crearJustificativo(justificativo.getRut_empleado(), justificativo.getFecha_cubridora());
        if(justificativoFinal == null){
            return ResponseEntity.badRequest().build();
        }
        else {
            JustificativoEntity justificado = justificativoService.guardarJustificativo(justificativoFinal);
            return ResponseEntity.ok(justificado);
        }
    }
}