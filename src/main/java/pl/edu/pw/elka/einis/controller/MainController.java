package pl.edu.pw.elka.einis.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class MainController {
	
	static Logger logger = LogManager.getLogger(MainController.class);
	
	@FXML private Slider polyNumSlider;
	@FXML private Slider iterationNumSlider;
	@FXML private Slider populationNumSlider;
	@FXML private Slider succNumSlider;
	
	@FXML private Button deleteGenChartsButton;
	@FXML private Button clearChartButton;
	@FXML private Button runGenAlghoritmButton;
	
	@FXML private LineChart<Number, Number> chart;
	
	@FXML
	protected void runGenAlghoritm(ActionEvent event) {
		logger.debug("Running genetic alhoritm.");
		// TODO
	}
	
	@FXML
	protected void clearAllChart(ActionEvent event) {
		logger.debug("Clearing all data from chart.");
		chart.getData().clear();
	}
	
	@FXML
	protected void deleteGeneratedCharts(ActionEvent event) {
		logger.debug("Clearing generated data from chart.");
		for(int i=1; i<chart.getData().size(); i++){
			chart.getData().remove(i);
		}
	}
	
	@FXML
	protected void chartMouseClicked(MouseEvent event) {
		
		if(chart.getData().isEmpty()) {
			LineChart.Series series = new LineChart.Series();
			series.setName("Requested points");
			chart.getData().add(series);
		}
		
		double x = (double) chart.getXAxis().getValueForDisplay(event.getX() - (chart.getYAxis().getTickLength() + chart.getXAxis().getLayoutX()));
		double y = (double) chart.getYAxis().getValueForDisplay(event.getY() - (chart.getXAxis().getTickLength() + chart.getYAxis().getLayoutY())); 
		
		logger.debug("Chart clicked. Adding new point: (" + x + ":" + y + ")");
		chart.getData().get(0).getData().add(new XYChart.Data(x, y));
	}

}
