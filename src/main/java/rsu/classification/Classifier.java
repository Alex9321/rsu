package rsu.classification;

import weka.classifiers.bayes.NaiveBayes;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class Classifier {

	public static void main(String[] args) throws Exception {
		NaiveBayes naiveBayes = new NaiveBayes();
		DataSource source = new DataSource("weka/driverIntentTrainingModel.arff");
		Instances instances = source.getDataSet();
		instances.setClassIndex(instances.numAttributes() - 1);

		naiveBayes.buildClassifier(instances);

		Instance instance = new DenseInstance(6);
		instance.setValue(new Attribute("speed1", 0), 12.56);
		instance.setValue(new Attribute("speed2", 1), 10.519);
		instance.setValue(new Attribute("speed3", 2), 8.519);
		instance.setValue(new Attribute("speed4", 3), 6.519);
		instance.setValue(new Attribute("speed5", 4), 4.519);
		instance.setValue(new Attribute("speed6", 5), 2.519);
		instance.setDataset(instances);

		System.out.println(naiveBayes.classifyInstance(instance));

		naiveBayes.distributionForInstance(instance);
	}

}
