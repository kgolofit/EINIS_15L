package pl.edu.pw.elka.einis.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import pl.edu.pw.elka.einis.algorithm.Algorithm;
import pl.edu.pw.elka.einis.algorithm.AlgorithmParameters;
import pl.edu.pw.elka.einis.entity.Point;
import pl.edu.pw.elka.einis.entity.Polynomial;

public class MainController {
	
	static Logger logger = LogManager.getLogger(MainController.class);
	
	private static final String USER_POINTS_NAME = "Punkty użytkownika";
	private static final int POLY_MAX_ = 12;
	private static final int POLY_MIN_ = 2;
	private static final int ITERATION_MAX_ = 100000;
	private static final int ITERATION_MIN_ = 20;
	private static final int POPULATION_MAX_ = 200;
	private static final int POPULATION_MIN_ = 5;
	private static final int SUCC_MAX_ = 200;
	private static final int SUCC_MIN_ = 5;
	
	@FXML private Slider polyNumSlider;
	@FXML private Slider iterationNumSlider;
	@FXML private Slider populationNumSlider;
	@FXML private Slider succNumSlider;
	
	@FXML private TextField polyNumText;
	@FXML private TextField iterationNumText;
	@FXML private TextField populationNumText;
	@FXML private TextField succNumText;
	
	@FXML private Button deleteGenChartsButton;
	@FXML private Button clearChartButton;
	@FXML private Button runGenAlghoritmButton;
	
	@FXML private ProgressBar progressBar;
	
	@FXML private LineChart<Number, Number> chart;

	private Algorithm algorithm;
	
	@FXML
	protected void runGenAlgorithm(ActionEvent event) {
		
		if(chart.getData().size() <= 0) {
			return;
		}
		
		// Zebranie parametrów
		AlgorithmParameters params = new AlgorithmParameters(
				(int)polyNumSlider.getValue(),
				(int)iterationNumSlider.getValue(),
				(int)populationNumSlider.getValue(),
				(int)succNumSlider.getValue()
		);
		logger.debug("Running algorithm; degree: " + params.polynomialDegree());
		List<Point> points = chart.getData().get(0).getData().stream()
				.map(data -> new Point(data.getXValue().doubleValue(), data.getYValue().doubleValue()))
				.collect(Collectors.toList());
		Algorithm algorithm = getAlgorithm();
		Polynomial result = algorithm.solve(points, params);
		logger.debug("Evolved polynomial: " + result);
		drawPolynomial(result);
	}
	
	@FXML
	protected void chartScroll(ScrollEvent event) {
		double scrollValue = event.getDeltaY();
		
		NumberAxis xAxis = (NumberAxis) chart.getXAxis();
		NumberAxis yAxis = (NumberAxis) chart.getYAxis();
		
		double xLowerBound = xAxis.getLowerBound();
		double xUpperBound = xAxis.getUpperBound();
		
		double yLowerBound = yAxis.getLowerBound();
		double yUpperBound = yAxis.getUpperBound();
		
		xAxis.setLowerBound(scrollValue > 0 ? xLowerBound/2 : xLowerBound*2);
		xAxis.setUpperBound(scrollValue > 0 ? xUpperBound/2 : xUpperBound*2);
		yAxis.setLowerBound(scrollValue > 0 ? yLowerBound/2 : yLowerBound*2);
		yAxis.setUpperBound(scrollValue > 0 ? yUpperBound/2 : yUpperBound*2);
	}
	
	@FXML
	protected void clearAllChart(ActionEvent event) {
		logger.debug("Clearing all data from chart.");
		chart.getData().clear();
	}
	
	@FXML
	protected void deleteGeneratedCharts(ActionEvent event) {
		logger.debug("Clearing generated data from chart.");
		while(chart.getData().size() > 1) {
			chart.getData().remove(1);
		}
//		for(int i=1; i<chart.getData().size(); i++){
//			chart.getData().remove(i);
//		}
	}
	
	/* --------------------------------------- */
	/* Sliders and textFields change listeners */
	
	@FXML
	protected void polyNumSliderChanged(MouseEvent event) {
		polyNumText.setText(String.valueOf(((int)polyNumSlider.getValue())));
	}
	
