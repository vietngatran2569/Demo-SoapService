package com.example.soapdemoaviet.config;

import com.example.soapdemoaviet.security.UserDetailsServiceImp;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.server.EndpointInterceptor;
import org.springframework.ws.server.endpoint.interceptor.PayloadLoggingInterceptor;
import org.springframework.ws.soap.security.wss4j2.Wss4jSecurityInterceptor;
import org.springframework.ws.soap.security.wss4j2.callback.SpringSecurityPasswordValidationCallbackHandler;
import org.springframework.ws.soap.security.wss4j2.support.CryptoFactoryBean;
import org.springframework.ws.soap.server.endpoint.interceptor.PayloadValidatingInterceptor;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

import java.io.IOException;
import java.util.List;

@EnableWs
@Configuration
public class SoapConfig extends WsConfigurerAdapter {
    @Bean
    public ServletRegistrationBean messageDispatcherServlet(ApplicationContext applicationContext){
        MessageDispatcherServlet messageDispatcherServlet = new MessageDispatcherServlet();
        messageDispatcherServlet.setApplicationContext(applicationContext);
        messageDispatcherServlet.setTransformWsdlLocations(true);
        return new ServletRegistrationBean(messageDispatcherServlet,"/taskSoap/*");
    }

    @Bean(name = "task")
    public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema employeeXSDSchema){
        DefaultWsdl11Definition defaultWsdl11Definition = new DefaultWsdl11Definition();
        defaultWsdl11Definition.setPortTypeName("TaskPort");
        defaultWsdl11Definition.setLocationUri("/taskSoap");
        defaultWsdl11Definition.setTargetNamespace("http://www.fintech/task");
        defaultWsdl11Definition.setSchema(employeeXSDSchema);
        return defaultWsdl11Definition;
    }

    @Bean
    public XsdSchema employeeXSDSchema() {
        return new SimpleXsdSchema(new ClassPathResource("xsds/task.xsd"));
    }

    @Bean
    PayloadValidatingInterceptor payloadValidatingInterceptor() {
        PayloadValidatingInterceptor validatingInterceptor = new PayloadValidatingInterceptor();
        validatingInterceptor.setValidateRequest(true);
        validatingInterceptor.setValidateResponse(true);
        validatingInterceptor.setXsdSchema(employeeXSDSchema());
        return validatingInterceptor;
    }

    @Bean
    PayloadLoggingInterceptor payloadLoggingInterceptor() {
        return new PayloadLoggingInterceptor() {
            @Override
            protected boolean isLogEnabled() {
                return true;
            }
        };
    }

    @SneakyThrows
    @Override
    public void addInterceptors(List<EndpointInterceptor> interceptors) {
        interceptors.add(payloadValidatingInterceptor());
        interceptors.add(securityInterceptor1());
        interceptors.add(payloadValidatingInterceptor());
    }
////
////    @Bean
////    XwsSecurityInterceptor securityInterceptor() {
////        XwsSecurityInterceptor securityInterceptor = new XwsSecurityInterceptor();
////        securityInterceptor.setCallbackHandler(callbackHandle());
////        securityInterceptor.setPolicyConfiguration(new ClassPathResource("securityPolicy.xml"));
////        return securityInterceptor;
////    }
////
////    @Bean
////    SimplePasswordValidationCallbackHandler callbackHandle() {
////        SimplePasswordValidationCallbackHandler callbackHandler = new SimplePasswordValidationCallbackHandler();
////        callbackHandler.setUsersMap(Collections.singletonMap("admin1", "pwd1234"));
////        return callbackHandler;
////    }
    /////////wss4j
//    @Bean
//    XwsSecurityInterceptor securityInterceptor() {
//        XwsSecurityInterceptor securityInterceptor = new XwsSecurityInterceptor();
//        securityInterceptor.setCallbackHandler(callHandBack());
//        securityInterceptor.setPolicyConfiguration(new ClassPathResource("securityDigestPolicy.xml"));
//        return securityInterceptor;
//    }
//
    @Autowired
    UserDetailsServiceImp userdetailService;

//    @Bean
//    SpringSecurityPasswordValidationCallbackHandler callHandBack() {
//        SpringSecurityPasswordValidationCallbackHandler callbackHandler = new SpringSecurityPasswordValidationCallbackHandler();
//        callbackHandler.setUserDetailsService(userdetailService);
//        return callbackHandler;
//    }
    /////////wss4j


//    @Bean
//    Wss4jSecurityInterceptor securityInterceptor1() {
//        Wss4jSecurityInterceptor securityInterceptor = new Wss4jSecurityInterceptor();
//        securityInterceptor.setValidationCallbackHandler(callbackHandle1());
//        securityInterceptor.setValidationActions("UsernameToken");
//        return securityInterceptor;
//    }
//
//    @Bean
//    SpringSecurityPasswordValidationCallbackHandler callbackHandle1() {
//        SpringSecurityPasswordValidationCallbackHandler callbackHandler = new SpringSecurityPasswordValidationCallbackHandler();
//        callbackHandler.setUserDetailsService(userdetailService);
//        return callbackHandler;
//    }


    @Bean
    Wss4jSecurityInterceptor securityInterceptor1() throws Exception{
        Wss4jSecurityInterceptor securityInterceptor = new Wss4jSecurityInterceptor();

        securityInterceptor.setSecurementActions("Timestamp Signature");
        securityInterceptor.setSecurementUsername("mycert");
        securityInterceptor.setSecurementPassword("12345678");
        securityInterceptor.setSecurementSignatureCrypto(getCryptoFactoryBean().getObject());
        return securityInterceptor;
    }

    @Bean
    public CryptoFactoryBean getCryptoFactoryBean() throws IOException {
        CryptoFactoryBean cryptoFactoryBean=new CryptoFactoryBean();
        cryptoFactoryBean.setKeyStorePassword("12345678");
        cryptoFactoryBean.setKeyStoreLocation(new ClassPathResource("server.keystore"));
        return cryptoFactoryBean;
    }
}
