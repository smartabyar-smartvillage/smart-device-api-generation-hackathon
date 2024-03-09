package org.computate.smartvillage.enus.vertx;

import java.net.URLDecoder;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.computate.search.tool.SearchTool;
import org.computate.smartvillage.enus.config.ConfigKeys;
import org.computate.smartvillage.enus.model.page.SitePage;
import org.computate.smartvillage.enus.model.user.SiteUserEnUSGenApiService;
{% for JAVA_CLASS in JAVA_CLASSES %}
import {{ JAVA_CLASS.classeNomCanonique_enUS_stored_string }}EnUSGenApiService;
{% endfor %}
import org.computate.smartvillage.enus.mqtt.MqttMessageReader;
import org.computate.smartvillage.enus.page.HomePage;
import org.computate.smartvillage.enus.page.dynamic.DynamicPage;
import org.computate.smartvillage.enus.request.SiteRequestEnUS;
import org.computate.vertx.handlebars.AuthHelpers;
import org.computate.vertx.handlebars.DateHelpers;
import org.computate.vertx.handlebars.SiteHelpers;
import org.computate.vertx.openapi.ComputateOAuth2AuthHandlerImpl;
import org.computate.vertx.openapi.OpenApi3Generator;
import org.computate.vertx.search.list.SearchList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Jackson2Helper;
import com.github.jknack.handlebars.helper.ConditionalHelpers;
import com.github.jknack.handlebars.helper.StringHelpers;
import com.github.jknack.handlebars.internal.lang3.BooleanUtils;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator;
import io.opentelemetry.context.propagation.ContextPropagators;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.CompositeFuture;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.MultiMap;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.WorkerExecutor;
import io.vertx.core.eventbus.EventBusOptions;
import io.vertx.core.http.Cookie;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.impl.VertxImpl;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.JksOptions;
import io.vertx.core.net.PemKeyCertOptions;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.core.spi.cluster.NodeInfo;
import io.vertx.ext.auth.authorization.AuthorizationProvider;
import io.vertx.ext.auth.oauth2.OAuth2Auth;
import io.vertx.ext.auth.oauth2.OAuth2FlowType;
import io.vertx.ext.auth.oauth2.OAuth2Options;
import io.vertx.ext.auth.oauth2.Oauth2Credentials;
import io.vertx.ext.auth.oauth2.authorization.KeycloakAuthorization;
import io.vertx.ext.auth.oauth2.providers.OpenIDConnectAuth;
import io.vertx.ext.bridge.PermittedOptions;
import io.vertx.ext.healthchecks.HealthCheckHandler;
import io.vertx.ext.healthchecks.Status;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.Session;
import io.vertx.ext.web.api.service.ServiceRequest;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.OAuth2AuthHandler;
import io.vertx.ext.web.handler.SessionHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.TemplateHandler;
import io.vertx.ext.web.handler.impl.OAuth2AuthHandlerImpl;
import io.vertx.ext.web.handler.sockjs.SockJSBridgeOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;
import io.vertx.ext.web.impl.Origin;
import io.vertx.ext.web.openapi.RouterBuilder;
import io.vertx.ext.web.sstore.LocalSessionStore;
import io.vertx.ext.web.templ.handlebars.HandlebarsTemplateEngine;
import io.vertx.kafka.client.producer.KafkaProducer;
import io.vertx.mqtt.MqttClient;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.pgclient.PgPool;
import io.vertx.spi.cluster.zookeeper.ZookeeperClusterManager;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.sqlclient.Tuple;
import io.vertx.tracing.opentelemetry.OpenTelemetryOptions;


/**
 * Description: A Java class to start the Vert.x application as a main method. 
 * Keyword: classSimpleNameVerticle
 **/
public class MainVerticle extends MainVerticleGen<AbstractVerticle> {
	private static final Logger LOG = LoggerFactory.getLogger(MainVerticle.class);

	private Integer siteInstances;
	private Integer workerPoolSize;
	private Integer jdbcMaxPoolSize; 
	private Integer jdbcMaxWaitQueueSize;

	/**
	 * A io.vertx.ext.jdbc.JDBCClient for connecting to the relational database PostgreSQL. 
	 **/
	private PgPool pgPool;

	private WebClient webClient;

	private Router router;

	private WorkerExecutor workerExecutor;
//
//	private OAuth2Auth oauth2AuthenticationProvider;

	private OAuth2Auth oauth2AuthenticationProvider;

	private AuthorizationProvider authorizationProvider;

	private HandlebarsTemplateEngine templateEngine;

	private Handlebars handlebars;

	private TemplateHandler templateHandler;

	private KafkaProducer<String, String> kafkaProducer;

	private MqttClient mqttClient;

	/**	
	 *	The main method for the Vert.x application that runs the Vert.x Runner class
	 **/
	public static void  main(String[] args) {
		Vertx vertx = Vertx.vertx();
		String configPath = System.getenv(ConfigKeys.CONFIG_PATH);
		configureConfig(vertx).onSuccess(config -> {
			try {
				Future<Void> originalFuture = Future.future(a -> a.complete());
				Future<Void> future = originalFuture;
				WebClient webClient = WebClient.create(vertx, new WebClientOptions().setVerifyHost(false).setTrustAll(true));
				Boolean runOpenApi3Generator = Optional.ofNullable(config.getBoolean(ConfigKeys.RUN_OPENAPI3_GENERATOR)).orElse(false);
				Boolean runSqlGenerator = Optional.ofNullable(config.getBoolean(ConfigKeys.RUN_SQL_GENERATOR)).orElse(false);
				Boolean runArticleGenerator = Optional.ofNullable(config.getBoolean(ConfigKeys.RUN_ARTICLE_GENERATOR)).orElse(false);
				Boolean runFiwareGenerator = Optional.ofNullable(config.getBoolean(ConfigKeys.RUN_FIWARE_GENERATOR)).orElse(false);
				Boolean runProjectGenerator = Optional.ofNullable(config.getBoolean(ConfigKeys.RUN_PROJECT_GENERATOR)).orElse(false);

				if(runOpenApi3Generator || runSqlGenerator || runArticleGenerator || runFiwareGenerator) {
					SiteRequestEnUS siteRequest = new SiteRequestEnUS();
					siteRequest.setConfig(config);
					siteRequest.setWebClient(webClient);
					siteRequest.initDeepSiteRequestEnUS();
					OpenApi3Generator api = new OpenApi3Generator();
					api.setVertx_(vertx);
					api.setWebClient(webClient);
					api.setConfig(config);
					api.initDeepOpenApi3Generator(siteRequest);
					if(runOpenApi3Generator)
						future = future.compose(a -> api.writeOpenApi());
					if(runSqlGenerator)
						future = future.compose(a -> api.writeSql());
					if(runArticleGenerator)
						future = future.compose(a -> api.writeArticle());
					if(runFiwareGenerator)
						future = future.compose(a -> api.writeFiware());
					if(runProjectGenerator)
						future = future.compose(a -> api.writeProject());
					future.compose(a -> vertx.close());
				} else {
					future = future.compose(a -> run(config).onSuccess(b -> {
						LOG.info("MainVerticle run completed");
					}).onFailure(ex -> {
						LOG.info("MainVerticle run failed");
						vertx.close();
					}));
//					future.compose(a -> vertx.close());
				}
			} catch(Exception ex) {
				LOG.error(String.format("Error loading config: %s", configPath), ex);
				vertx.close();
			}
		}).onFailure(ex -> {
			LOG.error(String.format("Error loading config: %s", configPath), ex);
			vertx.close();
		});
	}

	public static void  runOpenApi3Generator(String[] args, Vertx vertx, JsonObject config) {
		OpenApi3Generator api = new OpenApi3Generator();
		WebClient webClient = WebClient.create(vertx, new WebClientOptions().setVerifyHost(false).setTrustAll(true));
		SiteRequestEnUS siteRequest = new SiteRequestEnUS();
		siteRequest.setConfig(config);
		siteRequest.setWebClient(webClient);
		api.setWebClient(webClient);
		api.setConfig(config);
		siteRequest.initDeepSiteRequestEnUS();
		api.initDeepOpenApi3Generator(siteRequest);
		api.writeOpenApi().onSuccess(a -> {
			LOG.info("Write OpenAPI completed. ");
			vertx.close();
		}).onFailure(ex -> {
			LOG.error("Write OpenAPI failed. ", ex);
			vertx.close();
		});
	}

