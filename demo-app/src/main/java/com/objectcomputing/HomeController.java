package com.objectcomputing;

import io.micronaut.context.annotation.Value;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

@Controller("/home")
public class HomeController {

    private static final Logger LOG = LoggerFactory.getLogger(HomeController.class);

    @Value("${getFailureChance:1000}")
    Integer getFailureChance;

    @Value("${postFailureChance:1000}")
    Integer postFailureChance;

    @Get
    HttpResponse<String> index() {

        if (getRandomNumber(0, getFailureChance).equals(getFailureChance / 2)) {
            return HttpResponse.serverError();
        }

        return HttpResponse.ok("Hello world!");
    }

    @Get("/{name}")
    HttpResponse<WidgetDTO> getWidget(String name) {

        if (getRandomNumber(0, getFailureChance).equals(getFailureChance / 2)) {
            LOG.warn("Triggering 500...");
            return HttpResponse.serverError();
        }

        if (getRandomNumber(0, getFailureChance).equals(getFailureChance / 2)) {
            LOG.warn("Triggering 404...");
            return HttpResponse.notFound();
        }

        LOG.debug("Returning 200...");
        return HttpResponse.ok(new WidgetDTO(name, getRandomNumber(1, name.length())));
    }


    @Post
    HttpResponse<WidgetDTO> saveWidget(WidgetDTO widgetDTO) {
        //save widget

        if (getRandomNumber(0, postFailureChance).equals(postFailureChance / 2)) {

            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return HttpResponse.serverError();
        }

        return HttpResponse.created(widgetDTO);

    }

    //To simulate failures
    private Integer getRandomNumber(Integer min, Integer max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

}
