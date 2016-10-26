package es.udc.pa.pa015.practicapa.web.pages.user;

import java.util.Locale;
import java.text.DateFormat;
import java.text.Format;
import java.text.NumberFormat;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.ioc.annotations.Inject;

import es.udc.pa.pa015.practicapa.model.betservice.BetInfoBlock;
import es.udc.pa.pa015.practicapa.model.bettype.BetType;
import es.udc.pa.pa015.practicapa.model.typeoption.TypeOption;
import es.udc.pa.pa015.practicapa.model.betservice.BetService;
import es.udc.pa.pa015.practicapa.model.betinfo.BetInfo;
import es.udc.pa.pa015.practicapa.model.userservice.UserService;
import es.udc.pa.pa015.practicapa.web.services.AuthenticationPolicy;
import es.udc.pa.pa015.practicapa.web.services.AuthenticationPolicyType;
import es.udc.pa.pa015.practicapa.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@AuthenticationPolicy(AuthenticationPolicyType.NON_ADMIN_AUTHENTICATED_USERS)
public class BetHistory {

	private final static int BETS_PER_PAGE = 10;
	
	private int startIndex = 0;
	
	@Property
	@SessionState(create=false)
    private UserSession userSession;
	
	@Inject
	private UserService userService;
	
	@Property
	private BetInfoBlock betInfoBlock;
	
	@Property
	private BetInfo betInfo;
	
	@Property
	private BetType betType;
	
	@Property
	private TypeOption typeOption;

	@Inject
	private BetService betService;
	
	@Inject
	private Locale locale;

	public DateFormat getDateFormat() {
		return DateFormat.getDateInstance(DateFormat.SHORT, locale);
	}
	
	public DateFormat getTimeFormat() {
		return DateFormat.getTimeInstance(DateFormat.SHORT, locale);
	}
	
	public Object[] getPreviousLinkContext() {
		
		if (startIndex-BETS_PER_PAGE >= 0) {
			return new Object[] {startIndex - BETS_PER_PAGE};
		} else {
			return null;
		}
		
	}
	public Format getFormat(){
		return NumberFormat.getInstance(locale); 
	}
	public Object[] getNextLinkContext() {
		
		if (betInfoBlock.isExistMoreBets()) {
			return new Object[] {startIndex + BETS_PER_PAGE};
		} else {
			return null;
		}
		
	}
	public Double getGain(){
		return betInfo.getAmount() * betInfo.getOption().getOdd();
	}
	
	Object[] onPassivate() {
		return new Object[] {startIndex};
	}
	
	void onActivate(int startIndex) throws InstanceNotFoundException {
		this.startIndex = startIndex;
		betInfoBlock = betService.findBetsByUserId(userSession.getUserProfileId(),
			startIndex, BETS_PER_PAGE);
	}

}
