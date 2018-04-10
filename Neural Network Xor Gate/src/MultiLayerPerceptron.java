import java.io.Serializable;
import java.lang.Math;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Implements backpropagation, saves edge in files.
 * @Author : Deepak Ahire
 * B.Tech CSE
 * Walchand College Of Engineering, Sangli.
 */
 
public class MultiLayerPerceptron implements Serializable{
    /**
     * Example of the network solving the XOR-problem
     */
    public static void main(String[] args){
        MultiLayerPerceptron mlp = new MultiLayerPerceptron(2, 5, 1, 1, 1.0);
        double[][] xor = new double[4][2];
        xor[0][0] = 0;
        xor[0][1] = 0;
        xor[1][0] = 1;
        xor[1][1] = 0;
        xor[2][0] = 0;
        xor[2][1] = 1;
        xor[3][0] = 1;
        xor[3][1] = 1;
        Random r = new Random();
        for(int i = 0; i <= 100000; i++){
            double[] input = xor[r.nextInt(4)];
            double[] target = new double[]{((int)input[0]+(int)input[1])%2};
            mlp.train(input, target); //input = 4 pairs { (0,0) (0,1) (1,0) (1,1) } target = { (0) (1) (1) (0) }
        }

        //File Operations:

            //Last hidden layer : Saving a Arraylist consisiting of Neuron:
            ArrayList <Neuron> lastHiddenLayer = mlp.hidden.get(mlp.hidden.size()-1);
            FileOperation fileOperation = new FileOperation();
            fileOperation.writeToFile((Object)lastHiddenLayer, "./LastHiddenLayer.obj");

            //Hidden Layers : Saving an Arraylist of ArrayList of Nuerons :
            ArrayList <ArrayList<Neuron>> hiddenLayers = new ArrayList<ArrayList<Neuron>>();
            System.out.println("Number of hidden Layers:" + mlp.hidden.size());
            for(int i=0;i<mlp.hidden.size();i++){
                hiddenLayers.add(mlp.hidden.get(i));
            }
            fileOperation = new FileOperation();
            fileOperation.writeToFile((Object)lastHiddenLayer, "./HiddenLayer.obj");

            //Last input layer : Saving a Arraylist consisiting of Neuron:
            ArrayList <Neuron> inputLayer = mlp.input;
            fileOperation = new FileOperation();
            fileOperation.writeToFile((Object)inputLayer, "./InputLayer.obj");

        //File save complete.


        for(int i = 0; i < 4; i++){
            System.out.println("Classifying "+xor[i][0]+","+xor[i][1]+". Output: "+mlp.classify(xor[i])[0]);
        }
    }

    private double learningrate;
    private ArrayList<ArrayList<Neuron>> hidden;
    private ArrayList<Neuron> input;
    private ArrayList<Neuron> output;
    private HashMap<Neuron,Integer> inputIndex, outputIndex;
    private HashMap<Integer,HashMap<Neuron,Integer>> hiddenIndex;
    private final int RUNMODE = 2;

    /**
     *
     * @param input Size of input layer
     * @param hidden Size if hidden layer(s)
     * @param output Size of output layer
     * @param numberOfHiddenLayers Number of hidden layers
     * @param learningrate Learning rate
     */
    public MultiLayerPerceptron(int input, int hidden, int output, int numberOfHiddenLayers, double learningrate){
        this.hiddenIndex = new HashMap<Integer,HashMap<Neuron,Integer>>(); // First: layer number, Second: Index of the neuron in that layer OR index in the arraylist.
        this.inputIndex = new HashMap<Neuron,Integer>();
        this.outputIndex = new HashMap<Neuron,Integer>();

        this.hidden = new ArrayList<ArrayList<Neuron>>();
        this.input = new ArrayList<Neuron>();
        this.output = new ArrayList<Neuron>();
        this.learningrate = learningrate;
        //Input
        for(int i = 1; i <= input; i++){
            this.input.add(new Neuron(false)); //false = act : 'false' is used for input and output layer who will not use activation function.
        }
        for(Neuron i : this.input){
            this.inputIndex.put(i, this.input.indexOf(i));
        }

        //Hidden
        for(int i = 1; i <= numberOfHiddenLayers; i++){
            ArrayList<Neuron> a = new ArrayList<Neuron>();
            for(int j = 1; j <= hidden; j++){
                a.add(new Neuron(true));
            }
            this.hidden.add(a);
        }
        for(ArrayList<Neuron> a : this.hidden){
            HashMap<Neuron,Integer> put = new HashMap<Neuron,Integer>();
            for(Neuron h : a){
                put.put(h, a.indexOf(h));
            }
            this.hiddenIndex.put(this.hidden.indexOf(a), put);
        }

        //Output
        for(int i = 1; i <= output; i++){
            this.output.add(new Neuron(true));
        }
        for(Neuron o : this.output){
            this.outputIndex.put(o, this.output.indexOf(o));
        }

        if(RUNMODE == 1) {
            //Assigning random weights to neurons of all layers:
            for (Neuron i : this.input) {
                for (Neuron h : this.hidden.get(0)) {
                    i.forbind(h, Math.random() * (Math.random() > 0.5 ? 1 : -1)); //Assigning the random weights with random signs.
                }
            }
            for (int i = 1; i < this.hidden.size(); i++) {
                for (Neuron h : this.hidden.get(i - 1)) {
                    for (Neuron hto : this.hidden.get(i)) {
                        h.forbind(hto, Math.random() * (Math.random() > 0.5 ? 1 : -1));
                    }
                }
            }
            for (Neuron h : this.hidden.get(this.hidden.size() - 1)) {
                for (Neuron o : this.output) {
                    h.forbind(o, Math.random() * (Math.random() > 0.5 ? 1 : -1)); // forbind() creates a new input synapse to a neuron.
                }
            }
        }
        else{
            //Assigning random weights to neurons from the files:
            FileOperation fileOperation = new FileOperation();
            ArrayList<Neuron> inputLayer = new ArrayList<Neuron>();
            inputLayer = (ArrayList<Neuron>) fileOperation.readFromFile("./InputLayer.obj");

            for (int i = 0; i < this.input.size(); i++) {
                for (int j = 0; j < this.hidden.get(0).size(); j++) {
                    this.input.get(i).forbind(this.hidden.get(0).get(j), inputLayer.get(i).forbundetTil.get(j).getWeight()); //Assigning the random weights with random signs.
                }
            }

            fileOperation = new FileOperation();
            ArrayList<ArrayList<Neuron>> hiddenLayer = (ArrayList<ArrayList<Neuron>>) fileOperation.readFromFile("./HiddenLayer.obj");

            for (int i = 0; i < this.hidden.size() - 1 ; i++) {
                for (int j = 0; j < this.hidden.get(i).size(); j++) {
                    for (int k = 0; k < this.hidden.get(i+1).size(); k++) {
                        Neuron h = this.hidden.get(i).get(j);
                        Neuron hto = this.hidden.get(i+1).get(k);
                        h.forbind(hto, hiddenLayer.get(i).get(j).forbundetTil.get(k).getWeight());
                    }
                }
            }

            fileOperation = new FileOperation();
            ArrayList<Neuron> lastHiddenLayer = (ArrayList<Neuron>) fileOperation.readFromFile("./LastHiddenLayer.obj");

            for (int i = 0; i < this.hidden.get(this.hidden.size() - 1).size(); i++) {
                for (int j = 0; j < this.output.size(); j++) {
                    this.hidden.get(this.hidden.size() - 1).get(i).forbind(this.output.get(j), lastHiddenLayer.get(i).forbundetTil.get(j).getWeight()); // forbind() creates a new input synapse to a neuron.
                }
            }
        }
    }

