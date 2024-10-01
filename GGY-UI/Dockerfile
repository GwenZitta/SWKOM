FROM node:20-alpine

WORKDIR /ggy-ui/

COPY public/ /ggy-ui/public
COPY src/ /ggy-ui/src
COPY package.json /ggy-ui/

RUN npm install

CMD ["npm", "start"]
