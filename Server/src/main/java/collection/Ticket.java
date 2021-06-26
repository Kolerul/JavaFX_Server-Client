package collection;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Scanner;

/**
 * Class Ticket with fields generalId, id, name, coordinates, creationDate, price, type and venue
 */
public class Ticket implements Serializable {

    /**ticket id*/
    private int id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String owner;
    /**ticket name*/
    private String name; //Поле не может быть null, Строка не может быть пустой
    /**ticket coordinates*/
    private Coordinates coordinates; //Поле не может быть null
    /**ticket creation date*/
    private final Date creationDate = new Date(); //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    /**ticket price*/
    private Double price; //Поле не может быть null, Значение поля должно быть больше 0
    /**ticket type*/
    private TicketType type; //Поле может быть null
    /**ticket venue*/
    private Venue venue; //Поле может быть null

    public Ticket() {
    }

    public Ticket(Double price) {
        this.price = price;
    }

    /**
     * Constructor with parameters
     * @see Ticket#Ticket()
     * @see Ticket#Ticket(Double)
     * @param id - ticket id (generate automatically)
     * @param name - ticket name (!= null)
     * @param coordinates - ticket coordinates (x and y, more than -48)
     * @param price - ticket price
     * @param type - ticket type (VIP, USUAL, BUDGETARY, CHEAP)
     * @param venueName - venue name (!= null)
     * @param venueCapacity - venue capacity
     * @param venueType - venue type (BAR, LOFT, THEATRE, MALL, STADIUM)
     */

    public Ticket(int id, String owner, String name, Coordinates coordinates, Double price, TicketType type, Integer venueId, String venueName, Integer venueCapacity, VenueType venueType) {
        this.id = id;
        this.owner = owner;
        this.name = name;
        this.coordinates = coordinates;
        this.price = price;
        this.type = type;
        if (venueName != null) {
            venue = new Venue(venueId, venueName, venueCapacity, venueType);
        }
    }

    /**
     * Getter {@link Ticket#creationDate}
     * @return creation date
     */

    public Date getDateOfCreation() {
        return creationDate;
    }

    /**
     * Getter {@link Ticket#price}
     * @return ticket price
     */

    public Double getPrice() {
        return price;
    }
    public void setPrice(Double price) {this.price = price;}

    /**
     * Getter {@link Ticket#id}
     * @return ticket id
     */
    public int getId() {return id;}
    public void setId(int id) {this.id = id;}

    /**
     * Getter {@link Ticket#name}
     * @return ticket name
     */
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public String getOwner() {return owner;}
    public void setOwner(String owner) {this.owner = owner;}

    /**
     * Getter {@link Ticket#type}
     * @return ticket type
     */
    public TicketType getType() {return type;}
    public void setType(TicketType type) {this.type = type;}

    /**
     * Getter {@link Ticket#coordinates}
     * @return ticket coordinates ("x y")
     */
    public Coordinates getCoordinates() {return coordinates;}
    public void setCoordinates(Coordinates coordinates) {this.coordinates = coordinates;}

    /**
     * Getter {@link Ticket#venue}
     * @return ticket venue
     */
    public Venue getVenue() {return venue;}
    public void setVenue(Venue venue) {this.venue = venue;}

    /**
     *
     * @return String with ticket id, name, price and type
     */
    @Override
    public String toString() {
        return "Id: " + id + ", " + name + ", стоимость билета - " + price + ", владелец - " + owner;
    }

    public Double getX() {
        return coordinates.getX();
    }

    public Double getY() {
        return coordinates.getY();
    }

    public String getVenueName() {
        if (venue == null) return null;
        else return venue.getName();
    }

    public Integer getVenueId() {
        if (venue == null) return null;
        else return venue.getId();
    }

    public Integer getVenueCapacity() {
        if (venue == null) return null;
        else return venue.getCapacity();
    }

    public VenueType getVenueType() {
        if (venue == null) return null;
        else return venue.getType();
    }
    public boolean equals(Ticket obj) {

        return Objects.equals(getId(), obj.getId()) && Objects.equals(getName(), obj.getName()) && Objects.equals(getX(), obj.getX()) &&
                Objects.equals(getY(), obj.getY()) && Objects.equals(getPrice(), obj.getPrice()) && Objects.equals(getType(), obj.getType()) &&
                Objects.equals(getVenueId(), obj.getVenueId()) && Objects.equals(getVenueName(), obj.getVenueName()) && Objects.equals(getVenueCapacity(), obj.getVenueCapacity()) &&
                Objects.equals(getVenueType(), obj.getVenueType());

    }
}

