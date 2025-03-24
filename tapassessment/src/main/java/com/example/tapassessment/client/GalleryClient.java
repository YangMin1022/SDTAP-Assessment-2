package com.example.tapassessment.client;

import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.fasterxml.jackson.databind.ObjectMapper;

public class GalleryClient extends JFrame {

    private JTextField artistField;
    private JButton fetchButton;
    private JTextArea resultArea;

    public GalleryClient() {
        setTitle("Photo Gallery Client");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        artistField = new JTextField(20);
        fetchButton = new JButton("Fetch Photos");
        topPanel.add(new JLabel("Artist Name:"));
        topPanel.add(artistField);
        topPanel.add(fetchButton);
        add(topPanel, BorderLayout.NORTH);

        resultArea = new JTextArea();
        resultArea.setEditable(false);
        add(new JScrollPane(resultArea), BorderLayout.CENTER);

        fetchButton.addActionListener(e -> fetchPhotos());
    }

    private void fetchPhotos() {
        String artist = artistField.getText();
        try {
            String urlStr = "http://localhost:8080/api/photos/artist?name=" + URLEncoder.encode(artist, "UTF-8");
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line).append("\n");
            }
            reader.close();
            // Use Jackson ObjectMapper to pretty print the JSON response
            ObjectMapper mapper = new ObjectMapper();
            Object json = mapper.readValue(response.toString(), Object.class);
            String prettyJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
            
            resultArea.setText(prettyJson);
        } 
        catch (java.io.UnsupportedEncodingException | java.net.MalformedURLException ex) {
                resultArea.setText("URL Error: " + ex.getMessage());
        } catch (java.io.IOException ex) {
                resultArea.setText("IO Error: " + ex.getMessage());
        }
        catch (Exception ex) {
            resultArea.setText("Error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GalleryClient client = new GalleryClient();
            client.setVisible(true);
        });
    }
}