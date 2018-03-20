package org.kevin.OwnBlog.controller;

import org.kevin.OwnBlog.model.Album;
import org.kevin.OwnBlog.model.Blog;
import org.kevin.OwnBlog.model.Translation;
import org.kevin.OwnBlog.model.Twitter;
import org.kevin.OwnBlog.service.AlbumService;
import org.kevin.OwnBlog.service.BlogService;
import org.kevin.OwnBlog.service.TranslationService;
import org.kevin.OwnBlog.service.TwitterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;

/**
 * Created by Kevin.Z on 2017/12/18.
 */
@Controller
public class MainController {
    @Autowired
    private BlogService blogService;
    @Autowired
    private AlbumService albumService;
    @Autowired
    private TwitterService twitterService;
    @Autowired
    private TranslationService translationService;

    @RequestMapping("/")
    public String mainPage() {
        return "main2";
    }

    @RequestMapping("/index")
    public String index() {
        return "main2";
    }

    @RequestMapping("/blogs")
    public String blogs(ModelMap map) {
        List<Blog> blogs = blogService.findAll();
        Collections.reverse(blogs);
        map.addAttribute("blogs", blogs);
        return "blogs";
    }

    @RequestMapping("/albums")
    public String albums(ModelMap map) {
        List<Album> albums = albumService.findAll();
        map.addAttribute("albums", albums);
        return "albums";
    }

    @RequestMapping("/twitters")
    public String twitters(ModelMap map) {
        List<Twitter> twitters = twitterService.findAll();
        Collections.reverse(twitters);
        map.addAttribute("twitters", twitters);
        return "twitters";
    }

    @RequestMapping("/addblog")
    public String addBlog() {
        return "addBlog";
    }

    @RequestMapping("/translation")
    public String translation(ModelMap map, HttpServletRequest request) {
        List<Translation> translations = translationService.findAll();
        map.addAttribute("translations", translations);

        String value = request.getParameter("id");
        if (value != null && !(value.trim()).equals("")) {
            long id = Long.parseLong(value);
            Translation translation = translationService.findById(id);
            map.addAttribute("translation", translation);
        } else {
            map.addAttribute("translation", translations.get(0));
        }
        return "translations";
    }
}