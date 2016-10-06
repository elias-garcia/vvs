package es.udc.pa.pa015.practicapa.model.betinfo;

import java.util.List;
import es.udc.pojo.modelutil.dao.GenericDao;

public interface BetInfoDao extends GenericDao<BetInfo, Long> {

	public List<BetInfo> findBetsByUserId(Long userId,
			int startindex, int count);
}