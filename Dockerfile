FROM eclipse-temurin:21-jdk

# Evita prompts interativos
ENV DEBIAN_FRONTEND=noninteractive

# Instala dependências essenciais do Chrome
RUN apt-get update && apt-get install -y \
    wget gnupg unzip \
    libx11-xcb1 libxcb1 libxcb-render0 libxcb-shm0 \
    libatk1.0-0 libcups2 libdrm2 libxkbcommon0 \
    libatspi2.0-0 libxcomposite1 libxrandr2 libgbm1 \
    libnss3 libxss1 libasound2 libxshmfence1 \
    && rm -rf /var/lib/apt/lists/*

# Instala o Google Chrome
RUN wget -O /tmp/chrome.deb https://dl.google.com/linux/direct/google-chrome-stable_current_amd64.deb \
    && apt-get install -y /tmp/chrome.deb \
    && rm /tmp/chrome.deb

# Chrome path
ENV CHROME_BIN=/usr/bin/google-chrome

# Diretório da aplicação
WORKDIR /app

# Copia o JAR
COPY target/status-checker.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]