package tm_simulator;

/**
 * Class representing the defined transitions for Turing Machine
 * 
 * @author MrMutart
 */
public class Transition {
    private String fromState;
    private char read;
    private char write;
    private char direction;
    private String toState;

    
    /**
     * Parameterized Constructor
     * 
     * @param fromState state transition from
     * @param read the read input value
     * @param write what the write in tape position
     * @param direction direction to move head
     * @param toState state transition to
     */
    public Transition(String fromState, char read, char write, char direction, String toState) {
        this.fromState = fromState;
        this.read = read;
        this.write = write;
        this.direction = direction;
        this.toState = toState;
    }

    
    /* Getters and Setters */
    
    public String getFromState() {
        return fromState;
    }

    public void setFromState(String fromState) {
        this.fromState = fromState;
    }

    public char getRead() {
        return read;
    }

    public void setRead(char read) {
        this.read = read;
    }

    public char getWrite() {
        return write;
    }

    public void setWrite(char write) {
        this.write = write;
    }

    public char getDirection() {
        return direction;
    }

    public void setDirection(char direction) {
        this.direction = direction;
    }

    public String getToState() {
        return toState;
    }

    public void setToState(String toState) {
        this.toState = toState;
    }
    
    
    /**
     * toString override method for debugging purposes
     * 
     * @return fromState->read->write->direction->toState
     */
    @Override
    public String toString() {
        return fromState + "->" + read + "->" + write + "->" + direction + "->" + toState;
    }

}
