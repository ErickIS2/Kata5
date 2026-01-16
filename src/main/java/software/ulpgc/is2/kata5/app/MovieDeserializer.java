package software.ulpgc.is2.kata5.app;

import software.ulpgc.is2.kata5.model.Movie;

public class MovieDeserializer {
    public static Movie fromTsv(String str){
        return fromTsv(str.split("\t"));
    }

    public static Movie fromTsv(String[] split){
        return new Movie(split[2], toInt(split[5]), toInt(split[7]));
    }

    private static int toInt(String s) {
        if(s.equals("\\N")) return -1;
        return Integer.parseInt(s);
    }
}

