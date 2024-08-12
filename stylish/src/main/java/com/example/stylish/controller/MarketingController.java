package com.example.stylish.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.stylish.dto.CampaignDto;
import com.example.stylish.dto.CampaignListResponseDto;
import com.example.stylish.service.MarketingService;


@Controller
public class MarketingController {

    private MarketingService marketingService;

    public MarketingController(MarketingService marketingService){
        this.marketingService = marketingService;
    }

    @GetMapping("/admin/campaign.html")
    public String campaignManage(){
        return "campaign";
    }

    @PostMapping("/api/1.0/admin/uploadCampaign")
    public String uploadCampaign(@ModelAttribute CampaignDto campaign,@RequestParam MultipartFile pictureFile, RedirectAttributes redirectAttributes) {
        marketingService.uploadCampaign(campaign, pictureFile);
        redirectAttributes.addFlashAttribute("message", "Successfully uploaded.");
        return "redirect:/admin/campaign.html";
    }

    @GetMapping("/api/1.0/marketing/campaigns")
    public ResponseEntity<CampaignListResponseDto> getCampaign() {
        CampaignListResponseDto campaignList = marketingService.getCampaign();
        return ResponseEntity.ok(campaignList);    
    }
}
