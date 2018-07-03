/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basededatos;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author susana
 */
@Embeddable
public class CargaEducativaPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "id_usuario")
    private int idUsuario;
    @Basic(optional = false)
    @Column(name = "id_clase")
    private int idClase;

    public CargaEducativaPK() {
    }

    public CargaEducativaPK(int idUsuario, int idClase) {
        this.idUsuario = idUsuario;
        this.idClase = idClase;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdClase() {
        return idClase;
    }

    public void setIdClase(int idClase) {
        this.idClase = idClase;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idUsuario;
        hash += (int) idClase;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CargaEducativaPK)) {
            return false;
        }
        CargaEducativaPK other = (CargaEducativaPK) object;
        if (this.idUsuario != other.idUsuario) {
            return false;
        }
        if (this.idClase != other.idClase) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "basededatos.CargaEducativaPK[ idUsuario=" + idUsuario + ", idClase=" + idClase + " ]";
    }
    
}
