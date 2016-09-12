package com.jorge.client;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import com.jorge.entity.Message;

/**
 * Using Hibernate as JPA provider
 * 
 *  persistence.xml file is in META-INF folder
 *  
 *  We need hibernate-entitymanager-x.x.x.jar library
 *
 */
public class Main {

	public static void main(String[] args) {
		BasicConfigurator.configure(); // Necessary for configure log4j. It must be the first line in main method
	       					           // log4j.properties must be in /src directory

		Logger  logger = Logger.getLogger(Main.class.getName());
		logger.debug("log4j configured correctly and logger set");

		// How make the same things with JPA and Hibernate (commented)
		logger.debug("creating entity manager factory");
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("helloworld"); // => SessionFactory sf = HibernateUtil.getSessionFactory(); HibernateUtil is a class created by us.
																						 // Persistence is imported from javax.persistence.Persistence package, it is not a class created by us
																						 // "helloworld" persistence unit name is the same name than "<persistence-unit name="helloworld"...>" element in persistence.xml file 
		logger.debug("creating entity manager");
		EntityManager em = emf.createEntityManager(); // => Session session = sf.openSession();
		
		logger.debug("getting transaction");
		EntityTransaction txn = em.getTransaction(); // => Transaction txn = session.getTransaction();
		
		try{
			logger.debug("beginning transaction");
			txn.begin();
			
			logger.debug("setting message");
			Message message = new Message("Trying text with Hibernate as JPA Provider");
			
			logger.debug("persisting message");
			em.persist(message); // => session.persist(message);
			
			// Getting data from DataBase
			//Message message = em.find(Message.class, 2L); // => Message message = (Message) session.get(Message.class, 2L); 
														    // In JPa, we 
			logger.debug("making commit");
			txn.commit();
			
			
		}
		catch (Exception e) {
			if (txn != null) {
				logger.error("something was wrong, making rollback of transactions");
				txn.rollback(); // If something was wrong, we make rollback
			}
			logger.error("Exception: " + e.getMessage().toString());
		} finally {
			if (em != null) { // => if (session != null) {
				logger.debug("close session");
				em.close(); // => session.close();
			}
		}
	}

}
  