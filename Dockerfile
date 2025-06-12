FROM openjdk:17-jdk-alpine

ENV  cloudflare_acessKey=14d3c15485077997f04571160917c5ec
ENV  cloudflare_bucket=forum-bucket
ENV  cloudflare_endpoint=https://514df536a756632b3483ce45bcca4d5e.r2.cloudflarestorage.com
ENV  cloudflare_publicUrl=https://pub-f076e8d873724a15945dd021de5d723a.r2.dev
ENV  cloudflare_secretKey=8654d2d73f57b0e131516078d4c34511c52af1d803602f187f6754d7754c9a5f
ENV  cloudflare_token=rj18AfHW7k-jMz-VE71w32yaQNmdmF0-6BFZJSgX
ENV  database_url=jdbc:postgresql://host.docker.internal:5432/Forum?user=postgres&password=PostgresPass@!13
ENV  google_client_id=595331401098-ivrdldf0i9mtas5tpf6gq0flph3kj1on.apps.googleusercontent.com
ENV  google_client_secret=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

WORKDIR /app

EXPOSE 8080

COPY target/forum-0.0.1-SNAPSHOT.jar forum-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java","-jar","forum-0.0.1-SNAPSHOT.jar"]