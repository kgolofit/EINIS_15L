package pl.edu.pw.elka.einis.controller;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import pl.edu.pw.elka.einis.algorithm.Algorithm;
import pl.edu.pw.elka.einis.entity.Point;
import pl.edu.pw.elka.einis.entity.Polynomial;
import scala.collection.JavaConversions;
import scala.collection.JavaConverters;

import java.util.List;
import java.util.stream.Collectors;

public class MainController {
	
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
		System.out.println("Running algorithm...");
		List<Point> points = chart.getData().get(0).getData().stream()
				.map(data -> new Point(data.getXValue().doubleValue(), data.getYValue().doubleValue()))
				.collect(Collectors.toList());
		Algorithm algorithm = new Algorithm();
		Polynomial result = algorithm.solve(points, 5); //FIXME - hardcode
		System.out.println("Evolved polynomial: " + result);
	}
	
	@FXML
	protected void clearAllChart(ActionEvent event) {
		chart.getData().clear();
	}
	
	@FXML
	protected void deleteGeneratedCharts(ActionEvent event) {
		for(int i=1; i<chart.getData().size(); i++){
			chart.getData().remove(i);
		}
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
