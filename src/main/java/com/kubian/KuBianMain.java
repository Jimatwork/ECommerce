package com.kubian;

import org.apache.catalina.connector.Connector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
//implements JsonRpcConfigurer
@SpringBootApplication
public class KuBianMain  {
//	public static final String KMSS_URIS_PROPERTY = "kms.uris";
//	public static final String KMSS_URIS_DEFAULT = "[ \"ws://209.160.24.141:8888/kurento\" ]";
//
	private static final Logger log = LoggerFactory.getLogger(KuBianMain.class);
//
//	@Bean
//	@ConditionalOnMissingBean
//	public KurentoClientProvider kmsManager() {
//		JsonArray kmsUris = getPropertyJson(KMSS_URIS_PROPERTY, KMSS_URIS_DEFAULT, JsonArray.class);
//
//		List<String> kmsWsUris = JsonUtils.toStringList(kmsUris);
//
//		if (kmsWsUris.isEmpty()) {
//			throw new IllegalArgumentException(KMSS_URIS_PROPERTY + " should contain at least one kms url");
//		}
//
//		String firstKmsWsUri = kmsWsUris.get(0);
//
//		if (firstKmsWsUri.equals("autodiscovery")) {
//			log.info("Using autodiscovery rules to locate KMS on every pipeline");
//			return new AutodiscoveryKurentoClientProvider();
//		} else {
//			log.info("Configuring Kurento Room Server to use first of the following kmss: " + kmsWsUris);
//			return new FixedOneKmsManager(firstKmsWsUri);
//		}
//	}
//
//	@Bean
//	@ConditionalOnMissingBean
//	public JsonRpcNotificationService notificationService() {
//		return new JsonRpcNotificationService();
//	}
//
//	@Bean
//	@ConditionalOnMissingBean
//	public NotificationRoomManager roomManager() {
//		return new NotificationRoomManager(notificationService(), kmsManager());
//	}
//
//	@Bean
//	@ConditionalOnMissingBean
//	public JsonRpcUserControl userControl() {
//		return new JsonRpcUserControl(roomManager());
//	}
//
//	@Bean
//	@ConditionalOnMissingBean
//	public RoomJsonRpcHandler roomHandler() {
//		return new RoomJsonRpcHandler(userControl(), notificationService());
//	}
//
//	@Override
//	public void registerJsonRpcHandlers(JsonRpcHandlerRegistry registry) {
//		registry.addHandler(roomHandler().withPingWatchdog(true), "/tcmRoom");
//	}
//
//	@Bean
//	public ServletServerContainerFactoryBean createWebSocketContainer() {
//		ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
//		container.setMaxTextMessageBufferSize(1000000); // chars
//		container.setMaxBinaryMessageBufferSize(1000000); // bytes
//		return container;
//	}

//	@Bean
//	public EmbeddedServletContainerFactory servletContainer() {
//		TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory() {
//			@Override
//			protected void postProcessContext(Context context) {
//				SecurityConstraint constraint = new SecurityConstraint();
//				constraint.setUserConstraint("CONFIDENTIAL");
//				SecurityCollection collection = new SecurityCollection();
//				collection.addPattern("/*");
//				constraint.addCollection(collection);
//				context.addConstraint(constraint);
//			}
//		};
//		tomcat.addAdditionalTomcatConnectors(httpConnector());
//		return tomcat;
//	}

//	public Connector httpConnector() {
//		Connector connector = new Connector(TomcatEmbeddedServletContainerFactory.DEFAULT_PROTOCOL);
//		connector.setScheme("http");
//		// Connector监听的http的端口号
//		connector.setPort(8888);
//		connector.setSecure(false);
//		// 监听到http的端口号后转向到的https的端口号
//		connector.setRedirectPort(9443);
//		return connector;
//	}
	@Bean
	public EmbeddedServletContainerCustomizer containerCustomizer() {
		return new EmbeddedServletContainerCustomizer() {
			@Override
			public void customize(ConfigurableEmbeddedServletContainer container) {
				if (container instanceof TomcatEmbeddedServletContainerFactory) {
					TomcatEmbeddedServletContainerFactory containerFactory = (TomcatEmbeddedServletContainerFactory) container;
					Connector connector = new Connector(TomcatEmbeddedServletContainerFactory.DEFAULT_PROTOCOL);
					connector.setPort(8858);
					containerFactory.addAdditionalTomcatConnectors(connector);
				}
			}
		};
	}

	public static void main(String[] args) throws Exception {
		start(args);
	}

	public static ConfigurableApplicationContext start(String[] args) {
		log.info("Using /dev/urandom for secure random generation");
		System.setProperty("java.security.egd", "file:/dev/./urandom");
		return SpringApplication.run(KuBianMain.class, args);
	}

}
