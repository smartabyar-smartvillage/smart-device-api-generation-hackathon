(cd ~/smartabyar-smartvillage \
  && env CONFIG_PATH=$HOME/smartabyar-smartvillage/config/smartabyar-smartvillage.yml \
  VERTXWEB_ENVIRONMENT=dev \
  mvn exec:java \
  -DfileResolverCachingEnabled=false \
  -Dvertx.disableFileCaching=true \
  -Dexec.mainClass="org.computate.smartvillage.enus.vertx.MainVerticle")

