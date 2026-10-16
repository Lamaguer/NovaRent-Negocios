// Domain Model 

package com.novarent.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Reservacion {

    private String folio;
    private String estado;
    private String clienteId;
    private String sucursalId;
    private String categoria;
    private PeriodoRenta periodo;

    public Reservacion(String folio,
                       String clienteId,
                       String sucursalId,
                       String categoria,
                       PeriodoRenta periodo) {

        this.folio = folio;
        this.clienteId = clienteId;
        this.sucursalId = sucursalId;
        this.categoria = categoria;
        this.periodo = periodo;
        this.estado = "PENDIENTE";
    }

    public void confirmar() {
        this.estado = "CONFIRMADA";
    }

    public String getFolio() {
        return folio;
    }
}

class Renta {

    private String idRenta;
    private String estado;
    private String reservacionId;
    private String clienteId;
    private String vehiculoId;
    private double tarifaOriginal;
    private String categoriaAsignada;
    private PeriodoRenta periodo;
    private List<Cargo> cargos;

    public Renta() {
        this.estado = "PENDIENTE";
        this.cargos = new ArrayList<>();
    }

    public void entregar(RequisitosEntrega requisitos,
                         EstadoVehiculo estadoInicial) {

        if (!requisitos.estanCompletos()) {
            throw new IllegalStateException(
                "RN-02: Requisitos de entrega incompletos."
            );
        }

        this.estado = "ENTREGADO";
    }

    public void extender(LocalDate nuevaFechaFin,
                         InventarioCategoria inventario) {

        PeriodoRenta nuevoPeriodo =
            new PeriodoRenta(
                periodo.getFechaInicio(),
                nuevaFechaFin
            );

        if (!inventario.hayCapacidad(nuevoPeriodo)) {
            throw new IllegalStateException(
                "RN-03: No existe disponibilidad para la extensión."
            );
        }

        this.periodo = nuevoPeriodo;
    }

    public void aplicarUpgrade(String categoriaSuperior) {

        this.categoriaAsignada = categoriaSuperior;

        // RN-01:
        // La tarifa original se conserva.
    }

    public void registrarDevolucion(
            EstadoVehiculo estadoFinal) {

        this.estado = "DEVUELTO";
    }
}

class InventarioCategoria {

    private String sucursalId;
    private String categoria;
    private int capacidad;

    public boolean hayCapacidad(
            PeriodoRenta periodo) {

        // BR-02 / US-03
        // Se validaría la ocupación real.

        return true;
    }

    public void apartarLugares(
            PeriodoRenta periodo) {

        // Descontar disponibilidad lógica.
    }
}