package com.example.biblo;

import com.example.biblo.domain.strutural.BridgeDemo;
import com.example.biblo.domain.strutural.LibroBridgeService;
import com.example.biblo.domain.strutural.NormalizadorAvanzado;
import com.example.biblo.domain.strutural.NormalizadorBasico;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class BibloApplication {

	public static void main(String[] args) {
        NormalizadorBasico normBasico = new NormalizadorBasico();

        //Prueba de Terminal de la Normalizacion de Texto con Patron Bridge
        NormalizadorAvanzado normAvanzado = new NormalizadorAvanzado();
        LibroBridgeService service = new LibroBridgeService(normBasico, normAvanzado);
        BridgeDemo demo = new BridgeDemo(service);

        // Ejecutar demo
        demo.ejecutarPruebas();
		SpringApplication.run(BibloApplication.class, args);
	}


}
