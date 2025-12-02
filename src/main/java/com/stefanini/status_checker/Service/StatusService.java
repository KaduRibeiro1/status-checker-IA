package com.stefanini.status_checker.Service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;

@Service
public class StatusService {

    public String getStatus(String url) {
        try {
            try {
                Document doc = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64)")
                        .header("Accept", "*/*")
                        .ignoreContentType(true)
                        .timeout(10000)
                        .get();

                Elements elements = doc.select("h3.heading-3, h2, h4, span.status, div.status, p");

                for (Element el : elements) {
                    String text = el.text().toLowerCase();
                    if (text.contains("operational") ||
                            text.contains("outage") ||
                            text.contains("degraded") ||
                            text.contains("investigating") ||
                            text.contains("maintenance")) {
                        return el.text();
                    }
                }
            } catch (Exception ignored) {

            }


            try {
                HttpClient client = HttpClient.newHttpClient();

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .header("User-Agent", "Mozilla/5.0")
                        .header("Accept", "application/json")
                        .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    String body = response.body().toLowerCase();

                    if (body.contains("operational")) return "All Systems Operational";
                    if (body.contains("outage")) return "Outage detectado";
                    if (body.contains("degraded")) return "Serviço degradado";
                    if (body.contains("investigating")) return "Investigando instabilidade";
                    if (body.contains("maintenance")) return "Em manutenção";

                    return "JSON recebido, mas sem status reconhecido";
                }
            } catch (Exception ignored) { }

            return "Status não encontrado";
        } catch (Exception e) {
            e.printStackTrace();
            return "Erro ao conectar: " + e.getMessage();
        }
    }
}


