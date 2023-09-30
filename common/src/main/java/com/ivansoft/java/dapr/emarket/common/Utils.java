package com.ivansoft.java.dapr.emarket.common;

import com.ivansoft.java.dapr.emarket.common.models.Order;

import java.io.*;
import java.util.Base64;

public class Utils {
    public static Order deserializeOrder(String order) throws IOException, ClassNotFoundException {
        byte[] deserializedBytes = Base64.getDecoder().decode(order);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(deserializedBytes);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        Order deserializedObj = (Order) objectInputStream.readObject();
        objectInputStream.close();
        return deserializedObj;
    }

    public static String serializeOrder(Order order) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(order);
        objectOutputStream.close();

        byte[] serializedBytes = byteArrayOutputStream.toByteArray();

        // Encode the byte array into a string using Base64
        return Base64.getEncoder().encodeToString(serializedBytes);
    }
}