	public static Future<Void> runVerticles(Vertx vertx, JsonObject config) {
		Promise<Void> promise = Promise.promise();
		try {
			Long vertxWarningExceptionSeconds = config.getLong(ConfigKeys.VERTX_WARNING_EXCEPTION_SECONDS);
			Long vertxMaxEventLoopExecuteTime = config.getLong(ConfigKeys.VERTX_MAX_EVENT_LOOP_EXECUTE_TIME);
			Long vertxMaxWorkerExecuteTime = config.getLong(ConfigKeys.VERTX_MAX_WORKER_EXECUTE_TIME);
			Integer siteInstances = config.getInteger(ConfigKeys.SITE_INSTANCES);

			DeploymentOptions deploymentOptions = new DeploymentOptions();
			deploymentOptions.setInstances(siteInstances);
			deploymentOptions.setConfig(config);
			deploymentOptions.setMaxWorkerExecuteTime(vertxMaxWorkerExecuteTime);
			deploymentOptions.setMaxWorkerExecuteTimeUnit(TimeUnit.SECONDS);

			DeploymentOptions emailVerticleDeploymentOptions = new DeploymentOptions();
			emailVerticleDeploymentOptions.setConfig(config);
			emailVerticleDeploymentOptions.setWorker(true);
			emailVerticleDeploymentOptions.setMaxWorkerExecuteTime(vertxMaxWorkerExecuteTime);
			emailVerticleDeploymentOptions.setMaxWorkerExecuteTimeUnit(TimeUnit.SECONDS);

			DeploymentOptions WorkerVerticleDeploymentOptions = new DeploymentOptions();
			WorkerVerticleDeploymentOptions.setConfig(config);
			WorkerVerticleDeploymentOptions.setInstances(1);
			WorkerVerticleDeploymentOptions.setMaxWorkerExecuteTime(vertxMaxWorkerExecuteTime);
			WorkerVerticleDeploymentOptions.setMaxWorkerExecuteTimeUnit(TimeUnit.SECONDS);

			vertx.deployVerticle(MainVerticle.class, deploymentOptions).onSuccess(a -> {
				LOG.info("Started main verticle. ");
				if(config.getBoolean(ConfigKeys.ENABLE_IMPORT_DATA)) {
					vertx.deployVerticle(WorkerVerticle.class, WorkerVerticleDeploymentOptions).onSuccess(b -> {
						LOG.info("Started worker verticle. ");
					}).onFailure(ex -> {
						LOG.error("Failed to start worker verticle. ", ex);
						vertx.close();
					});
				}
			}).onFailure(ex -> {
				LOG.error("Failed to start main verticle. ", ex);
				vertx.close();
			});
		} catch (Throwable ex) {
			vertx.close();
			LOG.error("Creating clustered Vertx failed. ", ex);
			ExceptionUtils.rethrow(ex);
		}
		return promise.future();
	}

	public static Future<Void> run(JsonObject config) {
		Promise<Void> promise = Promise.promise();
		try {
			Boolean enableZookeeperCluster = Optional.ofNullable(config.getBoolean(ConfigKeys.ENABLE_ZOOKEEPER_CLUSTER)).orElse(false);
			VertxOptions vertxOptions = new VertxOptions();
			EventBusOptions eventBusOptions = new EventBusOptions();
	
			if(enableZookeeperCluster) {
				JsonObject zkConfig = new JsonObject();
				String hostname = config.getString(ConfigKeys.HOSTNAME);
				String openshiftService = config.getString(ConfigKeys.OPENSHIFT_SERVICE);
				String zookeeperHostName = config.getString(ConfigKeys.ZOOKEEPER_HOST_NAME);
				Integer zookeeperPort = config.getInteger(ConfigKeys.ZOOKEEPER_PORT);
				String zookeeperHosts = Optional.ofNullable(config.getString(ConfigKeys.ZOOKEEPER_HOSTS)).orElse(zookeeperHostName + ":" + zookeeperPort);
				String clusterHostName = config.getString(ConfigKeys.CLUSTER_HOST_NAME);
				Integer clusterPort = config.getInteger(ConfigKeys.CLUSTER_PORT);
				String clusterPublicHostName = config.getString(ConfigKeys.CLUSTER_PUBLIC_HOST_NAME);
				Integer clusterPublicPort = config.getInteger(ConfigKeys.CLUSTER_PUBLIC_PORT);
				String zookeeperRetryPolicy = config.getString(ConfigKeys.ZOOKEEPER_RETRY_POLICY);
				Integer zookeeperBaseSleepTimeMillis = config.getInteger(ConfigKeys.ZOOKEEPER_BASE_SLEEP_TIME_MILLIS);
				Integer zookeeperMaxSleepMillis = config.getInteger(ConfigKeys.ZOOKEEPER_MAX_SLEEP_MILLIS);
				Integer zookeeperMaxRetries = config.getInteger(ConfigKeys.ZOOKEEPER_MAX_RETRIES);
				Integer zookeeperConnectionTimeoutMillis = config.getInteger(ConfigKeys.ZOOKEEPER_CONNECTION_TIMEOUT_MILLIS);
				Integer zookeeperSessionTimeoutMillis = config.getInteger(ConfigKeys.ZOOKEEPER_SESSION_TIMEOUT_MILLIS);
				zkConfig.put("zookeeperHosts", zookeeperHosts);
				zkConfig.put("sessionTimeout", zookeeperSessionTimeoutMillis);
				zkConfig.put("connectTimeout", zookeeperConnectionTimeoutMillis);
				zkConfig.put("rootPath", SITE_NAME);
				zkConfig.put("retry", new JsonObject()
						.put("policy", zookeeperRetryPolicy)
						.put("initialSleepTime", zookeeperBaseSleepTimeMillis)
						.put("intervalTimes", zookeeperMaxSleepMillis)
						.put("maxTimes", zookeeperMaxRetries)
				);
				ClusterManager clusterManager = new ZookeeperClusterManager(zkConfig);
	
				if(clusterHostName == null) {
					clusterHostName = hostname;
				}
				if(clusterPublicHostName == null) {
					if(hostname != null && openshiftService != null) {
						clusterPublicHostName = hostname + "." + openshiftService;
					}
				}
				if(clusterHostName != null) {
					LOG.info(String.format("%s: %s", ConfigKeys.CLUSTER_HOST_NAME, clusterHostName));
					eventBusOptions.setHost(clusterHostName);
				}
				if(clusterPort != null) {
					LOG.info(String.format("%s: %s", ConfigKeys.CLUSTER_PORT, clusterPort));
					eventBusOptions.setPort(clusterPort);
				}
				if(clusterPublicHostName != null) {
					LOG.info(String.format("%s: %s", ConfigKeys.CLUSTER_PUBLIC_HOST_NAME, clusterPublicHostName));
					eventBusOptions.setClusterPublicHost(clusterPublicHostName);
				}
				if(clusterPublicPort != null) {
					LOG.info(String.format("%s: %s", ConfigKeys.CLUSTER_PUBLIC_PORT, clusterPublicPort));
					eventBusOptions.setClusterPublicPort(clusterPublicPort);
				}
				vertxOptions.setClusterManager(clusterManager);
			}
			Long vertxWarningExceptionSeconds = config.getLong(ConfigKeys.VERTX_WARNING_EXCEPTION_SECONDS);
			Long vertxMaxEventLoopExecuteTime = config.getLong(ConfigKeys.VERTX_MAX_EVENT_LOOP_EXECUTE_TIME);
			Long vertxMaxWorkerExecuteTime = config.getLong(ConfigKeys.VERTX_MAX_WORKER_EXECUTE_TIME);
			Integer siteInstances = config.getInteger(ConfigKeys.SITE_INSTANCES);
			vertxOptions.setEventBusOptions(eventBusOptions);
			vertxOptions.setWarningExceptionTime(vertxWarningExceptionSeconds);
			vertxOptions.setWarningExceptionTimeUnit(TimeUnit.SECONDS);
			vertxOptions.setMaxEventLoopExecuteTime(vertxMaxEventLoopExecuteTime);
			vertxOptions.setMaxEventLoopExecuteTimeUnit(TimeUnit.SECONDS);
			vertxOptions.setMaxWorkerExecuteTime(vertxMaxWorkerExecuteTime);
			vertxOptions.setMaxWorkerExecuteTimeUnit(TimeUnit.SECONDS);
			vertxOptions.setWorkerPoolSize(config.getInteger(ConfigKeys.WORKER_POOL_SIZE));

			if(config.getBoolean(ConfigKeys.OPEN_TELEMETRY_ENABLED)) {
				SdkTracerProvider sdkTracerProvider = SdkTracerProvider.builder().build();
				OpenTelemetry openTelemetry = OpenTelemetrySdk.builder()
						.setTracerProvider(sdkTracerProvider)
						.setPropagators(ContextPropagators.create(W3CTraceContextPropagator.getInstance()))
						.buildAndRegisterGlobal();
				vertxOptions.setTracingOptions(new OpenTelemetryOptions(openTelemetry));
			}

			if(enableZookeeperCluster) {
				Vertx.clusteredVertx(vertxOptions).onSuccess(vertx -> {
					runVerticles(vertx, config).onSuccess(a -> {
						promise.complete();
					});
				}).onFailure(ex -> {
					LOG.error("Creating clustered Vertx failed. ", ex);
					promise.fail(ex);
				});
			} else {
				Vertx vertx = Vertx.vertx(vertxOptions);
				runVerticles(vertx, config).onSuccess(a -> {
					promise.complete();
				}).onFailure(ex -> {
					LOG.error("Creating clustered Vertx failed. ", ex);
					promise.fail(ex);
				});
			}
		} catch (Throwable ex) {
			LOG.error("Creating clustered Vertx failed. ", ex);
			promise.fail(ex);
		}
		return promise.future();
	}

