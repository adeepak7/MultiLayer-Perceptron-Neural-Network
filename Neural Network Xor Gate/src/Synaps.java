import java.io.Serializable;

/**
 * Class to connect neurons
 */
public class Synaps implements Serializable { //Synaps represents the target Neuron and weight of the edge coming towards it.
    public Neuron til;
    public double weight;

    public Synaps(Neuron til, double weight) {
        this.til = til;
        this.weight = weight;
    }

    public double getWeight() {
        return this.weight;
    }

    public void setWeight(double v) {
        this.weight = v;
    }

    public Neuron getTil() {
        return this.til;
    }

    public String toString() {
        return weight + "";
    }
}
