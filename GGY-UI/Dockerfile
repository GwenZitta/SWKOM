# Use a lightweight Node.js image
FROM node:20-alpine

# Set the working directory inside the container
WORKDIR /ggy-ui

# Copy only the package.json and package-lock.json files first
# This allows Docker to cache the npm install layer if dependencies haven't changed
COPY package.json package-lock.json ./

# Install dependencies
RUN npm install

# Copy the rest of the application files
COPY public/ ./public
COPY src/ ./src

# Build the application
RUN npm run build

# Expose the port and set the start command
EXPOSE 3000
CMD ["npm", "start"]
