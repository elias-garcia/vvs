package es.udc.pa.pa015.practicapa.web.pages.search;

import java.text.Format;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.text.DateFormat;

import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.ioc.annotations.Inject;

import es.udc.pa.pa015.practicapa.model.betservice.BetInfoBlock;
import es.udc.pa.pa015.practicapa.model.betservice.BetService;
import es.udc.pa.pa015.practicapa.model.betinfo.BetInfo;
import es.udc.pa.pa015.practicapa.model.userservice.UserService;
import es.udc.pa.pa015.practicapa.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

public class BetHistory {

	private final static int BETS_PER_PAGE = 10;
	private int startIndex = 0;	
	@SessionState(create=false)
    private UserSession userSession;
	
	@Inject
	private UserService userService;
	
	private BetInfoBlock betInfoBlock;
	private BetInfo betInfo;

	@Inject
	private BetService betService;
	
	@Inject
	private Locale locale;
	
	public List<BetInfo> getBets() {
		return betInfoBlock.getBets();
	}
	
	public BetInfo getBetInfo() {
		return betInfo;
	}

	public void setBetInfo(BetInfo bet) {
		this.betInfo = bet;
	}
	
	public Format getFormat() {
		return NumberFormat.getInstance(locale);
	}
	public DateFormat getDateFormat() {
		return DateFormat.getDateInstance(DateFormat.SHORT, locale);
	}
	
	public Object[] getPreviousLinkContext() {
		
		if (startIndex-BETS_PER_PAGE >= 0) {
			return new Object[] {startIndex-BETS_PER_PAGE};
		} else {
			return null;
		}
		
	}
	
	public Object[] getNextLinkContext() {
		
		if (betInfoBlock.isExistMoreBets()) {
			return new Object[] {startIndex+BETS_PER_PAGE};
		} else {
			return null;
		}
		
	}
	
	Object[] onPassivate() {
		return new Object[] {startIndex};
	}
	
	void onActivate(int startIndex)throws InstanceNotFoundException {
		this.startIndex = startIndex;
		betInfoBlock = betService.findBetsByUserId(userSession
                .getUserProfileId(),
			startIndex, BETS_PER_PAGE);
	}

}
