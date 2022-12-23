package com.sivalabs.devzone.links.web.controllers;

import com.opencsv.exceptions.CsvValidationException;
import com.sivalabs.devzone.config.annotations.AdminOnly;
import com.sivalabs.devzone.links.domain.services.LinksImportService;
import java.io.IOException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@AdminOnly
public class LinksUploadController {
    private final LinksImportService linksImportService;

    public LinksUploadController(LinksImportService linksImportService) {
        this.linksImportService = linksImportService;
    }

    @GetMapping("/links/upload")
    public String showUploadBookmarksPage() {
        return "upload-links";
    }

    @PostMapping("/links/upload")
    public String uploadBookmarks(
            @RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes)
            throws IOException, CsvValidationException {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("msg", "Please select the file to upload");
        } else {
            linksImportService.importLinks(file.getInputStream());
            redirectAttributes.addFlashAttribute("msg", "Links imported successfully");
        }
        return "redirect:/links/upload";
    }
}
