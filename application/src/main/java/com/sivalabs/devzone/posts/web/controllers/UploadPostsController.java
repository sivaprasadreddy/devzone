package com.sivalabs.devzone.posts.web.controllers;

import com.opencsv.exceptions.CsvValidationException;
import com.sivalabs.devzone.config.annotations.AdminOnly;
import com.sivalabs.devzone.posts.application.usecases.uploadposts.UploadPostsHandler;
import java.io.IOException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@AdminOnly
public class UploadPostsController {
    private final UploadPostsHandler uploadPostsHandler;

    public UploadPostsController(UploadPostsHandler uploadPostsHandler) {
        this.uploadPostsHandler = uploadPostsHandler;
    }

    @GetMapping("/posts/upload")
    public String showUploadBookmarksPage() {
        return "upload-posts";
    }

    @PostMapping("/posts/upload")
    public String uploadBookmarks(
            @RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes)
            throws IOException, CsvValidationException {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("msg", "Please select the file to upload");
        } else {
            uploadPostsHandler.importPosts(file.getInputStream());
            redirectAttributes.addFlashAttribute("msg", "Posts imported successfully");
        }
        return "redirect:/posts/upload";
    }
}
