package com.stefanini.status_checker.Service;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class SeleniumService {

    public String getGeminiStatus() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new"); // usa o modo headless moderno
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        WebDriver driver = new ChromeDriver(options);

        try {
            driver.get("https://aistudio.google.com/status");

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(40));

            //espera a div com a classe "status-large" aparecer (pode ser operational, degraded, etc)
            WebElement statusDiv = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.cssSelector("div.status.status-large")
            ));

            // pega o último span dentro dela (o que contém o texto, tipo "All Systems Operational")
            WebElement textSpan = statusDiv.findElement(By.cssSelector("span:last-child"));
            String statusText = textSpan.getText().trim();

            //a classe pra saber o tipo (ex: operational, degraded, outage)
            String className = statusDiv.getAttribute("class");
            String statusType = "Desconhecido";

            if (className.contains("operational")) statusType = "✅ Operacional";
            else if (className.contains("degraded")) statusType = "⚠️ Degradado";
            else if (className.contains("outage")) statusType = "❌ Indisponível";
            else if (className.contains("maintenance")) statusType = "🛠️ Manutenção";

            return String.format(statusText);

        } catch (Exception e) {
            return "Erro ao buscar status: " + e.getMessage();
        } finally {
            driver.quit();
        }
    }
}

