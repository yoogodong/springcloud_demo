package dong.yoogo.springcloud_demo.gallery_service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/")
public class HomeController {

    Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private Environment environment;

    @RequestMapping("/")
    public String home(){
        return "Hello from gallery server on port : "+environment.getProperty("local.server.port");
    }

    @HystrixCommand(fallbackMethod = "fallback")
    @RequestMapping("/{id}")
    public Gallery getGallery(@PathVariable final int id){
        logger.info("this is call for id {}",id);
        Gallery gallery = new Gallery();
        gallery.setId(id);

        List<Object> images = restTemplate.getForObject("http://image-service/images/",List.class);
        gallery.setImages(images);
        logger.info("return for id"+id);
        return  gallery;
    }

    @RequestMapping("/admin")
    public String homeAdmin(){
        return "This is the admin area of Gallery service running at port: " + environment.getProperty("local.server.port");
    }

    public Gallery fallback(int galleryId, Throwable hystrixCommand) {
        Gallery gallery = new Gallery();
        gallery.setId(9999999);
        return gallery;
    }
}

