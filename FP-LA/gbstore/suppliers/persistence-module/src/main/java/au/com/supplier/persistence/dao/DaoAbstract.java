package au.com.supplier.persistence.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

/** Abstract Dao for all objects. Persist, find and remove entities.
 * 
 * @author lteixeira
 *
 * @param <Entity>
 */
class DaoAbstract <Entity>{

	private static final String PERSISTENCE_UNIT_NAME = "supplierdb";
	private EntityManagerFactory factory;
	private Logger log = Logger.getLogger(this.getClass());
	/**
	 * Entity manager and PersistenceContext annotation.
	 */
	@PersistenceContext
	private EntityManager entityManager;

	/** create entityManager when it is not created.
	 * @throws Exception 
	 * 
	 */
	protected EntityManager getEntityManager() throws Exception {
		try {
			if (factory == null || !factory.isOpen()) {
				factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
			}
			if (entityManager == null || !entityManager.isOpen()) {
				this.entityManager = factory.createEntityManager();
			}
			return this.entityManager;
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			throw e;
		}
	}
	/**Save the Entity.
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	 Entity persist(Entity entity) throws Exception {
		try{
			EntityManager entityManager = getEntityManager();
			entityManager.persist(entity);
			return entity;
		}catch(Exception e){
			log.error(e.getMessage());
			throw e;
		}
	}
}
