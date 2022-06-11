package com.company;

import javax.naming.InvalidNameException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;
import java.net.*;

public class Main {

    public static void main(String[] args) {
//        System.out.print("Enter a URL: ");
        String text = args[0];
        System.out.println(text);
        try {
            URL url = new URL(text);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuilder stringBuilder = new StringBuilder();
            String inputLine;
            while((inputLine = bufferedReader.readLine()) != null){
                stringBuilder.append(inputLine);
                stringBuilder.append(System.lineSeparator());
            }
            bufferedReader.close();
            Map<String , Integer> map = new TreeMap<>();
            String[] words = stringBuilder.toString().trim().split("[\\s+\\p{P}]");
            for(int i = 0; i < words.length; i++){
                String key = words[i].toLowerCase();
                if(key.length() > 0) {
                    if(!map.containsKey(key)){
                        map.put(key, 1);
                    }
                    else{
                        int value = map.get(key);
                        value++;
                        map.put(key, value);
                    }
                }
            }
            System.out.println("Display words and their count in ascending order of the words");
            map.forEach((k, v) -> System.out.println(v + "\t" + k));
        }
        catch (java.net.MalformedURLException e) {
            System.out.println("Invalid URL");
        }
        catch (java.io.IOException ex) {
            System.out.println("IO Errors");
        }
    }
}