package com.example.biblo.domain.models;

import com.example.biblo.application.dto.AutorDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@Table(name="autor")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String fechanacimiento;
    private String fechafallecimiento;

    public Autor(){}

    public Autor(Long id, String nombre, String fechanacimiento, String fechafallecimiento) {
        this.id = id;
        this.nombre = nombre;
        this.fechanacimiento = fechanacimiento;
        this.fechafallecimiento = fechafallecimiento;
    }

    public Autor(AutorDTO autor){

        this.nombre= autor.nombre();
        this.fechanacimiento=autor.fechaNacimiento();
        this.fechafallecimiento= autor.fechaFallecimiento();

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFechanacimiento() {
        return fechanacimiento;
    }

    public void setFechanacimiento(String fechanacimiento) {
        this.fechanacimiento = fechanacimiento;
    }

    public String getFechafallecimiento() {
        return fechafallecimiento;
    }

    public void setFechafallecimiento(String fechafallecimiento) {
        this.fechafallecimiento = fechafallecimiento;
    }


}
