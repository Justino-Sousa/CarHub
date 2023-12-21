package br.com.js.carhub.repository;

import java.util.List;

import br.com.js.carhub.model.LoginInformation;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

public class CustomQueryImpl implements CustomQuery{

	@PersistenceContext
	EntityManager em;


	@SuppressWarnings("unchecked")
	@Override
	public List<LoginInformation> findByUser(Long id) {
		String sql = "SELECT * FROM login_information "
				+ " WHERE user_id = :id ";

		    Query query = em.createNativeQuery(sql, LoginInformation.class);
		    query.setParameter("id", id);
		    return query.getResultList();
	}

}
