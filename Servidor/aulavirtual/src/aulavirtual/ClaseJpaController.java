/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aulavirtual;

import aulavirtual.exceptions.IllegalOrphanException;
import aulavirtual.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import basededatos.Periodo;
import basededatos.Usuario;
import basededatos.CargaEducativa;
import basededatos.Clase;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author susana
 */
public class ClaseJpaController implements Serializable {

    public ClaseJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Clase clase) {
        if (clase.getCargaEducativaCollection() == null) {
            clase.setCargaEducativaCollection(new ArrayList<CargaEducativa>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Periodo idPeriodo = clase.getIdPeriodo();
            if (idPeriodo != null) {
                idPeriodo = em.getReference(idPeriodo.getClass(), idPeriodo.getIdPeriodo());
                clase.setIdPeriodo(idPeriodo);
            }
            Usuario idMaestro = clase.getIdMaestro();
            if (idMaestro != null) {
                idMaestro = em.getReference(idMaestro.getClass(), idMaestro.getIdUsuario());
                clase.setIdMaestro(idMaestro);
            }
            Collection<CargaEducativa> attachedCargaEducativaCollection = new ArrayList<CargaEducativa>();
            for (CargaEducativa cargaEducativaCollectionCargaEducativaToAttach : clase.getCargaEducativaCollection()) {
                cargaEducativaCollectionCargaEducativaToAttach = em.getReference(cargaEducativaCollectionCargaEducativaToAttach.getClass(), cargaEducativaCollectionCargaEducativaToAttach.getCargaEducativaPK());
                attachedCargaEducativaCollection.add(cargaEducativaCollectionCargaEducativaToAttach);
            }
            clase.setCargaEducativaCollection(attachedCargaEducativaCollection);
            em.persist(clase);
            if (idPeriodo != null) {
                idPeriodo.getClaseCollection().add(clase);
                idPeriodo = em.merge(idPeriodo);
            }
            if (idMaestro != null) {
                idMaestro.getClaseCollection().add(clase);
                idMaestro = em.merge(idMaestro);
            }
            for (CargaEducativa cargaEducativaCollectionCargaEducativa : clase.getCargaEducativaCollection()) {
                Clase oldClaseOfCargaEducativaCollectionCargaEducativa = cargaEducativaCollectionCargaEducativa.getClase();
                cargaEducativaCollectionCargaEducativa.setClase(clase);
                cargaEducativaCollectionCargaEducativa = em.merge(cargaEducativaCollectionCargaEducativa);
                if (oldClaseOfCargaEducativaCollectionCargaEducativa != null) {
                    oldClaseOfCargaEducativaCollectionCargaEducativa.getCargaEducativaCollection().remove(cargaEducativaCollectionCargaEducativa);
                    oldClaseOfCargaEducativaCollectionCargaEducativa = em.merge(oldClaseOfCargaEducativaCollectionCargaEducativa);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Clase clase) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Clase persistentClase = em.find(Clase.class, clase.getIdClase());
            Periodo idPeriodoOld = persistentClase.getIdPeriodo();
            Periodo idPeriodoNew = clase.getIdPeriodo();
            Usuario idMaestroOld = persistentClase.getIdMaestro();
            Usuario idMaestroNew = clase.getIdMaestro();
            Collection<CargaEducativa> cargaEducativaCollectionOld = persistentClase.getCargaEducativaCollection();
            Collection<CargaEducativa> cargaEducativaCollectionNew = clase.getCargaEducativaCollection();
            List<String> illegalOrphanMessages = null;
            for (CargaEducativa cargaEducativaCollectionOldCargaEducativa : cargaEducativaCollectionOld) {
                if (!cargaEducativaCollectionNew.contains(cargaEducativaCollectionOldCargaEducativa)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain CargaEducativa " + cargaEducativaCollectionOldCargaEducativa + " since its clase field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idPeriodoNew != null) {
                idPeriodoNew = em.getReference(idPeriodoNew.getClass(), idPeriodoNew.getIdPeriodo());
                clase.setIdPeriodo(idPeriodoNew);
            }
            if (idMaestroNew != null) {
                idMaestroNew = em.getReference(idMaestroNew.getClass(), idMaestroNew.getIdUsuario());
                clase.setIdMaestro(idMaestroNew);
            }
            Collection<CargaEducativa> attachedCargaEducativaCollectionNew = new ArrayList<CargaEducativa>();
            for (CargaEducativa cargaEducativaCollectionNewCargaEducativaToAttach : cargaEducativaCollectionNew) {
                cargaEducativaCollectionNewCargaEducativaToAttach = em.getReference(cargaEducativaCollectionNewCargaEducativaToAttach.getClass(), cargaEducativaCollectionNewCargaEducativaToAttach.getCargaEducativaPK());
                attachedCargaEducativaCollectionNew.add(cargaEducativaCollectionNewCargaEducativaToAttach);
            }
            cargaEducativaCollectionNew = attachedCargaEducativaCollectionNew;
            clase.setCargaEducativaCollection(cargaEducativaCollectionNew);
            clase = em.merge(clase);
            if (idPeriodoOld != null && !idPeriodoOld.equals(idPeriodoNew)) {
                idPeriodoOld.getClaseCollection().remove(clase);
                idPeriodoOld = em.merge(idPeriodoOld);
            }
            if (idPeriodoNew != null && !idPeriodoNew.equals(idPeriodoOld)) {
                idPeriodoNew.getClaseCollection().add(clase);
                idPeriodoNew = em.merge(idPeriodoNew);
            }
            if (idMaestroOld != null && !idMaestroOld.equals(idMaestroNew)) {
                idMaestroOld.getClaseCollection().remove(clase);
                idMaestroOld = em.merge(idMaestroOld);
            }
            if (idMaestroNew != null && !idMaestroNew.equals(idMaestroOld)) {
                idMaestroNew.getClaseCollection().add(clase);
                idMaestroNew = em.merge(idMaestroNew);
            }
            for (CargaEducativa cargaEducativaCollectionNewCargaEducativa : cargaEducativaCollectionNew) {
                if (!cargaEducativaCollectionOld.contains(cargaEducativaCollectionNewCargaEducativa)) {
                    Clase oldClaseOfCargaEducativaCollectionNewCargaEducativa = cargaEducativaCollectionNewCargaEducativa.getClase();
                    cargaEducativaCollectionNewCargaEducativa.setClase(clase);
                    cargaEducativaCollectionNewCargaEducativa = em.merge(cargaEducativaCollectionNewCargaEducativa);
                    if (oldClaseOfCargaEducativaCollectionNewCargaEducativa != null && !oldClaseOfCargaEducativaCollectionNewCargaEducativa.equals(clase)) {
                        oldClaseOfCargaEducativaCollectionNewCargaEducativa.getCargaEducativaCollection().remove(cargaEducativaCollectionNewCargaEducativa);
                        oldClaseOfCargaEducativaCollectionNewCargaEducativa = em.merge(oldClaseOfCargaEducativaCollectionNewCargaEducativa);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = clase.getIdClase();
                if (findClase(id) == null) {
                    throw new NonexistentEntityException("The clase with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Clase clase;
            try {
                clase = em.getReference(Clase.class, id);
                clase.getIdClase();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The clase with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<CargaEducativa> cargaEducativaCollectionOrphanCheck = clase.getCargaEducativaCollection();
            for (CargaEducativa cargaEducativaCollectionOrphanCheckCargaEducativa : cargaEducativaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Clase (" + clase + ") cannot be destroyed since the CargaEducativa " + cargaEducativaCollectionOrphanCheckCargaEducativa + " in its cargaEducativaCollection field has a non-nullable clase field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Periodo idPeriodo = clase.getIdPeriodo();
            if (idPeriodo != null) {
                idPeriodo.getClaseCollection().remove(clase);
                idPeriodo = em.merge(idPeriodo);
            }
            Usuario idMaestro = clase.getIdMaestro();
            if (idMaestro != null) {
                idMaestro.getClaseCollection().remove(clase);
                idMaestro = em.merge(idMaestro);
            }
            em.remove(clase);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Clase> findClaseEntities() {
        return findClaseEntities(true, -1, -1);
    }

    public List<Clase> findClaseEntities(int maxResults, int firstResult) {
        return findClaseEntities(false, maxResults, firstResult);
    }

    private List<Clase> findClaseEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Clase.class));
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

    public Clase findClase(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Clase.class, id);
        } finally {
            em.close();
        }
    }

    public int getClaseCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Clase> rt = cq.from(Clase.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
