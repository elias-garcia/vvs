package es.udc.pa.pa015.practicapa.model.betservice;

import java.util.List;

import es.udc.pa.pa015.practicapa.model.betinfo.BetInfo;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

public interface BetService {

	public BetInfo createBet(Long userId, Long typeOptionId, double amount)
			throws InstanceNotFoundException;
	
	public BetInfoBlock findBetsByUserId(Long userId, int startindex, int count)
		throws InstanceNotFoundException;
	
	public void changeOptionStatus(List<Long> optionsIds)
		throws InstanceNotFoundException, TypeNotMultipleException;
}