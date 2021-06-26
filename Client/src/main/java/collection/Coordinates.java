package collection;

import java.io.Serializable;

/**
 * Class that contains coordinates
 */
public class Coordinates implements Serializable {
    /**coordinate x*/
    private final Double x; //Значение поля должно быть больше -48, Поле не может быть null
    /**coordinate y*/
    private final Double y;

    /**
     * Constructor with parameters
     * @param x - coordinate x
     * @param y - coordinate y
     */
    public Coordinates(Double x, double y) {

        this.x = x;
        this.y = y;
    }

    /**
     * Getter {@link Coordinates#x}
     * @return x coordinate
     */
    public Double getX() {
        return x;
    }

    /**
     * Getter {@link Coordinates#y}
     * @return y coordinate
     */
    public Double getY() {
        return y;
    }


}
