package games.sparking.crystalguard.utils;

import com.google.common.base.Joiner;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;

import java.util.TimeZone;


public class Statics {

    public static final TimeZone TIME_ZONE = TimeZone.getTimeZone("America/New_York");
    public static final JsonParser JSON_PARSER = new JsonParser();
    public static final Gson PLAIN_GSON = new GsonBuilder().create();
    public static Joiner SPACE_JOINER = Joiner.on(" ");
    public static Joiner COMMA_JOINER = Joiner.on(", ");

}
