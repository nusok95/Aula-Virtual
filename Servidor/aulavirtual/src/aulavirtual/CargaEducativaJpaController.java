/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aulavirtual;

import aulavirtual.exceptions.NonexistentEntityException;
import aulavirtual.exceptions.PreexistingEntityException;
import basededatos.CargaEducativa;
import basededatos.CargaEducativaPK;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import basededatos.Usuario;
import basededatos.Clase;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author susana
 */
public class CargaEducativaJpaController implements Serializable {

    public CargaEducativaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(CargaEducativa cargaEducativa) throws PreexistingEntityException, Exception {
        if (cargaEducativa.getCargaEducativaPK() == null) {
            cargaEducativa.setCargaEducativaPK(new CargaEducativaPK());
        }
        cargaEducativa.getCargaEducativaPK().setIdClase(cargaEducativa.getClase().getIdClase());
        cargaEducativa.getCargaEducativaPK().setIdUsuario(cargaEducativa.getUsuario().getIdUsuario());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario usuario = cargaEducativa.getUsuario();
            if (usuario != null) {
                usuario = em.getReference(usuario.getClass(), usuario.getIdUsuario());
                cargaEducativa.setUsuario(usuario);
            }
            Clase clase = cargaEducativa.getClase();
            if (clase != null) {
                clase = em.getReference(clase.getClass(), clase.getIdClase());
                cargaEducativa.setClase(clase);
            }
            em.persist(cargaEducativa);
            if (usuario != null) {
                usuario.getCargaEducativaCollection().add(cargaEducativa);
                usuario = em.merge(usuario);
            }
            if (clase != null) {
                clase.getCargaEducativaCollection().add(cargaEducativa);
                clase = em.merge(clase);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCargaEducativa(cargaEducativa.getCargaEducativaPK()) != null) {
                throw new PreexistingEntityException("CargaEducativa " + cargaEducativa + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(CargaEducativa cargaEducativa) throws NonexistentEntityException, Exception {
        cargaEducativa.getCargaEducativaPK().setIdClase(cargaEducativa.getClase().getIdClase());
        cargaEducativa.getCargaEducativaPK().setIdUsuario(cargaEducativa.getUsuario().getIdUsuario());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            CargaEducativa persistentCargaEducativa = em.find(CargaEducativa.class, cargaEducativa.getCargaEducativaPK());
            Usuario usuarioOld = persistentCargaEducativa.getUsuario();
            Usuario usuarioNew = cargaEducativa.getUsuario();
            Clase claseOld = persistentCargaEducativa.getClase();
            Clase claseNew = cargaEducativa.getClase();
            if (usuarioNew != null) {
                usuarioNew = em.getReference(usuarioNew.getClass(), usuarioNew.getIdUsuario());
                cargaEducativa.setUsuario(usuarioNew);
            }
            if (claseNew != null) {
                claseNew = em.getReference(claseNew.getClass(), claseNew.getIdClase());
                cargaEducativa.setClase(claseNew);
            }
            cargaEducativa = em.merge(cargaEducativa);
            if (usuarioOld != null && !usuarioOld.equals(usuarioNew)) {
                usuarioOld.getCargaEducativaCollection().remove(cargaEducativa);
                usuarioOld = em.merge(usuarioOld);
            }
            if (usuarioNew != null && !usuarioNew.equals(usuarioOld)) {
                usuarioNew.getCargaEducativaCollection().add(cargaEducativa);
                usuarioNew = em.merge(usuarioNew);
            }
            if (claseOld != null && !claseOld.equals(claseNew)) {
                claseOld.getCargaEducativaCollection().remove(cargaEducativa);
                claseOld = em.merge(claseOld);
            }
            if (claseNew != null && !claseNew.equals(claseOld)) {
                claseNew.getCargaEducativaCollection().add(cargaEducativa);
                claseNew = em.merge(claseNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                CargaEducativaPK id = cargaEducativa.getCargaEducativaPK();
                if (findCargaEducativa(id) == null) {
                    throw new NonexistentEntityException("The cargaEducativa with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(CargaEducativaPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            CargaEducativa cargaEducativa;
            try {
                cargaEducativa = em.getReference(CargaEducativa.class, id);
                cargaEducativa.getCargaEducativaPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cargaEducativa with id " + id + " no longer exists.", enfe);
            }
            Usuario usuario = cargaEducativa.getUsuario();
            if (usuario != null) {
                usuario.getCargaEducativaCollection().remove(cargaEducativa);
                usuario = em.merge(usuario);
            }
            Clase clase = cargaEducativa.getClase();
            if (clase != null) {
                clase.getCargaEducativaCollection().remove(cargaEducativa);
                clase = em.merge(clase);
            }
            em.remove(cargaEducativa);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<CargaEducativa> findCargaEducativaEntities() {
        return findCargaEducativaEntities(true, -1, -1);
    }

    public List<CargaEducativa> findCargaEducativaEntities(int maxResults, int firstResult) {
        return findCargaEducativaEntities(false, maxResults, firstResult);
    }

    private List<CargaEducativa> findCargaEducativaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(CargaEducativa.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public CargaEducativa findCargaEducativa(CargaEducativaPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(CargaEducativa.class, id);
        } finally {
            em.close();
        }
    }

    public int getCargaEducativaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<CargaEducativa> rt = cq.from(CargaEducativa.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
