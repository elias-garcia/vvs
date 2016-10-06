package es.udc.pa.pa015.practicapa.model.betservice;

import java.util.Calendar;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.pa.pa015.practicapa.model.betinfo.BetInfo;
import es.udc.pa.pa015.practicapa.model.betinfo.BetInfoDao;
import es.udc.pa.pa015.practicapa.model.bettype.BetType;
import es.udc.pa.pa015.practicapa.model.bettype.BetTypeDao;
import es.udc.pa.pa015.practicapa.model.typeoption.TypeOption;
import es.udc.pa.pa015.practicapa.model.typeoption.TypeOptionDao;
import es.udc.pa.pa015.practicapa.model.userprofile.UserProfile;
import es.udc.pa.pa015.practicapa.model.userprofile.UserProfileDao;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@Service("BetService")
@Transactional
public class BetServiceImpl implements BetService {

	@Autowired
	private BetInfoDao betInfoDao;
	
	@Autowired
	private BetTypeDao betTypeDao;
	
	@Autowired
	private TypeOptionDao typeOptionDao;
	
	@Autowired
	private UserProfileDao userProfileDao;
	
	public BetInfo createBet(Long userId, Long typeOptionId, double amount)
			throws InstanceNotFoundException {
		
		UserProfile currentUser = userProfileDao.find(userId);
		TypeOption currentOption = typeOptionDao.find(typeOptionId);
		
		BetInfo newBet = new BetInfo(Calendar.getInstance(),amount, currentUser, currentOption);
		
		betInfoDao.save(newBet);
		
		return newBet;
	}

	@Transactional(readOnly=true)
	public BetInfoBlock findBetsByUserId(Long userId, int startindex, int count)
			throws InstanceNotFoundException {

		userProfileDao.find(userId);

		/*
		 * Find count+1 bets to determine if there exist more bets above
		 * the specified range.
		 */
		List<BetInfo> bets = betInfoDao.findBetsByUserId(userId, startindex, count + 1);

		boolean existMoreBets = bets.size() == (count + 1);

		/*
		 * Remove the last bet from the returned list if there exist more
		 * bets above the specified range.
		 */
		if (existMoreBets) {
			bets.remove(bets.size() - 1);
		}

		/* Return BetInfoBlock. */
		return new BetInfoBlock(bets, existMoreBets);
	}

	public void changeOptionStatus(List<Long> optionsIds)
			throws InstanceNotFoundException, TypeNotMultipleException {
		/*
		TypeOption firstOption = typeOptionDao.find(optionsIds.get(0));
		BetType type = betTypeDao.find(option.getType().getTypeId());
		
		/* If we want to pick more than one option as a winner or loser but
		 * it type doesn't allow multiple options, throw an exception.
		 */
		/*if (!type.getIsMultiple() && optionsIds.size() > 1)
			throw new TypeNotMultipleException(type.getTypeId());
		
		Set<TypeOption> typeOptions = type.getTypeOptions();
		
		/* If optionsIds is null or empty, then pick all options to false. */
		
		/*for (TypeOption option : typeOptions)
	*/		
	}
}
