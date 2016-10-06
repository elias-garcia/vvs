package es.udc.pa.pa015.practicapa.model.categoryinfo;

import java.util.List;

import es.udc.pojo.modelutil.dao.GenericDao;

public interface CategoryInfoDao extends GenericDao<CategoryInfo, Long> {
	
	public List<CategoryInfo> findAllCategories();
	
}