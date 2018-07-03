/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basededatos;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author susana
 */
@Entity
@Table(name = "clase")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Clase.findAll", query = "SELECT c FROM Clase c")
    , @NamedQuery(name = "Clase.findByIdClase", query = "SELECT c FROM Clase c WHERE c.idClase = :idClase")
    , @NamedQuery(name = "Clase.findByNrc", query = "SELECT c FROM Clase c WHERE c.nrc = :nrc")
    , @NamedQuery(name = "Clase.findByNombre", query = "SELECT c FROM Clase c WHERE c.nombre = :nombre")
    , @NamedQuery(name = "Clase.findByCarpetaCompartida", query = "SELECT c FROM Clase c WHERE c.carpetaCompartida = :carpetaCompartida")})
public class Clase implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_clase")
    private Integer idClase;
    @Basic(optional = false)
    @Column(name = "NRC")
    private String nrc;
    @Basic(optional = false)
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "carpeta_compartida")
    private String carpetaCompartida;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "clase")
    private Collection<CargaEducativa> cargaEducativaCollection;
    @JoinColumn(name = "id_periodo", referencedColumnName = "id_periodo")
    @ManyToOne(optional = false)
    private Periodo idPeriodo;
    @JoinColumn(name = "id_maestro", referencedColumnName = "id_usuario")
    @ManyToOne(optional = false)
    private Usuario idMaestro;

    public Clase() {
    }

    public Clase(Integer idClase) {
        this.idClase = idClase;
    }

    public Clase(Integer idClase, String nrc, String nombre) {
        this.idClase = idClase;
        this.nrc = nrc;
        this.nombre = nombre;
    }

    public Integer getIdClase() {
        return idClase;
    }

    public void setIdClase(Integer idClase) {
        this.idClase = idClase;
    }

    public String getNrc() {
        return nrc;
    }

    public void setNrc(String nrc) {
        this.nrc = nrc;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCarpetaCompartida() {
        return carpetaCompartida;
    }

    public void setCarpetaCompartida(String carpetaCompartida) {
        this.carpetaCompartida = carpetaCompartida;
    }

    @XmlTransient
    public Collection<CargaEducativa> getCargaEducativaCollection() {
        return cargaEducativaCollection;
    }

    public void setCargaEducativaCollection(Collection<CargaEducativa> cargaEducativaCollection) {
        this.cargaEducativaCollection = cargaEducativaCollection;
    }

    public Periodo getIdPeriodo() {
        return idPeriodo;
    }

    public void setIdPeriodo(Periodo idPeriodo) {
        this.idPeriodo = idPeriodo;
    }

    public Usuario getIdMaestro() {
        return idMaestro;
    }

    public void setIdMaestro(Usuario idMaestro) {
        this.idMaestro = idMaestro;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idClase != null ? idClase.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Clase)) {
            return false;
        }
        Clase other = (Clase) object;
        if ((this.idClase == null && other.idClase != null) || (this.idClase != null && !this.idClase.equals(other.idClase))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "basededatos.Clase[ idClase=" + idClase + " ]";
    }
    
}
