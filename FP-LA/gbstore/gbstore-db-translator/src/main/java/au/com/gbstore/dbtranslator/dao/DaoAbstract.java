package au.com.gbstore.dbtranslator.dao;
 
import java.lang.reflect.ParameterizedType;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

import au.com.gbstore.dbtranslator.utils.PropertiesValue;
 
/**Abstract Dao for all objects.
 * Persist, find and remove entities
 * @author lteixeira
 *
 * @param <Entity>
 */
public class DaoAbstract<Entity> {
 
private static PropertiesValue pv = new PropertiesValue();
private static final String PERSISTENCE_UNIT_NAME = pv.getPersistenceUnit();
private EntityManagerFactory factory;
private Logger log = Logger.getLogger(this.getClass());
/** Get the entityClass name.
 */
protected Class<Entity> entityClass;
 
/**Entity manager and PersistenceContext annotation.
 */
@PersistenceContext
private EntityManager entityManager;
 
/** Class constructor. 
 */
@SuppressWarnings("unchecked")
public DaoAbstract() {
       ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
       this.entityClass = (Class<Entity>) genericSuperclass.getActualTypeArguments()[0];
   }
	/**create entityManager when it is not created
	 * @throws Exception 
	 * 
	 */
	protected EntityManager getEntityManager() throws Exception{
		try{
			if(factory==null || !factory.isOpen()){
				factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
			}
			if(this.entityManager==null|| !this.entityManager.isOpen()){
				this.entityManager = factory.createEntityManager();
			}
			return this.entityManager;
		}catch(Exception e){
			log.error(e.getMessage());
			throw e;
		}
	}
   /**Persist an entity.
    * @param entity - Entity
    * @return entity - Entity
 * @throws Exception 
    */
   public Entity persist(Entity entity) throws Exception {
       try{
    	   EntityManager entityManager = getEntityManager();
    	   entityManager.getTransaction().begin();
    	   entity = entityManager.merge(entity);
    	   entityManager.getTransaction().commit();
    	   entityManager.close();
    	   return entity;
       }
       catch(Exception e){
           log.error(e.getMessage());
           throw e;
       }
   }
   /**Remove an entity.
    * @param id - Long
 * @throws Exception 
    */
   public void remove(Long id) throws Exception {
	   EntityManager entityManager = getEntityManager();
	   entityManager.getTransaction().begin();
       Entity newEntity = entityManager.find(entityClass, id);
       entityManager.remove(newEntity);
       entityManager.getTransaction().commit();
	   entityManager.close();
   }
   /**find an entity.
    * @param id - Long
    * @return entity - Entity
 * @throws Exception 
    */
   public Entity findById(Long id) throws Exception {
       
       try{
    	   EntityManager entityManager = getEntityManager();
           Entity entity = entityManager.find(entityClass, id);
           return entity;
       }catch(Exception e){
           log.error(e.getMessage());
           throw e;
       }
   }
}