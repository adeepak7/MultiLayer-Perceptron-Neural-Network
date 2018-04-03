import java.io.Serializable;
import java.util.ArrayList;

public class Neuron implements Serializable {

    public boolean act;
    public int antalTriggered = 0, antallinkstil = 0;
    public double senesteinput = 0, senesteoutput = 0, sum; // senesteinput and senesteoutput represents the input and output of the neurons.
    public ArrayList<Synaps> forbundetTil; //List of outgoing synapses.
    public Neuron(boolean act){
        this.act = act;
        this.forbundetTil = new ArrayList<Synaps>();
    }

    public void forbind(Neuron e, double weight){
        Synaps n = new Synaps(e, weight);// creates a new input synapse to neuron e with wgt vaegt
        this.forbundetTil.add(n);//synapse added to list of neuron n
        e.incLinksTil();//increments the number of input edges (of target neuron).
    }

    public double getSenesteInput(){
        return this.senesteinput;
    }

    public double getSenesteOutput(){
        return this.senesteoutput;
    }

    public void input(double input){
        this.antalTriggered++;  //Count of triggered synapses
        this.sum = sum+input;
        if(this.antalTriggered >= this.antallinkstil){ // '>=' becomes true only for input layer, and '>=' becomes true('==') for other neurons. This means if all synapses are triggered then only move forward. (Sequential processing).
            this.senesteinput = sum;
            test();
        }
    }

    public void test(){
        for(Synaps n : this.forbundetTil){
            if(this.act){
                n.getTil().input(MultiLayerPerceptron.activation(this.sum)*n.getWeight());
            }
            else{
                n.getTil().input(this.sum*n.getWeight());
            }
        }
        if(this.act){
            this.senesteoutput = MultiLayerPerceptron.activation(this.sum);
        }
        else{
            this.senesteoutput = this.sum;
        }
        this.sum = 0.0;
        this.antalTriggered = 0;
    }

    public void incLinksTil(){
        this.antallinkstil++;
    }

    public ArrayList<Synaps> getForbundetTil(){
        return this.forbundetTil;
    }

    public String toString(){
        String retur = this.hashCode()+" med "+this.forbundetTil.size()+" forbindelser.";
        return retur;
    }
}
