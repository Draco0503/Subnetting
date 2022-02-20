package main.java.com.draco.main;

import main.java.com.draco.subnetting.Net;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner s = new Scanner(System.in);

        System.out.print("IP: ");
        String ip = s.nextLine();
        System.out.print("Mask: ");
        String mask = s.nextLine();

        Net n = new Net(ip, mask);
        System.out.println(n.toString(true));

        System.out.print("The number of subnets you want: ");
        int sbn = s.nextInt();
        s.nextLine();

        Net[] nets = n.subnets(sbn);
        for (int i = 0; i < nets.length; i++) {
            System.out.println("-- SUBNET " + (i+1) + " --");
            System.out.println(nets[i].toString(false));
        }

        s.close();
    }
}
