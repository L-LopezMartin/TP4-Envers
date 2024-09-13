package org.example;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import entidades.*;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("example-unit");
        //Ese example-unit es el mismo que el de persistence-unit del xml

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        System.out.println("los engranajes comienzan a girar...");

        try {

            //region Transacción de ingreso de datos
            entityManager.getTransaction().begin();

            //Creación de las entidades
                //Categorías
            Categoria cat1 = Categoria.builder()
                    .denominacion("Exóticos").build();
            Categoria cat2 = Categoria.builder()
                    .denominacion("Alimentos").build();
            Categoria cat3 = Categoria.builder()
                    .denominacion("Cocina").build();

                //Artículos
            Articulo articulo1 = Articulo.builder()
                    .cantidad(100)
                    .denominacion("Caparazón de tortuga marítima")
                    .precio(300).build();
            articulo1.getCategorias().add(cat1);
            Articulo articulo2 = Articulo.builder()
                    .cantidad(250)
                    .denominacion("Caviar")
                    .precio(100).build();
            articulo2.getCategorias().add(cat1);
            articulo2.getCategorias().add(cat2);
            Articulo articulo3 = Articulo.builder()
                    .cantidad(70)
                    .denominacion("Juguito Baggio")
                    .precio(20).build();
            articulo3.getCategorias().add(cat2);
            Articulo articulo4 = Articulo.builder()
                    .cantidad(120)
                    .denominacion("Fósforos tres patitos")
                    .precio(10).build();
            articulo4.getCategorias().add(cat3);

                // Domicilios
            Domicilio dom1 = Domicilio.builder()
                    .nombreCalle("Olavarría")
                    .numero(512).build();
            Domicilio dom2 = Domicilio.builder()
                    .nombreCalle("Olavarría")
                    .numero(512).build();
            Domicilio dom3 = Domicilio.builder()
                    .nombreCalle("Suiza")
                    .numero(46).build();

                // Clientes
            Cliente cli1 = Cliente.builder()
                    .dni(40654781)
                    .nombre("Carlos")
                    .apellido("Sainz").build();
            cli1.setDomicilio(dom1);

            Cliente cli2 = Cliente.builder()
                    .dni(28861720)
                    .nombre("Maria Jose")
                    .apellido("Estrada").build();
            cli2.setDomicilio(dom2);

            Cliente cli3 = Cliente.builder()
                    .dni(55978542)
                    .nombre("Petr")
                    .apellido("Tägtgren").build();
            cli3.setDomicilio(dom3);

                //Facturas y detalles de facturas
            Factura factura1 = Factura.builder()
                    .fecha("04-09-2024")
                    .total(350)
                    .numero(1)
                    .detalleFactura(DetalleFactura.builder()
                            .cantidad(1)
                            .articulo(articulo1)
                            .subtotal(300).build())
                    .detalleFactura(DetalleFactura.builder()
                            .cantidad(5)
                            .articulo(articulo4)
                            .subtotal(50).build())
                    .build();
            factura1.setCliente(cli1);

            Factura factura2 = Factura.builder()
                    .fecha("03-09-2024")
                    .total(540)
                    .numero(2)
                    .detalleFactura(DetalleFactura.builder()
                            .cantidad(12)
                            .articulo(articulo3)
                            .subtotal(240).build())
                    .detalleFactura(DetalleFactura.builder()
                            .cantidad(3)
                            .articulo(articulo2)
                            .subtotal(300).build())
                    .build();
            factura2.setCliente(cli2);

            //Persistencia de las entidades
            entityManager.persist(cat1);
            entityManager.persist(cat2);
            entityManager.persist(cat3);
            entityManager.persist(articulo1);
            entityManager.persist(articulo2);
            entityManager.persist(articulo3);
            entityManager.persist(articulo4);
            entityManager.persist(cli1);
            entityManager.persist(cli2);
            entityManager.persist(cli3);
            entityManager.persist(dom1);
            entityManager.persist(dom2);
            entityManager.persist(dom3);
            entityManager.persist(factura2);
            entityManager.persist(factura1);

            //Fin transacción
            entityManager.flush();
            entityManager.getTransaction().commit();
            //endregion

            //region Actualización de datos
            entityManager.getTransaction().begin();

            Domicilio domicilioUpdate1 = entityManager.find(Domicilio.class, 2L);
            domicilioUpdate1.setNombreCalle("Azcuenaga");
            domicilioUpdate1.setNumero(320);

            Cliente clienteUpdate1 = entityManager.find(Cliente.class, 3L);
            clienteUpdate1.setNombre("Carlos");
            clienteUpdate1.setApellido("Martel");
            clienteUpdate1.setDni(54666781);
            clienteUpdate1.setDomicilio(entityManager.find(Domicilio.class, 2L));

            entityManager.merge(domicilioUpdate1);
            entityManager.merge(clienteUpdate1);

            entityManager.flush();
            entityManager.getTransaction().commit();
            //endregion

            //region Eliminar datos
            entityManager.getTransaction().begin();

            Cliente clienteEliminar1 = entityManager.find(Cliente.class, 3L);
            Domicilio domicilioEliminar1 = entityManager.find(Domicilio.class, 3L);
            entityManager.remove(clienteEliminar1);
            entityManager.remove(domicilioEliminar1);

            entityManager.flush();
            entityManager.getTransaction().commit();
            //endregion
        }
        catch (Exception e){
            entityManager.getTransaction().rollback();
            System.out.println("Hubo un error en la persistencia");
            System.out.println("Error: "+ e.getMessage());
        }

        entityManager.close();
        entityManagerFactory.close();
    }
}
