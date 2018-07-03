/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basededatos;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author susana
 */
@Entity
@Table(name = "carga_educativa")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CargaEducativa.findAll", query = "SELECT c FROM CargaEducativa c")
    , @NamedQuery(name = "CargaEducativa.findByIdUsuario", query = "SELECT c FROM CargaEducativa c WHERE c.cargaEducativaPK.idUsuario = :idUsuario")
    , @NamedQuery(name = "CargaEducativa.findByIdClase", query = "SELECT c FROM CargaEducativa c WHERE c.cargaEducativaPK.idClase = :idClase")
    , @NamedQuery(name = "CargaEducativa.findByRol", query = "SELECT c FROM CargaEducativa c WHERE c.rol = :rol")})
public class CargaEducativa implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected CargaEducativaPK cargaEducativaPK;
    @Basic(optional = false)
    @Column(name = "rol")
    private String rol;
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Usuario usuario;
    @JoinColumn(name = "id_clase", referencedColumnName = "id_clase", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Clase clase;

    public CargaEducativa() {
    }

    public CargaEducativa(CargaEducativaPK cargaEducativaPK) {
        this.cargaEducativaPK = cargaEducativaPK;
    }

    public CargaEducativa(CargaEducativaPK cargaEducativaPK, String rol) {
        this.cargaEducativaPK = cargaEducativaPK;
        this.rol = rol;
    }

    public CargaEducativa(int idUsuario, int idClase) {
        this.cargaEducativaPK = new CargaEducativaPK(idUsuario, idClase);
    }

    public CargaEducativaPK getCargaEducativaPK() {
        return cargaEducativaPK;
    }

    public void setCargaEducativaPK(CargaEducativaPK cargaEducativaPK) {
        this.cargaEducativaPK = cargaEducativaPK;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Clase getClase() {
        return clase;
    }

    public void setClase(Clase clase) {
        this.clase = clase;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cargaEducativaPK != null ? cargaEducativaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CargaEducativa)) {
            return false;
        }
        CargaEducativa other = (CargaEducativa) object;
        if ((this.cargaEducativaPK == null && other.cargaEducativaPK != null) || (this.cargaEducativaPK != null && !this.cargaEducativaPK.equals(other.cargaEducativaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "basededatos.CargaEducativa[ cargaEducativaPK=" + cargaEducativaPK + " ]";
    }
    
}
