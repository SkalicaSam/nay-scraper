package com.example.nay_scraper.nayScrapper;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
public class NayWebController {

    private final NayScraperService scraperService;
    public NayWebController(NayScraperService scraperService) {
        this.scraperService = scraperService;
    }

    @GetMapping("/check")
    public String showForm(@RequestParam(required = false) String productUrl, Model model) {
        if (productUrl != null && !productUrl.isEmpty()) {
            return processForm2(productUrl, model);
        }
        return "form";
    }

    @PostMapping("/check")
    public String processForm2(@RequestParam String productUrl, Model model) {
        productUrl = scraperService.getLinkOfProductFromInput(productUrl);
        ProductInfo productInfo = scraperService.getObjetAvailabilityForSkalica(productUrl);
        if (productInfo != null) {
            model.addAttribute("product", productInfo);
        } else {
            model.addAttribute("error", "Nepodarilo sa načítať údaje.");
        }
        return "resultObject";
    }

    @GetMapping("/variants")
    @ResponseBody
    public List<ProductVariant> getVariants(@RequestParam String url) {
        return scraperService.getProductVariants(url);
    }


}
