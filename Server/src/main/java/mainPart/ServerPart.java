package mainPart;

import commands.*;
import collection.*;
import exceptions.ConnectionException;
import exceptions.IdNotFoundException;
import exceptions.IncorrectInputDataException;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.nio.channels.SocketChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class ServerPart {

    private final ServerMaker serverMaker;
    private final DataBaseWorker dataBaseWorker;
    private static boolean isClientConnected = true;
    public boolean isItWaiting = false;

    public ServerPart (ServerMaker serverMaker) {
        dataBaseWorker = new DataBaseWorker();
        this.serverMaker = serverMaker;
    }

    public Serializable waitForRead(String userName, Serializable classToRead) throws  ConnectionException {
        while(isClientConnected) {
            for (Serializable serializable : serverMaker.getResponses().keySet()) {
                if (serializable != null) {
                    try {
                        if (serverMaker.getResponses().get(serializable).equals(userName) && serializable.getClass() == classToRead.getClass()) {
                            serverMaker.removeResponse(serializable);
                            isItWaiting = false;
                            return serializable;
                        }
                    } catch (NullPointerException e) {e.printStackTrace();}
                }

            }
        }
        return null;
    }

    public void waitForWrite(Serializable request, String userName) throws ConnectionException {
        serverMaker.addRequest(userName, request);
        while(serverMaker.getRequests().contains(request)) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
        isItWaiting = false;
    }

    private void readCommand(Command command, String userName) {
        try {
            Serializable s = check(command, userName);
            serverMaker.addRequest(userName, s);

        } catch (ConnectionException e) {
            System.out.println(e.getMessage());
            isClientConnected = false;
        }
    }

    public void readCommands() {
        ExecutorService service = Executors.newFixedThreadPool(4);
        while (isClientConnected) {
            try {
                try {
                for (Serializable serializable : serverMaker.getResponses().keySet()) {
                    Command command = (Command) serializable;
                        service.execute(() -> {
                            if (command != null) {
                                String user = serverMaker.getResponses().get(serializable);
                                serverMaker.removeResponse(serializable);
                                if (user != null) {
                                    readCommand(command, user);
                                }
                            }
                        });
                }
                } catch (ClassCastException e) {
                    //System.out.println(serverMaker.getResponses());
                }
            } catch(ConnectionException e) {
                System.out.println(e.getMessage());
                isClientConnected = false;
            }
        }
        service.shutdown();

    }

    private Serializable check(Command command, String userName) {
        try {
            Serializable s = null;
            try {
            if (command != null) {
                ((CommandWithoutAdditionalArgument) command).updateCollection(dataBaseWorker.getTickets());
                Ticket newTicket = null;


                //New Fragment
                if (command.getClass() == GetAllElementsCommand.class) {
                    StringBuilder stringBuilder = new StringBuilder();
                    for (Ticket t : dataBaseWorker.getTickets()) {
                        stringBuilder.append(t.getId()).append("\n");
                        stringBuilder.append(t.getOwner()).append("\n");
                        stringBuilder.append(t.getCoordinates().getX()).append(" ").append(t.getCoordinates().getY()).append("\n");
                        stringBuilder.append(t.getPrice()).append("\n");
                        stringBuilder.append(t.getName()).append("\n");
                    }
                    s = stringBuilder.toString();
                }

                if (command.getClass() == GetElementByIdCommand.class) {
                    Ticket ticket = new Ticket();
                    for (Ticket t : dataBaseWorker.getTickets()) {
                        if (t.getId() == ((GetElementByIdCommand) command).id) {
                            ticket = t;
                        }
                    }
                    /*s = ticket.getOwner() + "\n" +
                            ticket.getName() + "\n" +
                            ticket.getCoordinates().getX() + "\n" +
                            ticket.getCoordinates().getY() + "\n" +
                            ticket.getPrice() + "\n" +
                            ticket.getType();
                    System.out.println(ticket.getType());
                    if (ticket.getVenue() != null) {
                        s = s + "\n" + ticket.getVenue().getName() + "\n" +
                                ticket.getVenue().getType() + "\n" +
                                ticket.getVenue().getCapacity() + "\n" +
                                ticket.getVenue().getId();
                    } else
                        s = s + "\nnull\nnull\nnull\nnull";*/
                    s = ticket;
                }
            try {
                if (command.getClass() == SendTicketDataCommand.class) {
                    //System.out.println("yes");
                    String t = ((SendTicketDataCommand) command).data;
                    String[] fields = t.split("\n");
                    Integer id = Integer.parseInt(fields[10]);
                    String owner = fields[0];
                    String name = fields[1];
                    Double x = Double.parseDouble(fields[2]);
                    Double y = Double.parseDouble(fields[3]);
                    Double price = Double.parseDouble(fields[4]);
                    TicketType type = null;
                    try {
                        type = TicketType.valueOf(fields[5]);
                    } catch (IllegalArgumentException e) {};
                    Integer venueId = null;
                    try {
                        venueId = Integer.parseInt(fields[6]);
                    } catch (NumberFormatException e) {};
                    Ticket ticket;
                    if (venueId == null)
                        ticket = new Ticket(id, owner, name, new Coordinates(x, y), price, type, null, null, null, null);
                    else {
                        String venueName = fields[7];
                        VenueType venueType = null;
                        try {
                            venueType = VenueType.valueOf(fields[8]);
                        } catch (IllegalArgumentException e) {
                        }
                        ;
                        Integer capacity = null;
                        try {
                            capacity = Integer.parseInt(fields[9]);
                        } catch (NumberFormatException e) {

                        }
                        ;
                        ticket = new Ticket(id, owner, name, new Coordinates(x, y), price, type, venueId, venueName, capacity, venueType);
                    }
                    try {
                        dataBaseWorker.addTicketToDB(ticket, userName);
                        s = "Success";
                    } catch (SQLException e) {
                        e.printStackTrace();
                        s = "Failure";
                    }
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                s = "Failure";
            }

                if (command.getClass() == AddCommand.class) {
                    newTicket = ((AddCommand) command).ticket;
                }
                if (command.getClass() != UpdateCommand.class && command.getClass() != GetAllElementsCommand.class && command.getClass() !=GetElementByIdCommand.class && command.getClass() != SendTicketDataCommand.class) s = command.execute(userName);
            }
                if (command != null && ((CommandWithoutAdditionalArgument) command).isCollectionChanged())
                    dataBaseWorker.removeTicketsFromDB();

                return s;
        } catch (IdNotFoundException e) {return e.getMessage();}
        } catch (SQLException throwable) {
            throwable.printStackTrace();
            return "Не удалось это сделать Ticket created";
        }
    }

    private void updateTicketFields(Ticket ticket, String userName) throws SQLException {
        String s;
        String response = "";
        ticket.setCoordinates(null);
        ticket.setType(null);
        if (ticket.getOwner() != null && !ticket.getOwner().equals(userName)) throw new SQLException();
        ticket.setOwner(userName);
        do {
            waitForWrite("Введите название билета: ", userName);
            s = (String) waitForRead(userName, "");
            ticket.setName(s);
        } while(s.equals(""));

        do {
            waitForWrite(response + "Введите координаты: (в формате x y)", userName);
            response = "";
            String p = (String) waitForRead(userName, "");
            String[] j = p.split(" ");
            String[] jj = p.split("\t");
            if (jj.length > j.length) j = jj;
            try {
                if (j.length != 2) response += "Введите корректное число аргументов \n";
                else if (Double.parseDouble(j[0]) > -48 && Double.parseDouble(j[1]) > -48) ticket.setCoordinates(new Coordinates(Double.parseDouble(j[0]), Double.parseDouble(j[1])));
                else response += "Введите корректные значения x и y (они должны быть больше -48) \n";
            } catch (NumberFormatException e) {response += "Введите корректные значения x и y (они должны быть больше -48) \n";}

        } while(ticket.getCoordinates() == null);

        if (ticket.getId() != 0 || ticket.getPrice() == null) {
            ticket.setPrice(-1.0);
            do {
                waitForWrite(response + "Введите стоимость билета: (она должна быть больше 0)", userName);
                response = "";
                String[] j = ((String) waitForRead(userName, "")).split(" ");
                try {
                    if (j.length == 1) ticket.setPrice(Double.parseDouble(j[0]));
                    else {
                        response += "Введите корректное число \n";
                    }
                } catch (NumberFormatException e) {
                    response += "Введите корректную стоимость \n";
                }
            } while (ticket.getPrice() <= 0);
        }
        do {
            waitForWrite(response + "Введите тип билета: (оставьте поле пустым, если хотите) \nСписок возможным типов: VIP, USUAL, BUDGETARY, CHEAP", userName);
            response = "";
            s = (String) waitForRead(userName, "");
            if (!s.equals("")) {
                try {
                    ticket.setType(TicketType.valueOf(s.toUpperCase()));
                } catch (IllegalArgumentException e) { response += "Введите корректное название типа \n";}
            }
        } while (ticket.getType() == null && !s.equals(""));

        waitForWrite("Куда билет? (если не хотите вводить, оставьте поле пустым, для продолжения напишите название места)", userName);
        s = (String) waitForRead(userName, "");

        if (!s.equals("")) {
            ticket.setVenue(new Venue(s));
            s = "";
            do {
                waitForWrite(response + "Введите вместимость аудитории: (оставтье пустым, если она неизвестна)", userName);
                response = "";
                try {
                    s = (String) waitForRead(userName, "");
                    if (s != null && !s.equals("") && Integer.parseInt(s) > 0) ticket.getVenue().setCapacity(Integer.parseInt(s));
                } catch (NumberFormatException | NullPointerException e) {response += "Введите корректное значение \n";}
            } while (ticket.getVenue().getCapacity() == null && s != null && !s.equals(""));

            do {
                waitForWrite(response + "Введите тип аудитории: (оставьте поле пустым, если хотите) \nСписок возможным типов:   BAR, LOFT, THEATRE, MALL, STADIUM", userName);
                response = "";
                s = (String) waitForRead(userName, "");
                if (!s.equals("")) {
                    try {
                        ticket.getVenue().setType(VenueType.valueOf(s.toUpperCase()));
                    } catch (IllegalArgumentException e) {response += "Введите корректное название типа \n";}
                }
            } while (ticket.getVenue().getType() == null && !s.equals(""));
        }
    }

    public DataBaseWorker getDataBaseWorker() {return dataBaseWorker;}
    public boolean isIsClientConnected() {return isClientConnected;}


}
