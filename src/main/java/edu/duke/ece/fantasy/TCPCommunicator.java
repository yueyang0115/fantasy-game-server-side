package edu.duke.ece.fantasy;

import java.net.*;
import java.io.*;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.duke.ece.fantasy.json.MessagesC2S;
import edu.duke.ece.fantasy.json.MessagesS2C;
import org.json.*;

public class TCPCommunicator {

    public Socket socket;
    private ObjectMapper objectMapper;
    private InputStream in;
    private OutputStream out;

    public TCPCommunicator(ServerSocket serverSocket) {
        this.objectMapper = new ObjectMapper();
        objectMapper.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET,false);
        objectMapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE,false);
        try {
            this.socket = serverSocket.accept();
            while(this.socket ==null){
                this.socket = serverSocket.accept();
            }
            this.in = socket.getInputStream();
            this.out = socket.getOutputStream();
            System.out.println("[DEBUG] TCP communicator successfully accept player socket!");
        } catch (IOException e) {
            System.out.println("[DEBUG] TCP communicator failed to accept player socket!");
            e.printStackTrace();
        }
    }

    public TCPCommunicator(String ip, int port) {
        this.objectMapper = new ObjectMapper();
        objectMapper.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET,false);
        objectMapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE,false);
        try {
            this.socket = new Socket(ip, port);
        } catch (IOException e) {
            System.out.println("[DEBUG] TCP communicator failed to crete TCPCommunicator!");
            e.printStackTrace();
        }
    }

    public void send(MessagesS2C msg) {
        try {
            objectMapper.writeValue(out, msg);
        } catch (IOException e) {
            System.out.println("[DEBUG] TCP communicator failed to send data!");
            e.printStackTrace();
        }
    }

    public MessagesC2S receive() {
        MessagesC2S res = new MessagesC2S();
        try {
            res = objectMapper.readValue(in, MessagesC2S.class);
        } catch (IOException e) {
            System.out.println("[DEBUG] TCP communicator failed to receive data!");
            e.printStackTrace();
        }
        return res;
    }

    public void close() {
        try {
            socket.close();
        } catch (IOException e) {
            System.out.println("[DEBUG] TCP communicator failed to close socket!");
            e.printStackTrace();
        }
    }
}