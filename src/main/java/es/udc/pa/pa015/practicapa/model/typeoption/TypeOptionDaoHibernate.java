package es.udc.pa.pa015.practicapa.model.typeoption;

import org.springframework.stereotype.Repository;

import es.udc.pojo.modelutil.dao.GenericDaoHibernate;

@Repository("typeOptionDao")
public class TypeOptionDaoHibernate extends GenericDaoHibernate<TypeOption, Long> implements TypeOptionDao {

}