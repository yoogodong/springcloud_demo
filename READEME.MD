### 基本业务
    gallery_service 调用 image_service, 后者给前者提供照片服务。其他都是系统服务

### 系统服务
- 服务注册和发现，采用了独立的 eureka_demo 服务
- 网关，采用了独立的 zuul_demo
- 负载均衡， 没有独立的服务
- 认证服务，和 zuul_demo 及 auth_service 两个服务相关
- 断路器，体现在 gallery_service 上

### 业务服务配置
- 标注自己是 SpringBootApplicaiton,是 Eureka的客户
- 配置端点
- 有可能需要断路器声明

### 服务注册发现
包含两部分，一是服务由 eureka_demo 提供， 二是客户，每个应用服务都是注册发现服务的客户。 
##### 服务配置的要点，参考eureka_demo 
- 组件 spring-cloud-starter-netflix-eureka-server
- EurekaApplication 标注了 @EnableEurekaServer
##### client 配置要点：
- 组件 spring-cloud-starter-netflix-eureka-client
- 主类 标注  @EnableEurekaClient
- 配置自己的名称和端口，以及注册端点，注意注册端点不是管理端点
  - spring.application.name=gallery-service
  - server.port=8100
  - eureka.client.service-url.default-zone=http://localhost:8761/eureka


### 网关
- 组件 spring-cloud-starter-netflix-zuul
- ZuulApplication 标注 @EnableZuulProxy
- 然后就是要把 service-id 映射到路径上，service-id 当然是在注册服务上的名字
  - zuul.routes.gallery-service.path=/gallery/**
  - zuul.routes.gallery-service.service-id=gallery-service
- 当然网关也要作为 Eureka 的 client

### 负载均衡，体现在 gallery_service ，隐藏在背后的是 Ribbon 服务
- 配置一个bean：需要负载均衡的 RestTemplate

      @Configuration
      class RestTemplateConfig {
          @Bean
          @LoadBalanced
          public RestTemplate getTemplate() {
              return new RestTemplate();
          }
      }
      
- 使用这个 restTemplate 发请求即可

### 断路器
断路器服务体现在 gallery_service
- 在 GalleryApplication 有标注：@EnableCircuitBreaker
- 在 HomeController 方法 getGallery(),此方法远程调用 image_service, 
所以此方法上加入了标注@HystrixCommand(fallbackMethod = "fallback")，
这里的fallback 方法就是getGallery()的后备方法。


### 认证服务
认证由两个服务承担，zuul_service 和 auth_service 
- auth_service, 客户端应该先访问这个，对认证服务的请求会被 zuul_service 放行。 
auth_service 验证用户的凭证，然后给客户返回一个JWT 的 token. 
- zuul_service 负责验证 token

auth_service 在主类和application.yml 和普通的应用服务完全一样，关键在于：
- 继承并自定义WebSecurityConfigurerAdapter ， 提供基本的安全配置，并将对请求的处理交给自定义的 Filter
- 自定义的 Filter 负责认证用户的凭证，产生 Token

zuul_service 安全相关的内容与 auth_service 类似，一个安全配置+一个Filter
- 安全配置拦截除了 /auth 之外的url， 注意我在结尾处 把 authenticated() 改成了 permitAll() ,以暂时禁用 认证

### Log Tracing 日志追踪
只需要一个组件， spring-cloud-starter-sleuth

-  日志信息，id 部分 [image-service,eb99d57faf4c3bd1,59039685343b4560,false]，分别是 [服务，跨服务id,服务内id,?]

### 参考
https://medium.com/omarelgabrys-blog/microservices-with-spring-boot-intro-to-microservices-part-1-c0d24cd422c 

