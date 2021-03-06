package org.kevin.OwnBlog.controller;

import org.kevin.OwnBlog.dao.MediaRepository;
import org.kevin.OwnBlog.model.Album;
import org.kevin.OwnBlog.model.Media;
import org.kevin.OwnBlog.service.AlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.List;

/**
 * Created by Kevin.Z on 2017/12/18.
 */
@RequestMapping("/albums")
@Controller
public class AlbumController {
    @Autowired
    private AlbumService albumService;
    @Autowired
    private MediaRepository mediaRepository;

    @RequestMapping("/picture")
    public String picture(long id, ModelMap model) {
        Album album = albumService.findById(id);
        List<Media> mediaList = mediaRepository.findAllByAlbumId(id);
        model.addAttribute("medias", mediaList);
        model.addAttribute("albumid", id);
        return "picture";
    }

    @RequestMapping("/createAlbum")
    public String create(Album album, HttpServletRequest request) {
        album.setCreateTime(new Date());
        albumService.save(album);
        return "redirect:../albums";
    }

    @RequestMapping("/uploadFiles")
    @ResponseBody
    public String upload(HttpServletRequest request, @RequestParam(value = "fileArray", required = false) MultipartFile[] files) {
        long albumId = Long.parseLong(request.getParameter("albumId"));
        long c = System.currentTimeMillis();

        try {
            //String baseSrc = "/"+ResourceUtils.getURL("classpath:").getPath().substring(1) + "static/img/" + albumId;
            String baseSrc = "/media/img/" + albumId;
            File folder = new File(baseSrc);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            int duplication = 1;
            String src;
            for (MultipartFile mf : files) {
                String originalName = mf.getOriginalFilename();
                src = baseSrc + "/" + originalName;
                File targetFile = new File(src);
                //targetFile.setWritable(true,false);
                while (true) {
                    if (targetFile.exists()) {
                        String fileName = originalName.substring(0,originalName.lastIndexOf("."));
                        String extension = originalName.substring(originalName.lastIndexOf(".") + 1, originalName.length());
                        src = baseSrc + "/" + fileName + duplication + extension;
                        targetFile = new File(src);
                        duplication++;
                    } else {
                        duplication = 1;
                        break;
                    }
                }
                if(targetFile.createNewFile())
                    FileCopyUtils.copy(mf.getBytes(), new FileOutputStream(targetFile));

                Media media = new Media();
                media.setAlbumId(albumId);
                media.setSrc(src.substring(1));
                mediaRepository.save(media);// takes 857 ms to save the data...
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "failed";
        }
        return "done";
    }
}
