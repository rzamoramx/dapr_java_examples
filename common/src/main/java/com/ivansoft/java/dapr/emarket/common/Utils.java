package com.ivansoft.java.dapr.emarket.common;

import com.google.protobuf.ByteString;
import com.ivansoft.java.dapr.emarket.common.models.Order;

import java.io.*;
import java.util.Arrays;
import java.util.Base64;

public class Utils {
    public static Order deserializeOrder(String order) throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream byteIn = new ByteArrayInputStream(Base64.getDecoder().decode(order));
             ObjectInputStream objectIn = new ObjectInputStream(byteIn)) {

            // Deserialize (decode) the object from the byte array
            return (Order) objectIn.readObject();
        }
    }

    public static Order deserializeOrder(ByteString order) throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream byteIn = new ByteArrayInputStream(order.toByteArray());
             ObjectInputStream objectIn = new ObjectInputStream(byteIn)) {

            // Deserialize (decode) the object from the byte array
            return (Order) objectIn.readObject();
        }
    }

    public static String serializeOrder(Order order) throws IOException {
        try (ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
             ObjectOutputStream objectOut = new ObjectOutputStream(byteOut)) {

            objectOut.writeObject(order);

            // Get the byte array
            byte[] encodedBytes = byteOut.toByteArray();
            System.out.println("Object has been encoded to a byte array");

            return Base64.getEncoder().encodeToString(encodedBytes);
        }
    }

    public static byte[] serializeOrderByte(Order order) throws IOException {
        try (ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
             ObjectOutputStream objectOut = new ObjectOutputStream(byteOut)) {

            objectOut.writeObject(order);

            // Get the byte array
            byte[] encodedBytes = byteOut.toByteArray();
            System.out.println("Object has been encoded to a byte array: " + Arrays.toString(encodedBytes));

            return encodedBytes;
        }
    }
}
