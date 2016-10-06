package es.udc.pa.pa015.practicapa.model.bettype;

import org.springframework.stereotype.Repository;

import es.udc.pojo.modelutil.dao.GenericDaoHibernate;

@Repository("betTypeDao")
public class BetTypeDaoHibernate extends GenericDaoHibernate<BetType, Long> implements BetTypeDao {

}