package collection;

import java.io.Serializable;

/**
 * Enum class that contains the possible types of tickets
 */
public enum TicketType implements Serializable {
    VIP,
    USUAL,
    BUDGETARY,
    CHEAP
}
