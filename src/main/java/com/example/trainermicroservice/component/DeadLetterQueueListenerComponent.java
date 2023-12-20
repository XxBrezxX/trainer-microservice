package com.example.trainermicroservice.component;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Component
public class DeadLetterQueueListenerComponent {

    @JmsListener(destination = "ActiveMQ.DLQ")
    public void processMessageFromDlq(String message) {
        // Imprimir el contenido del mensaje
        System.out.println("Received message from DLQ: " + message);

        // Guardar el contenido del mensaje en un archivo
        File file = new File("dlq_messages.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            writer.append(message).append("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}