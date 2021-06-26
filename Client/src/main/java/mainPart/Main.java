package mainPart;


import applicationPart.ApplicationWindows;


/**
 * This is main
 */
public class Main {
    public static void main(String[] args) {
        ServerConnect serverConnect = new ServerConnect("127.0.0.1", 2425);
        ApplicationWindows applicationWindows = new ApplicationWindows(serverConnect);
        applicationWindows.launchApp();
    }
}
