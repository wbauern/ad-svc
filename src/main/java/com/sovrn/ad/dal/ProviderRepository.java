package com.sovrn.ad.dal;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sovrn.ad.dal.entity.Provider;

public interface ProviderRepository extends JpaRepository<Provider, Integer>{

	public static final String PROVIDER_QUERY = 
		"SELECT DISTINCT p.*" +
		" FROM   `user` u" +
		"       INNER JOIN user_provider_assoc upa" +
		"         ON upa.user_id = u.user_id" +
		"       INNER JOIN provider p" +
		"         ON p.provider_id = upa.provider_id" +
		"       INNER JOIN provider_size_assoc psa" +
		"         ON psa.provider_id = p.provider_id" +
		"		    INNER JOIN ad_size asz" +
		"         ON asz.ad_size_id = psa.ad_size_id" +
		" WHERE  u.user_id = :userId" +
		"	 and asz.width = :width" +
		"    and asz.height = :height";

	@Query(value=PROVIDER_QUERY, nativeQuery=true)
	public List<Provider> findUserProviders(@Param("userId") int userid, @Param("width") int width, @Param("height") int height);

}
