package es.udc.pa.pa015.practicapa.model.categoryinfo;

import es.udc.pojo.modelutil.dao.GenericDao;

import java.util.List;

public interface CategoryInfoDao extends GenericDao<CategoryInfo, Long> {

  public List<CategoryInfo> findAllCategories();

}
