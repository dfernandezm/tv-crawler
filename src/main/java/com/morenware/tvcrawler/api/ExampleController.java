package com.morenware.tvcrawler.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.morenware.tvcrawler.persistence.domain.TorrentSearchResult;
import com.morenware.tvcrawler.service.TorrentService;
import com.morenware.tvcrawler.service.torrentsearch.TorrentSearchService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

@Controller
public class ExampleController {

    private static Logger LOG = LogManager.getLogger();

    @Resource
    private TorrentSearchService torrentSearchService;

    @Resource
    private TorrentService torrentService;

    @RequestMapping("/")
    public String getHome(ModelMap model) {
        model.addAttribute("test","ja");
        return "home";
    }


    @RequestMapping(value = "/file", method = RequestMethod.GET)
    public @ResponseBody
    FileTestDto getFile() {

        FileTestDto fileDto = new FileTestDto();
        fileDto.setName("Something");
        fileDto.setValue("Something else");
        LOG.info("This file is good, is goodnesseeees");

        return fileDto;
    }

   /*
    "file": {
        "name": "I like it",
                "value": "I love it"
    }
   */
    @RequestMapping(value = "/file", method = RequestMethod.POST)
    public @ResponseBody
    FileTestDto setFile(@JsonProperty("file") FileTestDto fileDto) {

        String name = fileDto.getName();
        String value = fileDto.getValue();

        return fileDto;
    }

    @RequestMapping(value = "/file2", method = RequestMethod.POST)
    public @ResponseBody
    TestingDto setFile(@JsonProperty("file2") TestingDto fileDto) {
        String name = fileDto.getTestValue();
        return fileDto;
    }

    @RequestMapping(value="/crawler/start/{siteId}", method = RequestMethod.GET)
    public @ResponseBody
    List<TorrentSearchResult> startCrawler(HttpSession session) {

        torrentSearchService.crawlDivxTotal();
        return new ArrayList<>();
    }

    @RequestMapping(value="/crawler/stop", method = RequestMethod.GET)
    public void stopCrawler(HttpSession session) {

        if (session.getAttribute("futures") != null) {
            List<Future<?>> futuresToCancel = (List<Future<?>>) session.getAttribute("futures");
            for (Future<?> future : futuresToCancel) {

                if (!future.isDone() && !future.isCancelled()) {
                    future.cancel(true);
                }
            }
        }

        session.setAttribute("futures", null);
    }


    @ExceptionHandler(value = Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ResponseEntity<Object> generalException(Throwable throwable, WebRequest request) {
        // return new ApiError(Throwables.getRootCause(exception).getMessage());
        LOG.error("Unexpected error occurred -- ", throwable);
        return new ResponseEntity<Object>("{\"status\": \"ERROR\"}", new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }



}