	/**
	 * This is called by Vert.x when the verticle instance is deployed. 
	 * Initialize a new site context object for storing information about the entire site in English. 
	 * Setup the startPromise to handle the configuration steps and starting the server. 
	 **/
	@Override()
	public void  start(Promise<Void> startPromise) throws Exception, Exception {
		try {
			configureWebClient().onSuccess(a ->
				configureDataLoop().onSuccess(b -> 
					configureOpenApi().onSuccess(d -> 
						configureHealthChecks().onSuccess(e -> 
							configureSharedWorkerExecutor().onSuccess(f -> 
								configureWebsockets().onSuccess(g -> 
									configureHandlebars().onSuccess(j -> 
										configureKafka().onSuccess(k -> 
											configureApi().onSuccess(m -> 
												configureUi().onSuccess(n -> 
													startServer().onSuccess(o -> startPromise.complete())
												).onFailure(ex -> startPromise.fail(ex))
											).onFailure(ex -> startPromise.fail(ex))
										).onFailure(ex -> startPromise.fail(ex))
									).onFailure(ex -> startPromise.fail(ex))
								).onFailure(ex -> startPromise.fail(ex))
							).onFailure(ex -> startPromise.fail(ex))
						).onFailure(ex -> startPromise.fail(ex))
					).onFailure(ex -> startPromise.fail(ex))
				).onFailure(ex -> startPromise.fail(ex))
			).onFailure(ex -> startPromise.fail(ex));
		} catch (Exception ex) {
			LOG.error("Couldn't start verticle. ", ex);
			startPromise.fail(ex);
		}
	}

	/**	
	 **/
	public Future<Void> configureWebClient() {
		Promise<Void> promise = Promise.promise();

		try {
			Boolean sslVerify = config().getBoolean(ConfigKeys.SSL_VERIFY);
			webClient = WebClient.create(vertx, new WebClientOptions().setVerifyHost(sslVerify).setTrustAll(!sslVerify));
			promise.complete();
		} catch(Exception ex) {
			LOG.error("Unable to configure site context. ", ex);
			promise.fail(ex);
		}

		return promise.future();
	}

	/**
	 * Val.Success.enUS:The Kafka producer was initialized successfully. 
	 **/
	public Future<KafkaProducer<String, String>> configureKafka() {
		Promise<KafkaProducer<String, String>> promise = Promise.promise();

		try {
			if(config().getBoolean(ConfigKeys.ENABLE_KAFKA, true)) {
				Map<String, String> kafkaConfig = new HashMap<>();
				kafkaConfig.put("bootstrap.servers", config().getString(ConfigKeys.KAFKA_BROKERS));
				kafkaConfig.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
				kafkaConfig.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
				kafkaConfig.put("acks", "1");
				kafkaConfig.put("security.protocol", "SSL");
				Optional.ofNullable(config().getString(ConfigKeys.KAFKA_SSL_KEYSTORE_TYPE)).ifPresent(keystoreType -> {
					kafkaConfig.put("ssl.keystore.type", keystoreType);
					kafkaConfig.put("ssl.keystore.location", config().getString(ConfigKeys.KAFKA_SSL_KEYSTORE_LOCATION));
					kafkaConfig.put("ssl.keystore.password", config().getString(ConfigKeys.KAFKA_SSL_KEYSTORE_PASSWORD));
				});
				Optional.ofNullable(config().getString(ConfigKeys.KAFKA_SSL_KEYSTORE_TYPE)).ifPresent(truststoreType -> {
					kafkaConfig.put("ssl.truststore.type", truststoreType);
					kafkaConfig.put("ssl.truststore.location", config().getString(ConfigKeys.KAFKA_SSL_TRUSTSTORE_LOCATION));
					kafkaConfig.put("ssl.truststore.password", config().getString(ConfigKeys.KAFKA_SSL_TRUSTSTORE_PASSWORD));
				});
	
				kafkaProducer = KafkaProducer.createShared(vertx, SITE_NAME, kafkaConfig);
				LOG.info(configureKafkaSuccess);
				promise.complete(kafkaProducer);
			} else {
				LOG.info(configureKafkaSuccess);
				promise.complete(null);
			}
		} catch(Exception ex) {
			LOG.error("Unable to configure site context. ", ex);
			promise.fail(ex);
		}

		return promise.future();
	}

	/**
	 * Val.Success.enUS:The MQTT client was initialized successfully. 
	 * Val.Fail.enUS:The MQTT client failed to initialize. 
	 **/
	public Future<MqttClient> configureMqtt() {
		Promise<MqttClient> promise = Promise.promise();

		try {
			if(BooleanUtils.isTrue(config().getBoolean(ConfigKeys.MQTT_ENABLED))) {
				try {
					mqttClient = MqttClient.create(vertx);
					mqttClient.connect(config().getInteger(ConfigKeys.MQTT_PORT), config().getString(ConfigKeys.MQTT_HOST)).onSuccess(a -> {
						try {
							new MqttMessageReader(mqttClient, config());
							LOG.info(configureMqttSuccess);
							promise.complete(mqttClient);
						} catch(Exception ex) {
							LOG.error(configureMqttFail, ex);
							promise.fail(ex);
						}
					}).onFailure(ex -> {
						LOG.error(configureMqttFail, ex);
						promise.fail(ex);
					});
				} catch(Exception ex) {
					LOG.error(configureMqttFail, ex);
					promise.fail(ex);
				}
			} else {
				promise.complete();
			}
		} catch(Exception ex) {
			LOG.error(configureMqttFail, ex);
			promise.fail(ex);
		}

		return promise.future();
	}

	public Future<Void> configureDataLoop() {
		Promise<Void> promise = Promise.promise();
		configureData().onSuccess(a -> {
			promise.complete();
		}).onFailure(ex -> {
			LOG.info("Call timer");
			vertx.setTimer(10000, a -> {
			LOG.info("Timer triggered");
				configureDataLoop();
			});
		});
		return promise.future();
	}

