package es.udc.pa.pa015.practicapa.test.model.typeoption;

// Generated by GraphWalker (http://www.graphwalker.org)
import org.graphwalker.java.annotation.Edge;
import org.graphwalker.java.annotation.Model;
import org.graphwalker.java.annotation.Vertex;

@Model(file = "es/udc/pa/pa015/practicapa/testautomation/PickWinnersStates.graphml")
public interface PickWinnersStates {

	@Edge()
	void e_Init();

	@Vertex()
	void v_WinnersNotPicked();

	@Vertex()
	void v_WinnersPicked();

	@Edge()
	void e_PickWinners();

}