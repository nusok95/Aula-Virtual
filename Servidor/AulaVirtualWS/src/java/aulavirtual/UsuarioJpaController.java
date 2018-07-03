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
import basededatos.CargaEducativa;
import java.util.ArrayList;
import java.util.Collection;
import basededatos.Clase;
import basededatos.Usuario;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author susana
 */
public class UsuarioJpaController implements Serializable {

    public UsuarioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Usuario usuario) {
        if (usuario.getCargaEducativaCollection() == null) {
            usuario.setCargaEducativaCollection(new ArrayList<CargaEducativa>());
        }
        if (usuario.getClaseCollection() == null) {
            usuario.setClaseCollection(new ArrayList<Clase>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<CargaEducativa> attachedCargaEducativaCollection = new ArrayList<CargaEducativa>();
            for (CargaEducativa cargaEducativaCollectionCargaEducativaToAttach : usuario.getCargaEducativaCollection()) {
                cargaEducativaCollectionCargaEducativaToAttach = em.getReference(cargaEducativaCollectionCargaEducativaToAttach.getClass(), cargaEducativaCollectionCargaEducativaToAttach.getCargaEducativaPK());
                attachedCargaEducativaCollection.add(cargaEducativaCollectionCargaEducativaToAttach);
            }
            usuario.setCargaEducativaCollection(attachedCargaEducativaCollection);
            Collection<Clase> attachedClaseCollection = new ArrayList<Clase>();
            for (Clase claseCollectionClaseToAttach : usuario.getClaseCollection()) {
                claseCollectionClaseToAttach = em.getReference(claseCollectionClaseToAttach.getClass(), claseCollectionClaseToAttach.getIdClase());
                attachedClaseCollection.add(claseCollectionClaseToAttach);
            }
            usuario.setClaseCollection(attachedClaseCollection);
            em.persist(usuario);
            for (CargaEducativa cargaEducativaCollectionCargaEducativa : usuario.getCargaEducativaCollection()) {
                Usuario oldUsuarioOfCargaEducativaCollectionCargaEducativa = cargaEducativaCollectionCargaEducativa.getUsuario();
                cargaEducativaCollectionCargaEducativa.setUsuario(usuario);
                cargaEducativaCollectionCargaEducativa = em.merge(cargaEducativaCollectionCargaEducativa);
                if (oldUsuarioOfCargaEducativaCollectionCargaEducativa != null) {
                    oldUsuarioOfCargaEducativaCollectionCargaEducativa.getCargaEducativaCollection().remove(cargaEducativaCollectionCargaEducativa);
                    oldUsuarioOfCargaEducativaCollectionCargaEducativa = em.merge(oldUsuarioOfCargaEducativaCollectionCargaEducativa);
                }
            }
            for (Clase claseCollectionClase : usuario.getClaseCollection()) {
                Usuario oldIdMaestroOfClaseCollectionClase = claseCollectionClase.getIdMaestro();
                claseCollectionClase.setIdMaestro(usuario);
                claseCollectionClase = em.merge(claseCollectionClase);
                if (oldIdMaestroOfClaseCollectionClase != null) {
                    oldIdMaestroOfClaseCollectionClase.getClaseCollection().remove(claseCollectionClase);
                    oldIdMaestroOfClaseCollectionClase = em.merge(oldIdMaestroOfClaseCollectionClase);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Usuario usuario) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario persistentUsuario = em.find(Usuario.class, usuario.getIdUsuario());
            Collection<CargaEducativa> cargaEducativaCollectionOld = persistentUsuario.getCargaEducativaCollection();
            Collection<CargaEducativa> cargaEducativaCollectionNew = usuario.getCargaEducativaCollection();
            Collection<Clase> claseCollectionOld = persistentUsuario.getClaseCollection();
            Collection<Clase> claseCollectionNew = usuario.getClaseCollection();
            List<String> illegalOrphanMessages = null;
            for (CargaEducativa cargaEducativaCollectionOldCargaEducativa : cargaEducativaCollectionOld) {
                if (!cargaEducativaCollectionNew.contains(cargaEducativaCollectionOldCargaEducativa)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain CargaEducativa " + cargaEducativaCollectionOldCargaEducativa + " since its usuario field is not nullable.");
                }
            }
            for (Clase claseCollectionOldClase : claseCollectionOld) {
                if (!claseCollectionNew.contains(claseCollectionOldClase)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Clase " + claseCollectionOldClase + " since its idMaestro field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<CargaEducativa> attachedCargaEducativaCollectionNew = new ArrayList<CargaEducativa>();
            for (CargaEducativa cargaEducativaCollectionNewCargaEducativaToAttach : cargaEducativaCollectionNew) {
                cargaEducativaCollectionNewCargaEducativaToAttach = em.getReference(cargaEducativaCollectionNewCargaEducativaToAttach.getClass(), cargaEducativaCollectionNewCargaEducativaToAttach.getCargaEducativaPK());
                attachedCargaEducativaCollectionNew.add(cargaEducativaCollectionNewCargaEducativaToAttach);
            }
            cargaEducativaCollectionNew = attachedCargaEducativaCollectionNew;
            usuario.setCargaEducativaCollection(cargaEducativaCollectionNew);
            Collection<Clase> attachedClaseCollectionNew = new ArrayList<Clase>();
            for (Clase claseCollectionNewClaseToAttach : claseCollectionNew) {
                claseCollectionNewClaseToAttach = em.getReference(claseCollectionNewClaseToAttach.getClass(), claseCollectionNewClaseToAttach.getIdClase());
                attachedClaseCollectionNew.add(claseCollectionNewClaseToAttach);
            }
            claseCollectionNew = attachedClaseCollectionNew;
            usuario.setClaseCollection(claseCollectionNew);
            usuario = em.merge(usuario);
            for (CargaEducativa cargaEducativaCollectionNewCargaEducativa : cargaEducativaCollectionNew) {
                if (!cargaEducativaCollectionOld.contains(cargaEducativaCollectionNewCargaEducativa)) {
                    Usuario oldUsuarioOfCargaEducativaCollectionNewCargaEducativa = cargaEducativaCollectionNewCargaEducativa.getUsuario();
                    cargaEducativaCollectionNewCargaEducativa.setUsuario(usuario);
                    cargaEducativaCollectionNewCargaEducativa = em.merge(cargaEducativaCollectionNewCargaEducativa);
                    if (oldUsuarioOfCargaEducativaCollectionNewCargaEducativa != null && !oldUsuarioOfCargaEducativaCollectionNewCargaEducativa.equals(usuario)) {
                        oldUsuarioOfCargaEducativaCollectionNewCargaEducativa.getCargaEducativaCollection().remove(cargaEducativaCollectionNewCargaEducativa);
                        oldUsuarioOfCargaEducativaCollectionNewCargaEducativa = em.merge(oldUsuarioOfCargaEducativaCollectionNewCargaEducativa);
                    }
                }
            }
            for (Clase claseCollectionNewClase : claseCollectionNew) {
                if (!claseCollectionOld.contains(claseCollectionNewClase)) {
                    Usuario oldIdMaestroOfClaseCollectionNewClase = claseCollectionNewClase.getIdMaestro();
                    claseCollectionNewClase.setIdMaestro(usuario);
                    claseCollectionNewClase = em.merge(claseCollectionNewClase);
                    if (oldIdMaestroOfClaseCollectionNewClase != null && !oldIdMaestroOfClaseCollectionNewClase.equals(usuario)) {
                        oldIdMaestroOfClaseCollectionNewClase.getClaseCollection().remove(claseCollectionNewClase);
                        oldIdMaestroOfClaseCollectionNewClase = em.merge(oldIdMaestroOfClaseCollectionNewClase);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = usuario.getIdUsuario();
                if (findUsuario(id) == null) {
                    throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.");
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
            Usuario usuario;
            try {
                usuario = em.getReference(Usuario.class, id);
                usuario.getIdUsuario();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<CargaEducativa> cargaEducativaCollectionOrphanCheck = usuario.getCargaEducativaCollection();
            for (CargaEducativa cargaEducativaCollectionOrphanCheckCargaEducativa : cargaEducativaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the CargaEducativa " + cargaEducativaCollectionOrphanCheckCargaEducativa + " in its cargaEducativaCollection field has a non-nullable usuario field.");
            }
            Collection<Clase> claseCollectionOrphanCheck = usuario.getClaseCollection();
            for (Clase claseCollectionOrphanCheckClase : claseCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the Clase " + claseCollectionOrphanCheckClase + " in its claseCollection field has a non-nullable idMaestro field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(usuario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Usuario> findUsuarioEntities() {
        return findUsuarioEntities(true, -1, -1);
    }

    public List<Usuario> findUsuarioEntities(int maxResults, int firstResult) {
        return findUsuarioEntities(false, maxResults, firstResult);
    }

    private List<Usuario> findUsuarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usuario.class));
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

    public Usuario findUsuario(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuario.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usuario> rt = cq.from(Usuario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
