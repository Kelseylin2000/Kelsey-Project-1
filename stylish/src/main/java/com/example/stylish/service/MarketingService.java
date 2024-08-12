package com.example.stylish.service;

import java.util.List;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.stylish.dto.CampaignDto;
import com.example.stylish.dto.CampaignListResponseDto;
import com.example.stylish.repository.MarketingRepository;

@Service
public class MarketingService {

    private ImageSaveService imageSaveService;
    private MarketingRepository marketingRepository;
    private RedisTemplate<String, Object> redisTemplate;

    private static final String CAMPAIGN_CACHE_KEY = "campaign_data";

    public MarketingService(ImageSaveService imageSaveService, MarketingRepository marketingRepository, RedisTemplate<String, Object> redisTemplate){
        this.imageSaveService = imageSaveService;
        this.marketingRepository = marketingRepository;
        this.redisTemplate = redisTemplate;
    }
    
    public void uploadCampaign(CampaignDto campaign, MultipartFile pictureFile){
        String picturePath = imageSaveService.saveImage(pictureFile, "campaign");
        campaign.setPicture(picturePath);
        marketingRepository.saveCampaign(campaign.getProductId(), campaign.getPicture(), campaign.getStory());

        try {
            // delete campaign data in cache
            redisTemplate.delete(CAMPAIGN_CACHE_KEY);
        } catch (Exception e) {
            System.err.println("Redis connection timed out or failed: " + e.getMessage());
        }
    }

    public CampaignListResponseDto getCampaign(){

        List<CampaignDto> campaigns = null;

        try{
            // try to get data from cache
            campaigns = (List<CampaignDto>) redisTemplate.opsForValue().get(CAMPAIGN_CACHE_KEY);

            // if there is no data in cache, get data from database, and save it into cache
            if (campaigns == null) {
                // get data from database
                campaigns = marketingRepository.getCampaignList();
                // save it into cache
                redisTemplate.opsForValue().set(CAMPAIGN_CACHE_KEY, campaigns);
            }
            
        }catch(Exception e){
            System.err.println("Redis connection timed out or failed: " + e.getMessage());
            // if it fails to connect redis, directly get data from database
            campaigns = marketingRepository.getCampaignList();
        }

        return new CampaignListResponseDto(campaigns);
    }
}
