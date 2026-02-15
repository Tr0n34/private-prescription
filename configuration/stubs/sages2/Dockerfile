FROM wiremock/wiremock:3.13.2-alpine

COPY mappings /home/wiremock/mappings
COPY __files /home/wiremock/__files

EXPOSE 8080

CMD ["--verbose", "--global-response-templating"]