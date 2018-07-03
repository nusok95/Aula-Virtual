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
import basededatos.Clase;
import basededatos.Periodo;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author susana
 */
public class PeriodoJpaController implements Serializable {

    public PeriodoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Periodo periodo) {
        if (periodo.getClaseCollection() == null) {
            periodo.setClaseCollection(new ArrayList<Clase>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Clase> attachedClaseCollection = new ArrayList<Clase>();
            for (Clase claseCollectionClaseToAttach : periodo.getClaseCollection()) {
                claseCollectionClaseToAttach = em.getReference(claseCollectionClaseToAttach.getClass(), claseCollectionClaseToAttach.getIdClase());
                attachedClaseCollection.add(claseCollectionClaseToAttach);
            }
            periodo.setClaseCollection(attachedClaseCollection);
            em.persist(periodo);
            for (Clase claseCollectionClase : periodo.getClaseCollection()) {
                Periodo oldIdPeriodoOfClaseCollectionClase = claseCollectionClase.getIdPeriodo();
                claseCollectionClase.setIdPeriodo(periodo);
                claseCollectionClase = em.merge(claseCollectionClase);
                if (oldIdPeriodoOfClaseCollectionClase != null) {
                    oldIdPeriodoOfClaseCollectionClase.getClaseCollection().remove(claseCollectionClase);
                    oldIdPeriodoOfClaseCollectionClase = em.merge(oldIdPeriodoOfClaseCollectionClase);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Periodo periodo) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Periodo persistentPeriodo = em.find(Periodo.class, periodo.getIdPeriodo());
            Collection<Clase> claseCollectionOld = persistentPeriodo.getClaseCollection();
            Collection<Clase> claseCollectionNew = periodo.getClaseCollection();
            List<String> illegalOrphanMessages = null;
            for (Clase claseCollectionOldClase : claseCollectionOld) {
                if (!claseCollectionNew.contains(claseCollectionOldClase)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Clase " + claseCollectionOldClase + " since its idPeriodo field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Clase> attachedClaseCollectionNew = new ArrayList<Clase>();
            for (Clase claseCollectionNewClaseToAttach : claseCollectionNew) {
                claseCollectionNewClaseToAttach = em.getReference(claseCollectionNewClaseToAttach.getClass(), claseCollectionNewClaseToAttach.getIdClase());
                attachedClaseCollectionNew.add(claseCollectionNewClaseToAttach);
            }
            claseCollectionNew = attachedClaseCollectionNew;
            periodo.setClaseCollection(claseCollectionNew);
            periodo = em.merge(periodo);
            for (Clase claseCollectionNewClase : claseCollectionNew) {
                if (!claseCollectionOld.contains(claseCollectionNewClase)) {
                    Periodo oldIdPeriodoOfClaseCollectionNewClase = claseCollectionNewClase.getIdPeriodo();
                    claseCollectionNewClase.setIdPeriodo(periodo);
                    claseCollectionNewClase = em.merge(claseCollectionNewClase);
                    if (oldIdPeriodoOfClaseCollectionNewClase != null && !oldIdPeriodoOfClaseCollectionNewClase.equals(periodo)) {
                        oldIdPeriodoOfClaseCollectionNewClase.getClaseCollection().remove(claseCollectionNewClase);
                        oldIdPeriodoOfClaseCollectionNewClase = em.merge(oldIdPeriodoOfClaseCollectionNewClase);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = periodo.getIdPeriodo();
                if (findPeriodo(id) == null) {
                    throw new NonexistentEntityException("The periodo with id " + id + " no longer exists.");
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
            Periodo periodo;
            try {
                periodo = em.getReference(Periodo.class, id);
                periodo.getIdPeriodo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The periodo with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Clase> claseCollectionOrphanCheck = periodo.getClaseCollection();
            for (Clase claseCollectionOrphanCheckClase : claseCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Periodo (" + periodo + ") cannot be destroyed since the Clase " + claseCollectionOrphanCheckClase + " in its claseCollection field has a non-nullable idPeriodo field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(periodo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Periodo> findPeriodoEntities() {
        return findPeriodoEntities(true, -1, -1);
    }

    public List<Periodo> findPeriodoEntities(int maxResults, int firstResult) {
        return findPeriodoEntities(false, maxResults, firstResult);
    }

    private List<Periodo> findPeriodoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Periodo.class));
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

    public Periodo findPeriodo(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Periodo.class, id);
        } finally {
            em.close();
        }
    }

    public int getPeriodoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Periodo> rt = cq.from(Periodo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
