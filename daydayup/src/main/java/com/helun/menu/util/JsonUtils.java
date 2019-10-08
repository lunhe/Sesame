package com.helun.menu.util;

import com.google.gson.Gson;

/**
 * 
 * @author Administrator
 *
 * @param <T>
 */


public class JsonUtils<T>
{
    private static Gson gson;

    public JsonUtils()
    {
        gson = new Gson();
    }

    public String toGson(Object obj)
    {
        return gson.toJson(obj);

    }

    public T toObject(String s, Class<T> clazz)
    {
        return gson.fromJson(s, clazz);
    }
    

}

