package es.udc.pa.pa015.practicapa.model.categoryinfo;

import java.util.List;

import org.springframework.stereotype.Repository;

import es.udc.pojo.modelutil.dao.GenericDaoHibernate;

@Repository("categoryInfoDao")
public class CategoryInfoDaoHibernate extends GenericDaoHibernate<CategoryInfo, Long> implements CategoryInfoDao {

	@SuppressWarnings("unchecked")
	public List<CategoryInfo> findAllCategories() {
		
		return getSession().createQuery(
				"SELECT c " +
				"FROM CategoryInfo c " +
				"ORDER BY c.categoryName").
				list();
	}
	
}