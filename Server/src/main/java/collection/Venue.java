package collection;

import java.io.Serializable;
import java.util.Scanner;

/**
 * Class Venue with fields id, name, capacity and type
 */
public class Venue implements Serializable {
    /**Venue id*/
    private Integer id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    /**Venue name*/
    private final String name; //Поле не может быть null, Строка не может быть пустой
    /**Venue capacity*/
    private Integer capacity; //Поле может быть null, Значение поля должно быть больше 0
    /**Venue type*/
    private VenueType type; //Поле может быть null


    public Venue(String name) {
        this.name = name;
        //scanner.close();
    }

    /**
     * Constructor with parameters
     * @param name - venue name {@link Venue#name}
     * @param capacity - venue capacity {@link Venue#capacity}
     * @param type - venue type {@link Venue#type}
     * @see Venue#Venue(String)
     */
    public Venue(Integer id, String name, Integer capacity,VenueType type) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
        this.type = type;
    }

    /**
     * Getter {@link Venue#name}
     * @return venue name
     */
    public String getName() {
        return name;
    }


    /**
     * Getter {@link Venue#capacity}
     * @return venue capacity
     */
    public Integer getCapacity() {return capacity;}

    public void setCapacity(Integer capacity) {this.capacity = capacity;}

    /**
     * Getter {@link Venue#type}
     * @return venue type
     */
    public VenueType getType() {return type;}

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {this.id = id;}

    public void setType(VenueType type) {this.type = type;}

}