    /**
     * @param exp The expected value of the input
     */
    private void backpropagate(double[] exp){
        double[] error = new double[this.output.size()];  // error array represents the value = {output neuron value - required value}.
        //Hidden->Output
        int c = 0;
        for(Neuron o : this.output){
            error[c] = o.getSenesteOutput()*(1.0-o.getSenesteOutput())*(exp[this.outputIndex.get(o)]-o.getSenesteOutput());
            c++;
        }
        for(Neuron h : this.hidden.get(this.hidden.size()-1)){
            for(Synaps s : h.getForbundetTil()){
                double v = s.getWeight();
                s.setWeight(v+this.learningrate*h.getSenesteOutput()*error[this.outputIndex.get(s.getTil())]);
            }
        }

        double[] oerror = error.clone();
        error = new double[this.hidden.get(0).size()];
        //Hidden->Hidden
        for(int i = this.hidden.size()-1; i > 0; i--){
            c = 0;
            for(Neuron h : this.hidden.get(i)){
                double p = h.getSenesteOutput()*(1-h.getSenesteOutput());
                double k = 0;
                for(Synaps s : h.getForbundetTil()){
                    if(i == this.hidden.size()-1){
                        k = k+oerror[this.outputIndex.get(s.getTil())]*s.getWeight();
                    }
                    else{
                        k = k+error[this.hiddenIndex.get(i+1).get(s.getTil())]*s.getWeight();
                    }
                }
                error[c] = p*k;
                c++;
            }
            for(Neuron h : this.hidden.get(i-1)){
                for(Synaps s : h.getForbundetTil()){
                    double v = s.getWeight();
                    int index = this.hiddenIndex.get(i).get(s.getTil());
                    s.setWeight(v+this.learningrate*error[index]*h.getSenesteInput());
                }
            }
        }
        //Input->Hidden
        c = 0;
        double[] t = error.clone();
        for(Neuron h : this.hidden.get(0)){
            double p = h.getSenesteOutput()*(1.0-h.getSenesteOutput());
            double k = 0;
            for(Synaps s : h.getForbundetTil()){
                if(this.hidden.size() == 1){
                    k = k+s.getWeight()*oerror[this.outputIndex.get(s.getTil())];
                }
                else{
                    k = k+s.getWeight()*error[this.hiddenIndex.get(1).get(s.getTil())];
                }
            }
            t[c] = k*p;
            c++;
        }
        for(Neuron i : this.input){
            for(Synaps s : i.getForbundetTil()){
                double v = s.getWeight();
                s.setWeight(v+this.learningrate*t[this.hiddenIndex.get(0).get(s.getTil())]*i.getSenesteInput());
            }
        }
    }

    public static double activation(double x){
        return 1.0/(1+Math.pow(Math.E, -x));
    }

    /**
     *
     * @param input Input to be classified
     * @return The classification of the input
     */
    public double[] classify(double[] input) {
        for(int i = 0; i < input.length; i++){
            this.input.get(i).input(input[i]);
        }
        double[] r = new double[this.output.size()];
        for(int i = 0; i < r.length; i++){
            r[i] = this.output.get(i).getSenesteOutput();
        }
        return r;
    }

    public double[] map(double[] input){
        for(int i = 0; i < input.length; i++){
            this.input.get(i).input(input[i]);
        }
        double[] retur = new double[this.output.size()];
        for(int i = 0; i < retur.length; i++){
            retur[i] = this.output.get(i).getSenesteOutput();
        }
        return retur;
    }

    public void train(double[] input, double[] target){
        for(int i = 0; i < input.length; i++){
            this.input.get(i).input(input[i]);
        }
        backpropagate(target);
    }
}



