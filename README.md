# sbj-media
Application to convert mp4 to mp3 and download YT videos

# swagger
http://localhost:9090/swagger-ui/index.html#/


# docker-build
docker build --no-cache -f Dockerfile -t sbj-media:1.0.0 .

# docker-run
docker run -d --name sbj-media -p 9090:9090 sbj-media:1.0.0

# docker-rm
docker rm -f sbj-media
