package es.udc.pa.pa015.practicapa.test.model.typeoption;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

import org.graphwalker.core.condition.EdgeCoverage;
import org.graphwalker.core.condition.TimeDuration;
import org.graphwalker.core.generator.RandomPath;
import org.graphwalker.core.machine.ExecutionContext;
import org.graphwalker.java.test.TestBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import es.udc.pa.pa015.practicapa.model.bettype.BetType;
import es.udc.pa.pa015.practicapa.model.bettype.BetTypeDao;
import es.udc.pa.pa015.practicapa.model.categoryinfo.CategoryInfo;
import es.udc.pa.pa015.practicapa.model.categoryinfo.CategoryInfoDao;
import es.udc.pa.pa015.practicapa.model.eventinfo.EventInfo;
import es.udc.pa.pa015.practicapa.model.eventinfo.EventInfoDao;
import es.udc.pa.pa015.practicapa.model.typeoption.TypeOption;
import es.udc.pa.pa015.practicapa.model.typeoption.TypeOptionDao;

public class PickWinnersStatesTest extends ExecutionContext implements PickWinnersStates {

	public final static Path MODEL_PATH = Paths
			.get("es/udc/pa/pa015/practicapa/testautomation/PickWinnersStates.graphml");

	@Autowired
	CategoryInfoDao categoryInfoDao;

	@Autowired
	EventInfoDao eventInfoDao;

	@Autowired
	BetTypeDao betTypeDao;

	@Autowired
	TypeOptionDao typeOptionDao;

	CategoryInfo category;
	EventInfo event;
	BetType betType;
	TypeOption typeOption;

	@Override
	public void e_Init() {
		// Category initialization
		category = new CategoryInfo("category1");
		categoryInfoDao.save(category);

		// Event initialization
		event = new EventInfo("Barça-Madrid", new GregorianCalendar(2010, Calendar.FEBRUARY, 22, 23, 11, 44), category);
		eventInfoDao.save(event);

		// BetType initialization
		betType = new BetType("¿Quien ganará?", true, event);
		betTypeDao.save(betType);
		event.addBetType(betType);

		// TypeOption initialization
		typeOption = new TypeOption(2, "typeOption", betType);
		typeOptionDao.save(typeOption);
		betType.addTypeOption(typeOption);
	}

	@Override
	public void v_WinnersNotPicked() {
		assertFalse(typeOption.getIsWinner());
	}

	@Override
	public void v_WinnersPicked() {
		typeOption.setIsWinner(true);
		typeOptionDao.save(typeOption);
	}

	@Override
	public void e_PickWinners() {
		assertTrue(typeOption.getIsWinner());
	}

	// @Test
	public void runFunctionalTest() {
		new TestBuilder().addModel(MODEL_PATH,
				new PickWinnersStatesTest().setPathGenerator(new RandomPath(new EdgeCoverage(100)))).execute();
	}

	// @Test
	public void runStabilityTest() {
		new TestBuilder().addModel(MODEL_PATH,
				new PickWinnersStatesTest().setPathGenerator(new RandomPath(new TimeDuration(30, TimeUnit.SECONDS))))
				.execute();
	}

}