	@FXML
	protected void polyNumTextChanged(KeyEvent event) {
		try {
			int tValue = Integer.parseInt(polyNumText.getText());
			if(tValue > POLY_MAX_) {
				polyNumSlider.setValue(POLY_MAX_);
				polyNumText.setText(String.valueOf(POLY_MAX_));
			}
			else if (tValue < POLY_MIN_) {
				polyNumSlider.setValue(POLY_MIN_);
				polyNumText.setText(String.valueOf(POLY_MIN_));
			}
			else {
				polyNumSlider.setValue(tValue);
			}
		}
		catch(NumberFormatException e) {
			polyNumSlider.setValue(POLY_MIN_);
		}
	}
	
	@FXML
	protected void iterationNumSliderChanged(MouseEvent event) {
		iterationNumText.setText(String.valueOf(((int)iterationNumSlider.getValue())));
	}
	
	@FXML
	protected void iterationNumTextChanged(KeyEvent event) {
		try {
			int tValue = Integer.parseInt(iterationNumText.getText());
			if(tValue > ITERATION_MAX_) {
				iterationNumSlider.setValue(ITERATION_MAX_);
				iterationNumText.setText(String.valueOf(ITERATION_MAX_));
			}
			else if (tValue < ITERATION_MIN_) {
				iterationNumSlider.setValue(ITERATION_MIN_);
				iterationNumText.setText(String.valueOf(ITERATION_MIN_));
			}
			else {
				iterationNumSlider.setValue(tValue);
			}
		}
		catch(NumberFormatException e) {
			iterationNumSlider.setValue(ITERATION_MIN_);
		}
	}
	
	@FXML
	protected void populationNumSliderChanged(MouseEvent event) {
		populationNumText.setText(String.valueOf(((int)populationNumSlider.getValue())));
	}
	
	@FXML
	protected void populationNumTextChanged(KeyEvent event) {
		try {
			int tValue = Integer.parseInt(populationNumText.getText());
			if(tValue > POPULATION_MAX_) {
				populationNumSlider.setValue(POPULATION_MAX_);
				populationNumText.setText(String.valueOf(POPULATION_MAX_));
			}
			else if (tValue < POPULATION_MIN_) {
				populationNumSlider.setValue(POPULATION_MIN_);
				populationNumText.setText(String.valueOf(POPULATION_MIN_));
			}
			else {
				populationNumSlider.setValue(tValue);
			}
		}
		catch(NumberFormatException e) {
			populationNumSlider.setValue(POPULATION_MIN_);
		}
	}
	
	@FXML
	protected void succNumSliderChanged(MouseEvent event) {
		succNumText.setText(String.valueOf(((int)succNumSlider.getValue())));
	}
	
	@FXML
	protected void succNumTextChanged(KeyEvent event) {
		try {
			int tValue = Integer.parseInt(succNumText.getText());
			if(tValue > SUCC_MAX_) {
				succNumSlider.setValue(SUCC_MAX_);
				succNumText.setText(String.valueOf(SUCC_MAX_));
			}
			else if (tValue < SUCC_MIN_) {
				succNumSlider.setValue(SUCC_MIN_);
				succNumText.setText(String.valueOf(SUCC_MIN_));
			}
			else {
				succNumSlider.setValue(tValue);
			}
		}
		catch(NumberFormatException e) {
			succNumSlider.setValue(SUCC_MIN_);
		}
	}
	
	/* End of Sliders and textFields change listeners */
	/* ---------------------------------------------- */
	
	@FXML
	protected void chartMouseClicked(MouseEvent event) {
		if(chart.getData().isEmpty()) {
			LineChart.Series series = new LineChart.Series();
			series.setName(USER_POINTS_NAME);
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
		DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
		series.setName("Wielomian " + polynomial.getDegree() + " st " + formatter.format(new Date()));
		for (double x = lowerBound; x <= upperBound; x += step) {
			double y = polynomial.calculate(x);
			series.getData().add(new XYChart.Data<>(x, y));
		}
		chart.getData().add(series);
	}

	private Algorithm getAlgorithm() {
		if (algorithm == null) {
			algorithm = new Algorithm();
			algorithm.progressListener_$eq((progress) -> {
				progressBar.setProgress(progress);
				logger.debug("Progress: " + progress + "%");
			});
		}
		return algorithm;
	}
}
