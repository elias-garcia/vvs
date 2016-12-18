package es.udc.pa.pa015.practicapa.model.categoryinfo;

import es.udc.pojo.modelutil.dao.GenericDaoHibernate;

import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * CategoryInfoDaoHibernate.
 */
@Repository("categoryInfoDao")
public class CategoryInfoDaoHibernate extends
    GenericDaoHibernate<CategoryInfo, Long> implements CategoryInfoDao {

  /**
   * This method find all the categories.
   * @return list of categories.
   */
  @SuppressWarnings("unchecked")
  public final List<CategoryInfo> findAllCategories() {

    return getSession().createQuery("SELECT c " + "FROM CategoryInfo c "
        + "ORDER BY c.categoryName").list();
  }

}
