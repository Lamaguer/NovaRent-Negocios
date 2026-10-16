// Application Service Layer

package com.novarent.application;

public class ServicioDeReservaciones {

    private final ReservacionRepository reservacionRepo;
    private final InventarioCategoriaRepository inventarioRepo;
    private final UnitOfWork unitOfWork;

    public ServicioDeReservaciones(
            ReservacionRepository reservacionRepo,
            InventarioCategoriaRepository inventarioRepo,
            UnitOfWork unitOfWork) {

        this.reservacionRepo = reservacionRepo;
        this.inventarioRepo = inventarioRepo;
        this.unitOfWork = unitOfWork;
    }

    public ConfirmarReservacionResultado confirmarReservacion(
            ConfirmarReservacionCommand cmd) {

        unitOfWork.begin();

        try {

            InventarioCategoria inventario =
                inventarioRepo.obtenerDe(
                    cmd.getSucursalId(),
                    cmd.getCategoria()
                );

            if (!inventario.hayCapacidad(
                    cmd.getPeriodo())) {

                throw new IllegalStateException(
                    "Categoría sin inventario disponible."
                );
            }

            inventario.apartarLugares(
                cmd.getPeriodo()
            );

            Reservacion reservacion =
                new Reservacion(
                    cmd.getFolio(),
                    cmd.getClienteId(),
                    cmd.getSucursalId(),
                    cmd.getCategoria(),
                    cmd.getPeriodo()
                );

            reservacion.confirmar();

            reservacionRepo.guardar(
                reservacion
            );

            unitOfWork.commit();

            return new ConfirmarReservacionResultado(
                reservacion.getFolio(),
                cmd.getCategoria()
            );

        } catch (Exception ex) {

            unitOfWork.rollback();

            throw ex;
        }
    }
}