package entidades;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;


@Getter
@Setter
@ToString
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Audited
public class Factura implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private int numero;
    private int total;
    private String fecha;

    //Foreign Keys
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "fk_cliente")
    private Cliente cliente;

    @OneToMany(mappedBy = "factura", orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<DetalleFactura> detalleFactura = new HashSet<>();

    public static class FacturaBuilder {
        private Set<DetalleFactura> detalles = new HashSet<>();
        private int numero;
        private int total;
        private String fecha;
        Factura factura = new Factura();

        public FacturaBuilder detalleFactura(DetalleFactura detalle) {
            detalle.setFactura(factura);
            this.detalles.add(detalle);
            return this;
        }

        public FacturaBuilder numero(int numero){
            this.numero= numero;
            return this;
        }

        public FacturaBuilder total(int total){
            this.total= total;
            return this;
        }

        public FacturaBuilder fecha(String fecha){
            this.fecha= fecha;
            return this;
        }

        public Factura build() {
            factura.setFecha(this.fecha);
            factura.setNumero(this.numero);
            factura.setTotal(this.total);
            factura.setDetalleFactura(this.detalles);
            return factura;
        }
    }
}
