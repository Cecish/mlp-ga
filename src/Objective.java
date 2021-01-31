/**
 * Created with IntelliJ IDEA.
 * User: Cécile RIQUART
 * Date: 10/25/15
 * Time: 3:41 PM
 * Here are all the activation functions used
 */
public class Objective {

    //Constructor ================================================
    /**
     * Default constructor
     */
    public Objective() {}

    //Methods ============================================================
    /**
     * Applying the activation function corresponding to the id in parameter
     * @param id designating a specific activation function
     * @param x input of the activatio function to apply
     * @return the result of activation_function(x)
     */
    public static double activationFunction(int id, double x) {
        double res = 0.0;

        switch (id) {
            case 0 : //Null
                res = funcNull(x);
                break;

            case 1 : //Sigmoid
                res = sigmoid(x);
                break;

            case 2 : //HyperbolicTangent
                res = tanh(x);
                break;

            case 3 : //Cosine
                res = cosine(x);
                break;

            case 4 : //Gaussian
                res = gaussian(x);
                break;

            default:
                System.err.println("No corresponding activation function");
                System.exit(0);
                break;

        }
        return res;
    }
    //Activation functions
    /**
     * Tanh function
     * @param x input
     * @return the result of tanh(x)
     */
    private static double tanh(double x) {
        return Math.tanh(x);
    }

    /**
     * Null function
     * @param x input
     * @return the result of null(x)
     */
    private static double funcNull(double x) {
        return 0.0;
    }

    /**
     * Sigmoid function
     * @param x input
     * @return the result of sigmoid(x)
     */
    private static double sigmoid(double x) {
        double denominator = 1 + Math.exp(-x);

        return (1/denominator);
    }

    /**
     * Cosine function
     * @param x input
     * @return the result of cos(x)
     */
    private static double cosine(double x) {
        return Math.cos(x);
    }

    /**
     * Gaussian function
     * @param x input
     * @return the result of gaussian(x)
     */
    private static double gaussian(double x) {
        double param = Math.pow(x,2)/2;

        return Math.exp(-param);
    }
}
