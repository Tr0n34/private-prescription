FROM wiremock/wiremock:3.13.2-alpine

COPY mappings /home/wiremock/mappings

EXPOSE 8080

CMD ["--verbose", "--global-response-templating"]