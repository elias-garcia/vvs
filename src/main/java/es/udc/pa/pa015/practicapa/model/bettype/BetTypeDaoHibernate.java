package es.udc.pa.pa015.practicapa.model.bettype;

import es.udc.pojo.modelutil.dao.GenericDaoHibernate;

import org.springframework.stereotype.Repository;

@Repository("betTypeDao")
public class BetTypeDaoHibernate extends GenericDaoHibernate<BetType, Long>
    implements BetTypeDao {

}
