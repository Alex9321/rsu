package rsu.classification;

import org.jfree.data.xy.XYSeries;
import org.springframework.stereotype.Component;
import rsu.charts.ChartCreator;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

@Component
public class Classifier {

	public boolean stops(XYSeries speeds) throws Exception {
		ChartCreator chartCreator = new ChartCreator();
		NaiveBayes naiveBayes = new NaiveBayes();
		DataSource source = new DataSource("weka/driverIntentTrainingModel.arff");
		Instances instances = source.getDataSet();
		instances.setClassIndex(instances.numAttributes() - 1);

		naiveBayes.buildClassifier(instances);

		Instance instance = new DenseInstance(25);

		for (int i = 40; i < 65; i++) {
			instance.setValue(new Attribute("speed" + (i - 39), i - 40), chartCreator.interpolate(speeds, i));
		}
		instance.setDataset(instances);

		return naiveBayes.classifyInstance(instance) == 0.0;
	}

}
