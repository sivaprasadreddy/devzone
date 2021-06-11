package com.sivalabs.devzone.web.controllers;

import com.opencsv.exceptions.CsvValidationException;
import com.sivalabs.devzone.annotations.AdminOnly;
import com.sivalabs.devzone.domain.services.LinksImportService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@Slf4j
public class LinksUploadController {
    private final LinksImportService linksImportService;

    @GetMapping("/links/upload")
    @AdminOnly
    public String showUploadBookmarksPage() {
        return "upload-links";
    }

    @PostMapping("/links/upload")
    @AdminOnly
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
