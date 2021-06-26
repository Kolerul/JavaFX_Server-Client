package mainPart;

import collection.Ticket;

import java.util.LinkedList;

public class CollectionWorker {

    private volatile LinkedList<Ticket> collection;

    CollectionWorker(LinkedList<Ticket> collection) {
        this.collection = collection;
    }

    public synchronized LinkedList<Ticket> getCollection() {
        return collection;
    }

    public synchronized void setCollection(LinkedList<Ticket> collection) {
        this.collection = collection;
    }
}