	/**	
	 * 
	 * Val.ConnectionError.enUS:Could not open the database client connection. 
	 * Val.ConnectionSuccess.enUS:The database client connection was successful. 
	 * 
	 * Val.InitError.enUS:Could not initialize the database. 
	 * Val.InitSuccess.enUS:The database was initialized successfully. 
	 * 
	 *	Configure shared database connections across the cluster for massive scaling of the application. 
	 *	Return a promise that configures a shared database client connection. 
	 *	Load the database configuration into a shared io.vertx.ext.jdbc.JDBCClient for a scalable, clustered datasource connection pool. 
	 **/
	public Future<Void> configureData() {
		Promise<Void> promise = Promise.promise();
		try {
			PgConnectOptions pgOptions = new PgConnectOptions();
			pgOptions.setPort(config().getInteger(ConfigKeys.JDBC_PORT));
			pgOptions.setHost(config().getString(ConfigKeys.JDBC_HOST));
			pgOptions.setDatabase(config().getString(ConfigKeys.JDBC_DATABASE));
			pgOptions.setUser(config().getString(ConfigKeys.JDBC_USERNAME));
			pgOptions.setPassword(config().getString(ConfigKeys.JDBC_PASSWORD));
			pgOptions.setIdleTimeout(config().getInteger(ConfigKeys.JDBC_MAX_IDLE_TIME, 10));
			pgOptions.setIdleTimeoutUnit(TimeUnit.SECONDS);
			pgOptions.setConnectTimeout(config().getInteger(ConfigKeys.JDBC_CONNECT_TIMEOUT, 1000));

			PoolOptions poolOptions = new PoolOptions();
			jdbcMaxPoolSize = config().getInteger(ConfigKeys.JDBC_MAX_POOL_SIZE, 1);
			jdbcMaxWaitQueueSize = config().getInteger(ConfigKeys.JDBC_MAX_WAIT_QUEUE_SIZE, 10);
			poolOptions.setMaxSize(jdbcMaxPoolSize);
			poolOptions.setMaxWaitQueueSize(jdbcMaxWaitQueueSize);

			pgPool = PgPool.pool(vertx, pgOptions, poolOptions);
			Promise<Void> promise1 = Promise.promise();
			pgPool.withConnection(sqlConnection -> {
				sqlConnection.preparedQuery("SELECT")
						.execute(Tuple.tuple()
						).onSuccess(result -> {
					promise1.complete();
				}).onFailure(ex -> {
					LOG.error(configureDataInitError, ex);
					promise1.fail(ex);
				});
				return promise1.future();
			}).onSuccess(a -> {
				LOG.info(configureDataInitSuccess);
				promise.complete();
			}).onFailure(ex -> {
				LOG.error(configureDataInitError, ex);
				promise.fail(ex);
			});
		} catch (Exception ex) {
			LOG.error(configureDataInitError, ex);
			promise.fail(ex);
		}

		return promise.future();
	}

