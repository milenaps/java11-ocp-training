package com.mps.training;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;

public class SerializationSample {

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, ClassNotFoundException {
        Student student = new Student("Fernando");
        //what to do next?
    }

    static class Student implements Serializable {
        private String name;
        private transient String hash = generateHash();

        public Student(String name) {
            this.name = name;
        }

        private void writeObject(ObjectOutputStream out) throws IOException {
            out.defaultWriteObject();
            out.writeObject(Instant.now());
        }

        private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
            in.defaultReadObject();
            hash = generateHash();
        }

        private String generateHash() {
            String hash = null;
            try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                 ObjectOutputStream out = new ObjectOutputStream(byteArrayOutputStream)) {
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                out.writeObject(this);
                hash = new BigInteger(1, md.digest(byteArrayOutputStream.toByteArray())).toString(16);
            } catch (IOException | NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            System.out.println(hash);
            return hash;
        }
    }
}
