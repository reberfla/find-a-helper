# build stage
FROM node:22-alpine3.21 AS build
WORKDIR /app
COPY package*.json .
RUN npm install
COPY . .
RUN npm run build

# production stage
FROM nginx:1.28-alpine3.21 AS final
COPY --from=build /app/dist /usr/share/nginx/html
EXPOSE 8085
CMD ["nginx", "-g", "daemon off;"]