	private void mountCallback(OAuth2Auth authProvider, Router router, String callbackUri, String callbackUrl) {

		////////////////////////////////////////////////////////////////////////////////////////////////////////
		// MODIFIED BY computate FROM THE ORIGINAL qdox SOURCE CODE IN vertx-web to remove: .order(order - 1) //
		////////////////////////////////////////////////////////////////////////////////////////////////////////

		Origin callbackURL = Origin.parse(callbackUrl);
		Route callback = router.get(callbackUri);

		////////////////////////////////////////////////////////////////////////////////////////////////////////
		// MODIFIED BY computate FROM THE ORIGINAL qdox SOURCE CODE IN vertx-web to remove: .order(order - 1) //
		////////////////////////////////////////////////////////////////////////////////////////////////////////

		callback.handler(ctx -> {
			// Some IdP's (e.g.: AWS Cognito) returns errors as query arguments
			String error = ctx.request().getParam("error");

			if (error != null) {
				int errorCode;
				// standard error's from the Oauth2 RFC
				switch (error) {
				case "invalid_token":
					errorCode = 401;
					break;
				case "insufficient_scope":
					errorCode = 403;
					break;
				case "invalid_request":
				default:
					errorCode = 400;
					break;
				}

				String errorDescription = ctx.request().getParam("error_description");
				if (errorDescription != null) {
					ctx.fail(errorCode, new IllegalStateException(error + ": " + errorDescription));
				} else {
					ctx.fail(errorCode, new IllegalStateException(error));
				}
				return;
			}

			// Handle the callback of the flow
			final String code = ctx.request().getParam("code");

			// code is a require value
			if (code == null) {
				ctx.fail(400, new IllegalStateException("Missing code parameter"));
				return;
			}

			final Oauth2Credentials credentials = new Oauth2Credentials().setFlow(OAuth2FlowType.AUTH_CODE)
					.setCode(code);

			// the state that was passed to the IdP server. The state can be
			// an opaque random string (to protect against replay attacks)
			// or if there was no session available the target resource to
			// server after validation
			final String state = ctx.request().getParam("state");

			// state is a required field
			if (state == null) {
				ctx.fail(400, new IllegalStateException("Missing IdP state parameter to the callback endpoint"));
				return;
			}

			final String resource;
			final Session session = ctx.session();

			if (session != null) {
				// validate the state. Here we are a bit lenient, if there is no session
				// we always assume valid, however if there is session it must match
				String ctxState = session.remove("state");
				// if there's a state in the context they must match
				if (!state.equals(ctxState)) {
					// forbidden, the state is not valid (this is a replay attack)
					ctx.fail(401, new IllegalStateException("Invalid oauth2 state"));
					return;
				}

				// remove the code verifier, from the session as it will be trade for the
				// token during the final leg of the oauth2 handshake
				String codeVerifier = session.remove("pkce");
				credentials.setCodeVerifier(codeVerifier);
				// state is valid, extract the redirectUri from the session
				resource = session.get("redirect_uri");
			} else {
				resource = state;
			}

			// The valid callback URL set in your IdP application settings.
			// This must exactly match the redirect_uri passed to the authorization URL in
			// the previous step.
			credentials.setRedirectUri(callbackURL.href());

			authProvider.authenticate(credentials, res -> {
				if (res.failed()) {
					LOG.error(res.cause().getMessage(), res.cause());
					ctx.fail(res.cause());
				} else {
					ctx.setUser(res.result());
					String location = resource != null ? resource : "/";
					if (session != null) {
						// the user has upgraded from unauthenticated to authenticated
						// session should be upgraded as recommended by owasp
						session.regenerateId();
					} else {
						// there is no session object so we cannot keep state.
						// if there is no session and the resource is relative
						// we will reroute to "location"
						if (location.length() != 0 && location.charAt(0) == '/') {
							ctx.reroute(location);
							return;
						}
					}

					// we should redirect the UA so this link becomes invalid
					ctx.response()
							// disable all caching
							.putHeader(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate")
							.putHeader("Pragma", "no-cache").putHeader(HttpHeaders.EXPIRES, "0")
							// redirect (when there is no state, redirect to home
							.putHeader(HttpHeaders.LOCATION, location).setStatusCode(302)
							.end("Redirecting to " + location + ".");
				}
			});
		});
	}

	/**	
	 * 
	 * Val.Error.enUS:Could not configure the auth server and API. 
	 * Val.Success.enUS:The auth client %s was configured successfully. 
	 * Val.AuthCallbackUriEmpty.enUS:Please configure an AUTH_CALLBACK_URI for the AUTH_CLIENT %s. 
	 * Val.AuthClientOpenApiIdEmpty.enUS:Please configure an AUTH_OPEN_API_ID for the AUTH_CLIENT %s. 
	 * 
	 *	Configure the connection to the auth server and setup the routes based on the OpenAPI definition. 
	 *	Setup a callback route when returning from the auth server after successful authentication. 
	 *	Setup a logout route for logging out completely of the application. 
	 *	Return a promise that configures the authentication server and OpenAPI. 
	 **/
	public Future<OAuth2AuthHandler> configureAuthClient(String authClientOpenApiId, JsonObject clientConfig, Map<String, OAuth2AuthHandler> authHandlers, Map<String, OAuth2Auth> authProviders) {
		Promise<OAuth2AuthHandler> promise = Promise.promise();
		try {
			String siteBaseUrl = config().getString(ConfigKeys.SITE_BASE_URL);

			OAuth2Options oauth2ClientOptions = new OAuth2Options();
			Boolean authSsl = clientConfig.getBoolean(ConfigKeys.AUTH_SSL);
			String authHostName = clientConfig.getString(ConfigKeys.AUTH_HOST_NAME);
			Integer authPort = clientConfig.getInteger(ConfigKeys.AUTH_PORT);
			String authUrl = String.format("%s", clientConfig.getString(ConfigKeys.AUTH_URL));
			oauth2ClientOptions.setSite(authUrl + "/realms/" + clientConfig.getString(ConfigKeys.AUTH_REALM));
			oauth2ClientOptions.setTenant(clientConfig.getString(ConfigKeys.AUTH_REALM));
			String authClient = clientConfig.getString(ConfigKeys.AUTH_CLIENT);
			oauth2ClientOptions.setClientId(authClient);
			oauth2ClientOptions.setClientSecret(clientConfig.getString(ConfigKeys.AUTH_SECRET));
			oauth2ClientOptions.setAuthorizationPath("/oauth/authorize");
			JsonObject extraParams = new JsonObject();
			extraParams.put("scope", "profile");
			oauth2ClientOptions.setExtraParameters(extraParams);
			oauth2ClientOptions.setHttpClientOptions(new HttpClientOptions().setTrustAll(true).setVerifyHost(false).setConnectTimeout(120000));
			String authCallbackUri = clientConfig.getString(ConfigKeys.AUTH_CALLBACK_URI);
			if(authCallbackUri == null)
				throw new RuntimeException(String.format(configureAuthClientAuthCallbackUriEmpty, authClient));

			OpenIDConnectAuth.discover(vertx, oauth2ClientOptions).onSuccess(oauth2AuthenticationProvider -> {
				authorizationProvider = KeycloakAuthorization.create();

				ComputateOAuth2AuthHandlerImpl oauth2AuthHandler = new ComputateOAuth2AuthHandlerImpl(vertx, oauth2AuthenticationProvider, siteBaseUrl + authCallbackUri);
//					OAuth2AuthHandlerImpl oauth2AuthHandler = new OAuth2AuthHandlerImpl(vertx, oauth2AuthenticationProvider, siteBaseUrl + callbackUrl);
				Router tempRouter = Router.router(vertx);
				oauth2AuthHandler.setupCallback(tempRouter.get(authCallbackUri));
				authHandlers.put(authClientOpenApiId, oauth2AuthHandler);
				authProviders.put(authClientOpenApiId, oauth2AuthenticationProvider);
				LOG.info(String.format(configureAuthClientSuccess, authClient));
				promise.complete(oauth2AuthHandler);
			}).onFailure(ex -> {
				Exception ex2 = new RuntimeException("OpenID Connect Discovery failed", ex);
				LOG.error(configureAuthClientError, ex2);
				promise.fail(ex2);
			});
		} catch (Exception ex) {
			LOG.error(configureAuthClientError, ex);
			promise.fail(ex);
		}
		return promise.future();
	}

	/**	
	 * 
	 * Val.Error.enUS:Could not configure the auth server and API. 
	 * Val.Success.enUS:The auth server and API was configured successfully. 
	 * 
	 *	Configure the connection to the auth server and setup the routes based on the OpenAPI definition. 
	 *	Setup a callback route when returning from the auth server after successful authentication. 
	 *	Setup a logout route for logging out completely of the application. 
	 *	Return a promise that configures the authentication server and OpenAPI. 
	 **/
	public Future<Void> configureOpenApi() {
		Promise<Void> promise = Promise.promise();
		try {
			String siteBaseUrl = config().getString(ConfigKeys.SITE_BASE_URL);
			
			JsonObject authClients = Optional.ofNullable(config().getValue(ConfigKeys.AUTH_CLIENTS))
					.map(v -> v instanceof JsonObject ? (JsonObject)v : new JsonObject(v.toString()))
					.orElse(new JsonObject().put(Optional.ofNullable(config().getString(ConfigKeys.AUTH_OPEN_API_ID)).orElse("openIdConnect"), config()))
					;
			List<Future<OAuth2AuthHandler>> futures = new ArrayList<>();
			Map<String, OAuth2AuthHandler> authHandlers = new LinkedHashMap<>();
			Map<String, OAuth2Auth> authProviders = new LinkedHashMap<>();
			for(String authClientOpenApiId : authClients.fieldNames()) {
				JsonObject authClient = authClients.getJsonObject(authClientOpenApiId);
				futures.add(Future.future(promise1 -> {
					configureAuthClient(authClientOpenApiId, authClient, authHandlers, authProviders).onSuccess(a -> {
						promise1.complete();
					}).onFailure(ex -> {
						LOG.error(String.format("configureAuthClient failed. "), ex);
						promise1.fail(ex);
					});
				}));
			}
			Future.all(futures).onSuccess(a -> {
				oauth2AuthenticationProvider = authProviders.get(authProviders.keySet().toArray()[0]);
				authorizationProvider = KeycloakAuthorization.create();
		
				//ClusteredSessionStore sessionStore = ClusteredSessionStore.create(vertx);
				LocalSessionStore sessionStore = LocalSessionStore.create(vertx, SITE_NAME);
				SessionHandler sessionHandler = SessionHandler.create(sessionStore);
				if(StringUtils.startsWith(siteBaseUrl, "https://"))
					sessionHandler.setCookieSecureFlag(true);
		
				RouterBuilder.create(vertx, "webroot/openapi3-enUS.yaml").onSuccess(routerBuilder -> {
					routerBuilder.rootHandler(sessionHandler);
					routerBuilder.rootHandler(BodyHandler.create());

					routerBuilder.mountServicesFromExtensions();
	
					routerBuilder.serviceExtraPayloadMapper(routingContext -> new JsonObject()
							.put("uri", routingContext.request().uri())
							.put("method", routingContext.request().method().name())
							);
					for(String authClientOpenApiId : authHandlers.keySet()) {
						OAuth2AuthHandler authHandler = authHandlers.get(authClientOpenApiId);
						routerBuilder.securityHandler(authClientOpenApiId, authHandler);
					}
					routerBuilder.operation("callback").handler(ctx -> {
	
						// Handle the callback of the flow
						final String code = ctx.request().getParam("code");
	
						// code is a require value
						if (code == null) {
							ctx.fail(400);
							return;
						}
	
						final String state = ctx.request().getParam("state");
	
						final JsonObject config = new JsonObject().put("code", code);
	
						String authClientOpenApiId = config().getString(ConfigKeys.AUTH_OPEN_API_ID);
						JsonObject clientConfig = authClients.getJsonObject(authClientOpenApiId);
						String authCallbackUri = clientConfig.getString(ConfigKeys.AUTH_CALLBACK_URI);
						config.put("redirectUri", siteBaseUrl + authCallbackUri);
						OAuth2Auth authProvider = authProviders.get(authClientOpenApiId);

						authProvider.authenticate(config, res -> {
							if (res.failed()) {
								LOG.error("Failed to authenticate user. ", res.cause());
								ctx.fail(res.cause());
							} else {
								ctx.setUser(res.result());
								Session session = ctx.session();
								if (session != null) {
									// the user has upgraded from unauthenticated to authenticated
									// session should be upgraded as recommended by owasp
									Cookie cookie = Cookie.cookie("sessionIdBefore", session.id());
									if(StringUtils.startsWith(siteBaseUrl, "https://"))
										cookie.setSecure(true);
									ctx.addCookie(cookie);
									session.regenerateId();
									String redirectUri = session.get("redirect_uri");
									// we should redirect the UA so this link becomes invalid
									ctx.response()
											// disable all caching
											.putHeader(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate")
											.putHeader("Pragma", "no-cache")
											.putHeader(HttpHeaders.EXPIRES, "0")
											// redirect (when there is no state, redirect to home
											.putHeader(HttpHeaders.LOCATION, redirectUri != null ? redirectUri : "/")
											.setStatusCode(302)
											.end("Redirecting to " + (redirectUri != null ? redirectUri : "/") + ".");
								} else {
									// there is no session object so we cannot keep state
									ctx.reroute(state != null ? state : "/");
								}
							}
						});
	
					});
					routerBuilder.operation("callback").failureHandler(ex -> {
						LOG.error("Failed callback. ", ex);
					});
	
					routerBuilder.operation("logout").handler(rc -> {
						String redirectUri = rc.request().params().get("redirect_uri");
						if(redirectUri == null)
							redirectUri = "/";
						rc.clearUser();
						rc.response()
								.putHeader(HttpHeaders.LOCATION, redirectUri)
								.setStatusCode(302)
								.end("Redirecting to " + redirectUri + ".");
					});
					routerBuilder.operation("logout").handler(c -> {});
	
					router = routerBuilder.createRouter();
	//						mountCallback(oauth2AuthenticationProvider, router, callbackUrl, siteBaseUrl + callbackUrl);
	
					LOG.info(configureOpenApiSuccess);
					promise.complete();
				}).onFailure(ex -> {
					LOG.error(configureOpenApiError, ex);
					promise.fail(ex);
				});
			}).onFailure(ex -> {
				LOG.error(configureOpenApiError, ex);
				promise.fail(ex);
			});
		} catch (Exception ex) {
			LOG.error(configureOpenApiError, ex);
			promise.fail(ex);
		}
		return promise.future();
	}

	/**	
	 * Val.Complete.enUS:The config was configured successfully. 
	 * Val.Fail.enUS:Could not configure the config(). 
	 **/
	public static Future<JsonObject> configureConfig(Vertx vertx) {
		Promise<JsonObject> promise = Promise.promise();

		try {
			ConfigRetrieverOptions retrieverOptions = new ConfigRetrieverOptions();

			retrieverOptions.addStore(new ConfigStoreOptions().setType("file").setFormat("yaml").setConfig(new JsonObject().put("path", "application.yaml")));

			String configPath = System.getenv(ConfigKeys.CONFIG_PATH);
			if(StringUtils.isNotBlank(configPath)) {
				ConfigStoreOptions configIni = new ConfigStoreOptions().setType("file").setFormat("yaml").setConfig(new JsonObject().put("path", configPath));
				retrieverOptions.addStore(configIni);
			}

			ConfigStoreOptions storeEnv = new ConfigStoreOptions().setType("env");
			retrieverOptions.addStore(storeEnv);

			ConfigRetriever configRetriever = ConfigRetriever.create(vertx, retrieverOptions);
			configRetriever.getConfig().onSuccess(config -> {
				LOG.info("The config was configured successfully. ");
				promise.complete(config);
			}).onFailure(ex -> {
				LOG.error("Unable to configure site context. ", ex);
				promise.fail(ex);
			});
		} catch(Exception ex) {
			LOG.error("Unable to configure site context. ", ex);
			promise.fail(ex);
		}

		return promise.future();
	}

	/**
	 * 
	 * Val.Fail.enUS:Could not configure the shared worker executor. 
	 * Val.Complete.enUS:The shared worker executor "{}" was configured successfully. 
	 * 
	 *	Configure a shared worker executor for running blocking tasks in the background. 
	 *	Return a promise that configures the shared worker executor. 
	 **/
	public Future<Void> configureSharedWorkerExecutor() {
		Promise<Void> promise = Promise.promise();
		try {
			String name = "MainVerticle-WorkerExecutor";
			Integer workerPoolSize = System.getenv(ConfigKeys.WORKER_POOL_SIZE) == null ? 5 : Integer.parseInt(System.getenv(ConfigKeys.WORKER_POOL_SIZE));
			Long vertxMaxWorkerExecuteTime = config().getLong(ConfigKeys.VERTX_MAX_WORKER_EXECUTE_TIME);
			workerExecutor = vertx.createSharedWorkerExecutor(name, workerPoolSize, vertxMaxWorkerExecuteTime, TimeUnit.SECONDS);
			LOG.info(configureSharedWorkerExecutorComplete, name);
			promise.complete();
		} catch (Exception ex) {
			LOG.error(configureSharedWorkerExecutorFail, ex);
			promise.fail(ex);
		}
		return promise.future();
	}

	/**	
	 * Val.Complete.enUS:The health checks were configured successfully. 
	 * Val.Fail.enUS:Could not configure the health checks. 
	 * Val.ErrorDatabase.enUS:The database is not configured properly. 
	 * Val.EmptySolr.enUS:The Solr search engine is empty. 
	 * Val.ErrorSolr.enUS:The Solr search engine is not configured properly. 
	 * Val.ErrorVertx.enUS:The Vert.x application is not configured properly. 
	 *	Configure health checks for the status of the website and it's dependent services. 
	 *	Return a promise that configures the health checks. 
	 **/
	public Future<Void> configureHealthChecks() {
		Promise<Void> promise = Promise.promise();
		try {
			ClusterManager clusterManager = ((VertxImpl)vertx).getClusterManager();
			HealthCheckHandler healthCheckHandler = HealthCheckHandler.create(vertx);
			siteInstances = Optional.ofNullable(System.getenv(ConfigKeys.SITE_INSTANCES)).map(s -> Integer.parseInt(s)).orElse(1);
			workerPoolSize = System.getenv(ConfigKeys.WORKER_POOL_SIZE) == null ? null : Integer.parseInt(System.getenv(ConfigKeys.WORKER_POOL_SIZE));

			healthCheckHandler.register("vertx", 2000, a -> {
				a.complete(Status.OK(new JsonObject().put(ConfigKeys.SITE_INSTANCES, siteInstances).put("workerPoolSize", workerPoolSize)));
			});
			if(clusterManager != null) {
				healthCheckHandler.register("cluster", 2000, a -> {
					NodeInfo nodeInfo = clusterManager.getNodeInfo();
					JsonArray nodeArray = new JsonArray();
					clusterManager.getNodes().forEach(node -> nodeArray.add(node));
					a.complete(Status.OK(new JsonObject()
							.put("nodeId", clusterManager.getNodeId())
							.put("nodes", nodeArray)
							));
				});
			}
			router.get("/health").handler(healthCheckHandler);
			LOG.info(configureHealthChecksComplete);
			promise.complete();
		} catch (Exception ex) {
			LOG.error(configureHealthChecksFail, ex);
			promise.fail(ex);
		}
		return promise.future();
	}

	/**	
	 * Configure websockets for realtime messages. 
	 * Val.Complete.enUS:Configure websockets succeeded. 
	 * Val.Fail.enUS:Configure websockets failed. 
	 **/
	public Future<Void> configureWebsockets() {
		Promise<Void> promise = Promise.promise();
		try {
			SockJSBridgeOptions options = new SockJSBridgeOptions()
					.addOutboundPermitted(new PermittedOptions().setAddressRegex("websocket.*"))
					;
			router.mountSubRouter("/eventbus", SockJSHandler.create(vertx).bridge(options));
			LOG.info(configureWebsocketsComplete);
			promise.complete();
		} catch (Exception ex) {
			LOG.error(configureWebsocketsFail, ex);
			promise.fail(ex);
		}
		return promise.future();
	}

	/**
	 * Val.Fail.enUS:Handlebars was not configured properly. 
	 * Val.Complete.enUS:Handlebars was configured properly. 
	 */
	public Future<Void> configureHandlebars() {
		Promise<Void> promise = Promise.promise();
		try {
			templateEngine = HandlebarsTemplateEngine.create(vertx);
			handlebars = (Handlebars)templateEngine.unwrap();

			handlebars.registerHelpers(ConditionalHelpers.class);
			handlebars.registerHelpers(StringHelpers.class);
			handlebars.registerHelpers(AuthHelpers.class);
			handlebars.registerHelpers(SiteHelpers.class);
			handlebars.registerHelpers(DateHelpers.class);
			handlebars.registerHelpers(ProjectHelpers.class);
			handlebars.registerHelper("json", Jackson2Helper.INSTANCE);

			String templatePath = config().getString(ConfigKeys.TEMPLATE_PATH);
			if(StringUtils.isBlank(templatePath))
				templateHandler = TemplateHandler.create(templateEngine);
			else
				templateHandler = TemplateHandler.create(templateEngine, templatePath, "text/html");

			LOG.info(configureHandlebarsComplete);
			promise.complete();
		} catch(Exception ex) {
			LOG.error(configureHandlebarsFail, ex);
			promise.fail(ex);
		}
		return promise.future();
	}

	/**
	 * Val.Fail.enUS:The API was not configured properly. 
	 * Val.Complete.enUS:The API was configured properly. 
	 */
	public Future<Void> configureApi() {
		Promise<Void> promise = Promise.promise();
		try {
			SiteUserEnUSGenApiService.registerService(vertx.eventBus(), config(), workerExecutor, pgPool, kafkaProducer, webClient, oauth2AuthenticationProvider, authorizationProvider, templateEngine, vertx);
{% for JAVA_CLASS in JAVA_CLASSES %}
			{{ JAVA_CLASS.classeNomSimple_enUS_stored_string }}EnUSGenApiService.registerService(vertx.eventBus(), config(), workerExecutor, pgPool, kafkaProducer, webClient, oauth2AuthenticationProvider, authorizationProvider, templateEngine, vertx);
{% endfor %}

			LOG.info(configureApiComplete);
			promise.complete();
		} catch(Exception ex) {
			LOG.error(configureApiFail, ex);
			promise.fail(ex);
		}
		return promise.future();
	}

	/**
	 * Val.Fail.enUS:The UI was not configured properly. 
	 * Val.Complete.enUS:The UI was configured properly. 
	 */
	public Future<Void> configureUi() {
		Promise<Void> promise = Promise.promise();
		try {
			String staticPath = config().getString(ConfigKeys.STATIC_PATH);

			router.get("/").handler(a -> {
				a.reroute("/template/enUS/HomePage");
			});
 
			router.get("/api").handler(ctx -> {
				ctx.reroute("/template/enUS/openapi");
			});

			router.get("/template/enUS/HomePage").handler(ctx -> {
				try {
					ctx.put(ConfigKeys.STATIC_BASE_URL, config().getString(ConfigKeys.STATIC_BASE_URL));
					HomePage t = new HomePage();
					JsonObject query = new JsonObject();
					MultiMap queryParams = ctx.queryParams();
					for(String name : queryParams.names()) {
						JsonArray array = query.getJsonArray(name);
						List<String> vals = queryParams.getAll(name);
						if(array == null) {
							array = new JsonArray();
							query.put(name, array);
						}
						for(String val : vals) {
							array.add(val);
						}
					}
					ServiceRequest serviceRequest = new ServiceRequest(
							new JsonObject().put("path", JsonObject.mapFrom(ctx.pathParams())).put("query", query).put("cookie", JsonObject.mapFrom(ctx.cookieMap()))
							, ctx.request().headers()
							, Optional.ofNullable(ctx.user()).map(u -> u.principal()).orElse(null)
							, new JsonObject()
							);
					SiteRequestEnUS siteRequest = new SiteRequestEnUS();
					siteRequest.setConfig(config());
					siteRequest.setServiceRequest(serviceRequest);
					siteRequest.setRequestHeaders(ctx.request().headers());
					siteRequest.setWebClient(webClient);
					siteRequest.initDeepSiteRequestEnUS();
					t.promiseDeepForClass(siteRequest).onSuccess(a -> {
						try {
							JsonObject json = JsonObject.mapFrom(t);
							json.forEach(entry -> {
								ctx.put(entry.getKey(), entry.getValue());
							});
							ctx.next();
						} catch(Exception ex) {
							LOG.error("Failed to load home page. ", ex);
							ctx.fail(ex);
						}
					}).onFailure(ex -> {
						LOG.error("Failed to load home page. ", ex);
						ctx.fail(ex);
					});
				} catch(Exception ex) {
					LOG.error("Failed to load home page. ", ex);
					ctx.fail(ex);
				}
			});

			router.getWithRegex("(?<uri>\\/(?<lang>(?<lang1>[a-z][a-z])-(?<lang2>[a-z][a-z]))\\/.*)").handler(ctx -> {
				String uri = ctx.pathParam("uri");
				String url = String.format("%s%s", config().getString(ConfigKeys.SITE_BASE_URL), uri);
				String lang = String.format("%s%s", ctx.pathParam("lang1"), ctx.pathParam("lang2").toUpperCase());
				JsonObject query = new JsonObject();
				MultiMap queryParams = ctx.queryParams();
				for(String name : queryParams.names()) {
					JsonArray array = query.getJsonArray(name);
					List<String> vals = queryParams.getAll(name);
					if(array == null) {
						array = new JsonArray();
						query.put(name, array);
					}
					for(String val : vals) {
						array.add(val);
					}
				}
				ServiceRequest serviceRequest = new ServiceRequest(
						new JsonObject().put("path", JsonObject.mapFrom(ctx.pathParams())).put("query", query).put("cookie", JsonObject.mapFrom(ctx.cookieMap()))
						, ctx.request().headers()
						, Optional.ofNullable(ctx.user()).map(u -> u.principal()).orElse(null)
						, new JsonObject()
						);

				SiteRequestEnUS siteRequest = new SiteRequestEnUS();
				siteRequest.setConfig(config());
				siteRequest.setWebClient(webClient);
				siteRequest.setServiceRequest(serviceRequest);
				siteRequest.setRequestHeaders(ctx.request().headers());
				siteRequest.initDeepSiteRequestEnUS();
				SearchList<SitePage> l = new SearchList<>();
				l.q("*:*");
				l.setC(SitePage.class);
				l.fq(String.format("%s_docvalues_string:%s", SitePage.VAR_url, SearchTool.escapeQueryChars(url)));
				l.setStore(true);
				ctx.response().headers().add("Content-Type", "text/html");
				l.promiseDeepForClass(siteRequest).onSuccess(a -> {
					SitePage result = l.first();
					try {
						DynamicPage page = new DynamicPage();
						page.setPage(JsonObject.mapFrom(result));
						page.setUrl(url);
						page.setUri(uri);
						page.setPageImageUri(result.getPageImageUri());
						page.promiseDeepForClass(siteRequest).onSuccess(b -> {
							JsonObject json = JsonObject.mapFrom(page);
							json.put(ConfigKeys.STATIC_BASE_URL, config().getString(ConfigKeys.STATIC_BASE_URL));
							json.put(ConfigKeys.SITE_BASE_URL, config().getString(ConfigKeys.SITE_BASE_URL));
							json.put(ConfigKeys.GITHUB_ORG, config().getString(ConfigKeys.GITHUB_ORG));
							json.put(ConfigKeys.SITE_NAME, config().getString(ConfigKeys.SITE_NAME));
							json.put(ConfigKeys.SITE_DISPLAY_NAME, config().getString(ConfigKeys.SITE_DISPLAY_NAME));
							json.put(ConfigKeys.SITE_POWERED_BY_URL, config().getString(ConfigKeys.SITE_POWERED_BY_URL));
							json.put(ConfigKeys.SITE_POWERED_BY_NAME, config().getString(ConfigKeys.SITE_POWERED_BY_NAME));
							json.put(ConfigKeys.SITE_POWERED_BY_IMAGE_URI, config().getString(ConfigKeys.SITE_POWERED_BY_IMAGE_URI));
							templateEngine.render(json, Optional.ofNullable(config().getString(ConfigKeys.TEMPLATE_PATH)).orElse("templates") + "/" + lang + "/DynamicPage").onSuccess(buffer -> {
								try {
									ctx.response().end(buffer);
								} catch(Exception ex) {
									LOG.error(String.format("Failed to render page %s", uri), ex);
									ctx.fail(ex);
								}
							}).onFailure(ex -> {
								LOG.error(String.format("Failed to render page %s", uri), ex);
								ctx.fail(ex);
							});
						}).onFailure(ex -> {
							LOG.error(String.format("Failed to render page %s", uri), ex);
							ctx.fail(ex);
						});
					} catch (Exception ex) {
						LOG.error(String.format("Failed to render page %s", uri), ex);
						ctx.fail(ex);
					}
					
				}).onFailure(ex -> {
					LOG.error(String.format("Failed to render page %s", uri), ex);
					ctx.fail(ex);
				});
			});

			router.get("/template/*").handler(ctx -> {
				try {
					ctx.put(ConfigKeys.STATIC_BASE_URL, config().getString(ConfigKeys.STATIC_BASE_URL));
					ctx.put(ConfigKeys.GITHUB_ORG, config().getString(ConfigKeys.GITHUB_ORG));
					ctx.put(ConfigKeys.SITE_NAME, config().getString(ConfigKeys.SITE_NAME));
					ctx.put(ConfigKeys.SITE_DISPLAY_NAME, config().getString(ConfigKeys.SITE_DISPLAY_NAME));
					ctx.put(ConfigKeys.SITE_POWERED_BY_URL, config().getString(ConfigKeys.SITE_POWERED_BY_URL));
					ctx.put(ConfigKeys.SITE_POWERED_BY_NAME, config().getString(ConfigKeys.SITE_POWERED_BY_NAME));
					ctx.put(ConfigKeys.SITE_POWERED_BY_IMAGE_URI, config().getString(ConfigKeys.SITE_POWERED_BY_IMAGE_URI));
					ctx.next();
				} catch(Exception ex) {
					LOG.error("Failed to load page. ", ex);
					ctx.fail(ex);
				}
			});
			router.get("/template/*").handler(templateHandler);

			StaticHandler staticHandler = StaticHandler.create().setCachingEnabled(false).setFilesReadOnly(false);
			if(staticPath != null) {
				staticHandler.setAllowRootFileSystemAccess(true);
				staticHandler.setWebRoot(staticPath);
				staticHandler.setFilesReadOnly(true);
			}
			router.route("/static/*").handler(staticHandler);

			LOG.info(configureUiComplete);
			promise.complete();
		} catch(Exception ex) {
			LOG.error(configureUiFail);
			promise.fail(ex);
		}
		return promise.future();
	}

	public Future<Void> putVarsInRoutingContext(RoutingContext ctx) {
		Promise<Void> promise = Promise.promise();
		try {
			for(Entry<String, String> entry : ctx.queryParams()) {
				String paramName = entry.getKey();
				String paramObject = entry.getValue();
				String entityVar = null;
				String valueIndexed = null;

				switch(paramName) {
					case "var":
						entityVar = StringUtils.trim(StringUtils.substringBefore((String)paramObject, ":"));
						valueIndexed = URLDecoder.decode(StringUtils.trim(StringUtils.substringAfter((String)paramObject, ":")), "UTF-8");
						ctx.put(entityVar, valueIndexed);
						break;
				}
				promise.complete();
			}
		} catch(Exception ex) {
			LOG.error(String.format("putVarsInRoutingContext failed. "), ex);
			promise.fail(ex);
		}
		return promise.future();
	}

	/**	
	 * 
	 * Val.ErrorServer.enUS:The server is not configured properly. 
	 * Val.SuccessServer.enUS:The HTTP server is running: %s
	 * Val.BeforeServer.enUS:HTTP server starting: %s
	 * Val.Ssl.enUS:Configuring SSL: %s
	 * 
	 *	Start the Vert.x server. 
	 **/
	public Future<Void> startServer() {
		Promise<Void> promise = Promise.promise();

		try {
			Boolean sslPassthrough = config().getBoolean(ConfigKeys.SSL_PASSTHROUGH, false);
			String siteBaseUrl = config().getString(ConfigKeys.SITE_BASE_URL);
			Integer sitePort = config().getInteger(ConfigKeys.SITE_PORT);
			String sslJksPath = config().getString(ConfigKeys.SSL_JKS_PATH);
			String sslPrivateKeyPath = config().getString(ConfigKeys.SSL_KEY_PATH);
			String sslCertPath = config().getString(ConfigKeys.SSL_CERT_PATH);
			HttpServerOptions options = new HttpServerOptions();
			if(sslPassthrough) {
				if(sslPrivateKeyPath != null && sslCertPath != null) {
					options.setPemKeyCertOptions(new PemKeyCertOptions().setKeyPath(sslPrivateKeyPath).setCertPath(sslCertPath));
					LOG.info(String.format(startServerSsl, sslPrivateKeyPath));
					LOG.info(String.format(startServerSsl, sslCertPath));
				} else if(sslJksPath != null) {
					options.setKeyStoreOptions(new JksOptions().setPath(sslJksPath).setPassword(config().getString(ConfigKeys.SSL_JKS_PASSWORD)));
					LOG.info(String.format(startServerSsl, sslJksPath));
				}
				options.setSsl(true);
			}
			options.setPort(sitePort);
	
			LOG.info(String.format(startServerBeforeServer, siteBaseUrl));
			vertx.createHttpServer(options).requestHandler(router).listen(ar -> {
				if (ar.succeeded()) {
					LOG.info(String.format(startServerSuccessServer, siteBaseUrl));
					promise.complete();
				} else {
					LOG.error(startServerErrorServer, ar.cause());
					promise.fail(ar.cause());
				}
			});
		} catch (Exception ex) {
			LOG.error(startServerErrorServer, ex);
			promise.fail(ex);
		}

		return promise.future();
	}

	/**	
	 *	This is called by Vert.x when the verticle instance is undeployed. 
	 *	Setup the stopPromise to handle tearing down the server. 
	 **/
	@Override()
	public void stop(Promise<Void> promise) throws Exception, Exception {
		stopPgPool().onComplete(a -> {
			stopMqtt().onComplete(b -> {
				promise.complete();
			});
		});
	}

	/**
	 * Val.Fail.enUS:Could not close the database client connection. 
	 * Val.Complete.enUS:The database client connection was closed. 
	 **/
	public Future<Void> stopPgPool() {
		Promise<Void> promise = Promise.promise();

		if(pgPool != null) {
			pgPool.close().onSuccess(a -> {
				LOG.info(stopPgPoolComplete);
				promise.complete();
			}).onFailure(ex -> {
				LOG.error(stopPgPoolFail, ex);
				promise.fail(ex);
			});
		} else {
			promise.complete();
		}

		return promise.future();
	}

	/**
	 * Val.Fail.enUS:Could not close the MQTT client connection. 
	 * Val.Complete.enUS:The MQTT client connection was closed. 
	 **/
	public Future<Void> stopMqtt() {
		Promise<Void> promise = Promise.promise();

		if(mqttClient != null) {
			mqttClient.disconnect().onSuccess(a -> {
				LOG.info(stopMqttComplete);
				promise.complete();
			}).onFailure(ex -> {
				LOG.error(stopMqttFail, ex);
				promise.fail(ex);
			});
		} else {
			promise.complete();
		}

		return promise.future();
	}

	public String toId(String s) {
		if(s != null) {
			s = Normalizer.normalize(s, Normalizer.Form.NFD);
			s = StringUtils.lowerCase(s);
			s = StringUtils.trim(s);
			s = StringUtils.replacePattern(s, "\\s{1,}", "-");
			s = StringUtils.replacePattern(s, "[^\\w-]", "");
			s = StringUtils.replacePattern(s, "-{2,}", "-");
		}

		return s;
	}

	public Integer getSiteInstances() {
		return siteInstances;
	}

	public void setSiteInstances(Integer siteInstances) {
		this.siteInstances = siteInstances;
	}

	public Integer getWorkerPoolSize() {
		return workerPoolSize;
	}

	public void setWorkerPoolSize(Integer workerPoolSize) {
		this.workerPoolSize = workerPoolSize;
	}

	public Integer getJdbcMaxPoolSize() {
		return jdbcMaxPoolSize;
	}

	public void setJdbcMaxPoolSize(Integer jdbcMaxPoolSize) {
		this.jdbcMaxPoolSize = jdbcMaxPoolSize;
	}

	public Integer getJdbcMaxWaitQueueSize() {
		return jdbcMaxWaitQueueSize;
	}

	public void setJdbcMaxWaitQueueSize(Integer jdbcMaxWaitQueueSize) {
		this.jdbcMaxWaitQueueSize = jdbcMaxWaitQueueSize;
	}

	public PgPool getPgPool() {
		return pgPool;
	}

	public void setPgPool(PgPool pgPool) {
		this.pgPool = pgPool;
	}

	public WebClient getWebClient() {
		return webClient;
	}

	public void setWebClient(WebClient webClient) {
		this.webClient = webClient;
	}

	public Router getRouter() {
		return router;
	}

	public void setRouter(Router router) {
		this.router = router;
	}

	public WorkerExecutor getWorkerExecutor() {
		return workerExecutor;
	}

	public void setWorkerExecutor(WorkerExecutor workerExecutor) {
		this.workerExecutor = workerExecutor;
	}

	public OAuth2Auth getOauth2AuthenticationProvider() {
		return oauth2AuthenticationProvider;
	}

	public void setOauth2AuthenticationProvider(OAuth2Auth oauth2AuthenticationProvider) {
		this.oauth2AuthenticationProvider = oauth2AuthenticationProvider;
	}

	public AuthorizationProvider getAuthorizationProvider() {
		return authorizationProvider;
	}

	public void setAuthorizationProvider(AuthorizationProvider authorizationProvider) {
		this.authorizationProvider = authorizationProvider;
	}

	public HandlebarsTemplateEngine getTemplateEngine() {
		return templateEngine;
	}

	public void setTemplateEngine(HandlebarsTemplateEngine templateEngine) {
		this.templateEngine = templateEngine;
	}

	public Handlebars getHandlebars() {
		return handlebars;
	}

	public void setHandlebars(Handlebars handlebars) {
		this.handlebars = handlebars;
	}

	public TemplateHandler getTemplateHandler() {
		return templateHandler;
	}

	public void setTemplateHandler(TemplateHandler templateHandler) {
		this.templateHandler = templateHandler;
	}
}
