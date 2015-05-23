package pl.edu.pw.elka.einis.controller;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;

public class MainController {
	
	@FXML private Label polyLabel;
	@FXML private Slider polySlider;
	@FXML private Button runButton;
	@FXML private LineChart<Number, Number> chart;
	
	@FXML
	protected void xAxisClicked(MouseEvent event) {
		
	}
	
	@FXML
	protected void yAxisClicked(MouseEvent event) {
		
	}
	
	@FXML
	protected void chartMouseClicked(MouseEvent event) {
		
		System.out.println("Chart Clicked. Position: " + event.getX() + ":" + event.getY());
		
		if(chart.getData().isEmpty()) {
			LineChart.Series series = new LineChart.Series();
			series.setName("Requested points");
			chart.getData().add(series);
		}
		
		double x = (double) chart.getXAxis().getValueForDisplay(event.getX() - (chart.getYAxis().getTickLength() + chart.getXAxis().getLayoutX()));
		double y = (double) chart.getYAxis().getValueForDisplay(event.getY() - (chart.getXAxis().getTickLength() + chart.getYAxis().getLayoutY())); 
		
		chart.getData().get(0).getData().add(new XYChart.Data(x, y));
	}

}
