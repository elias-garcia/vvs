package es.udc.pa.pa015.practicapa.model.categoryinfo;

import es.udc.pojo.modelutil.dao.GenericDaoHibernate;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("categoryInfoDao")
public class CategoryInfoDaoHibernate extends
    GenericDaoHibernate<CategoryInfo, Long> implements CategoryInfoDao {

  /**
   * This method find all the categories.
   */
  @SuppressWarnings("unchecked")
  public List<CategoryInfo> findAllCategories() {

    return getSession().createQuery("SELECT c " + "FROM CategoryInfo c "
        + "ORDER BY c.categoryName").list();
  }

}
