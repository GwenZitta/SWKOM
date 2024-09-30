FROM node:20

RUN npm install npm@10.8.3

RUN npm install node@20

# Globale Installation von Angular CLI
RUN npm install @angular/cli

# Setze das Arbeitsverzeichnis
WORKDIR /angular-app

# Kopiere package.json und package-lock.json (falls vorhanden) vor den Code, um den Cache zu nutzen
COPY package*.json /angular-app/

# Installiere npm-Abhängigkeiten
RUN npm install

# Kopiere den Rest des Codes
COPY . /angular-app

# Baue das Projekt (optional, aber empfohlen)
RUN ng build

# Starte die Angular-App
CMD ["ng", "serve", "--host", "0.0.0.0", "--port", "4200"]

# Exponiere den Port 4200
EXPOSE 4200
