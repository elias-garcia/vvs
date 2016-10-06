package es.udc.pa.pa015.practicapa.model.betservice;

import java.util.List;
import es.udc.pa.pa015.practicapa.model.betinfo.BetInfo;

public class BetInfoBlock {

	private List<BetInfo> bets;
	private boolean existMoreBets;
	
	public BetInfoBlock(List<BetInfo> bets, boolean existMoreBets) {
		this.bets = bets;
		this.existMoreBets = existMoreBets;
	}

	public List<BetInfo> getBets() {
		return bets;
	}

	public boolean isExistMoreBets() {
		return existMoreBets;
	}
}
