package es.udc.pa.pa015.practicapa.model.categoryinfo;

import es.udc.pojo.modelutil.dao.GenericDao;

import java.util.List;

/**
 * CategoryInfo dao.
 */
public interface CategoryInfoDao extends GenericDao<CategoryInfo, Long> {

  /**
   * Find all the categories.
   * @return list of categories
   */
  List<CategoryInfo> findAllCategories();

}
