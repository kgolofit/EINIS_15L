package pl.edu.pw.elka.einis.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import pl.edu.pw.elka.einis.algorithm.Algorithm;
import pl.edu.pw.elka.einis.algorithm.AlgorithmParameters;
import pl.edu.pw.elka.einis.entity.Point;
import pl.edu.pw.elka.einis.entity.Polynomial;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
	
	@FXML private ProgressBar progressBar;
	
	@FXML private LineChart<Number, Number> chart;
	
	@FXML
	protected void runGenAlgorithm(ActionEvent event) {
		// Zebranie parametrów
		AlgorithmParameters params = new AlgorithmParameters(
				(int)polyNumSlider.getValue(),
				(int)iterationNumSlider.getValue(),
				(int)populationNumSlider.getValue(),
				(int)succNumSlider.getValue()
		);
		logger.debug("Running algorithm; degree: " + params.polynomialDegree());
		progressBar.setVisible(true);
		List<Point> points = chart.getData().get(0).getData().stream()
				.map(data -> new Point(data.getXValue().doubleValue(), data.getYValue().doubleValue()))
				.collect(Collectors.toList());
		Algorithm algorithm = new Algorithm();
		Polynomial result = algorithm.solve(points, params);
		logger.debug("Evolved polynomial: " + result);
		progressBar.setVisible(false);
		drawPolynomial(result);
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
		chart.getData().get(0).getData().add(new XYChart.Data<>(x, y));
	}

	/**
	 * Rysuje wykres wielomianu
	 *
	 * @param polynomial
	 */
	private void drawPolynomial(final Polynomial polynomial) {
		NumberAxis xAxis = (NumberAxis) chart.getXAxis();
		double lowerBound = xAxis.getLowerBound();
		double upperBound = xAxis.getUpperBound();
		double step = (upperBound - lowerBound) / 100;
		LineChart.Series series = new LineChart.Series();
		series.setName("Experiment result: " + new Date().toString());
		for (double x = lowerBound; x <= upperBound; x += step) {
			double y = polynomial.calculate(x);
			series.getData().add(new XYChart.Data<>(x, y));
		}
		chart.getData().add(series);
	}
}
