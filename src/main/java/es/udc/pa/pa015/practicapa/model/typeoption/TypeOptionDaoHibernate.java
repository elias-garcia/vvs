package es.udc.pa.pa015.practicapa.model.typeoption;

import es.udc.pojo.modelutil.dao.GenericDaoHibernate;

import org.springframework.stereotype.Repository;

/**
 * Type option repository.
 */
@Repository("typeOptionDao")
public class TypeOptionDaoHibernate extends
    GenericDaoHibernate<TypeOption, Long> implements TypeOptionDao {

}
