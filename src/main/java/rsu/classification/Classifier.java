package rsu.classification;

import org.jfree.data.xy.XYSeries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rsu.dto.VehicleData;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;

import java.util.List;

@Component
public class Classifier {

	@Autowired
	private DataSetManager dataSetManager;

	private Instances trainInstances1;

	private Instances trainInstances2;

	private Instances trainInstances3;

	private NaiveBayes classifierFirstPart;

	private NaiveBayes classifierMidPart;

	private NaiveBayes classifierLastPart;

	public Classifier() throws Exception {
		classifierFirstPart = new NaiveBayes();
		classifierMidPart = new NaiveBayes();
		classifierLastPart = new NaiveBayes();
		ConverterUtils.DataSource source = new ConverterUtils.DataSource("weka/meteor/trainingModel1.arff");
		trainInstances1 = source.getDataSet();
		trainInstances1.setClassIndex(trainInstances1.numAttributes() - 1);
		classifierFirstPart.buildClassifier(trainInstances1);
		source = new ConverterUtils.DataSource("weka/meteor/trainingModel2.arff");
		trainInstances2 = source.getDataSet();
		trainInstances2.setClassIndex(trainInstances1.numAttributes() - 1);
		classifierMidPart.buildClassifier(trainInstances2);
		source = new ConverterUtils.DataSource("weka/meteor/trainingModel3.arff");
		trainInstances3 = source.getDataSet();
		trainInstances3.setClassIndex(trainInstances1.numAttributes() - 1);
		classifierLastPart.buildClassifier(trainInstances3);
	}

	public boolean stops(XYSeries speeds, int dayPeriod) throws Exception {
		Instance instance = new DenseInstance(30);

		for (int i = 70; i < 100; i++) {
			instance.setValue(new Attribute("speed" + (i - 69), i - 70), dataSetManager.interpolate(speeds, i));
		}

		double classification = 0.0;

		switch (dayPeriod) {
		case 1:
			instance.setDataset(trainInstances1);
			classification = classifierFirstPart.classifyInstance(instance);
			break;
		case 2:
			instance.setDataset(trainInstances2);
			classification = classifierMidPart.classifyInstance(instance);
			break;
		case 3:
			instance.setDataset(trainInstances3);
			classification = classifierLastPart.classifyInstance(instance);
			break;
		default:
			break;
		}

		return classification == 0.0;
	}

	public void reTrain(List<List<VehicleData>> vehicleModel, int dayPeriod, String scenario) throws Exception {
		dataSetManager.createTrainingData(vehicleModel, scenario, dayPeriod);

		ConverterUtils.DataSource source;

		switch (dayPeriod) {
		case 1:
			source = new ConverterUtils.DataSource("weka/" + scenario + "/trainingModel1.arff");
			trainInstances1 = source.getDataSet();
			trainInstances1.setClassIndex(trainInstances1.numAttributes() - 1);
			classifierFirstPart.buildClassifier(trainInstances1);
			break;
		case 2:
			source = new ConverterUtils.DataSource("weka/" + scenario + "/trainingModel2.arff");
			trainInstances2 = source.getDataSet();
			trainInstances2.setClassIndex(trainInstances1.numAttributes() - 1);
			classifierFirstPart.buildClassifier(trainInstances2);
			break;
		case 3:
			source = new ConverterUtils.DataSource("weka/" + scenario + "/trainingModel3.arff");
			trainInstances3 = source.getDataSet();
			trainInstances3.setClassIndex(trainInstances1.numAttributes() - 1);
			classifierFirstPart.buildClassifier(trainInstances3);
			break;
		default:
			break;
		}
	}

}
