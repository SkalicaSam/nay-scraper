package com.example.nay_scraper.nayScrapper;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.jsoup.Jsoup;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Service
public class NayScraperService {

    public ProductInfo getObjetAvailabilityForSkalica(String productUrl) {
        try {
            // Zkontrolujeme, zda URL je platná
            if (!productUrl.startsWith("http://") && !productUrl.startsWith("https://")) {
                return new ProductInfo(
                        "Chyba",
                        "Neplatná URL adresa " + productUrl,
                        "0",
                        "0",
                        "",
                        "",
                        "",
                        "",
                        "Stránka neexistuje",
                        productUrl
                );
            }

            Document productPage = Jsoup.connect(productUrl)
                    .userAgent("Mozilla/5.0")
                    .timeout(10000)
                    .get();

            Element productDiv = productPage.selectFirst("div.product-detail");
            String productId = null;
            if (productDiv != null) {
                productId = productDiv.attr("data-product-id");
            }

            Element productDetailTitle = productPage.select(".product-detail-title").first();
            String productTitle = productDetailTitle != null ? productDetailTitle.text() : "";

            Element priceElement = productPage.select(".product-price.price-level-1").first();
            String price = priceElement != null ? priceElement.attr("data-price-value") + " " + priceElement.attr("data-price-currency") : "Cena nie je k dispozícii";
            String dataEan = productDetailTitle != null ? productDetailTitle.attr("data-ean") : "";
            String dataMatch = productDetailTitle != null ? productDetailTitle.attr("data-match") : "";
            Element imageElement = productPage.select("#product-gallery-main-zoom img").first();
            String imageUrl = imageElement != null ? imageElement.attr("src") : "";
            String centralStock = productPage.select(".product-availability-state[data-qa='product-availability-state']").text();

            // find quantity in skalicastock
            try {
                String ajaxUrl = productUrl + "?do=reservation&id=" + productId;

                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(ajaxUrl))
                        .header("User-Agent", "Mozilla/5.0")
                        .header("X-Requested-With", "XMLHttpRequest")
                        .header("Accept", "text/html")
                        .GET()
                        .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() != 200) {
                    return null;
                }

                JSONObject jsonResponse = new JSONObject(response.body());
                String htmlSnippet = jsonResponse.getJSONObject("snippets").getString("snippet--modal");
                Document ajaxDoc = Jsoup.parse(htmlSnippet);

                Elements storeDivs = ajaxDoc.select("div.col-6");
                for (Element store : storeDivs) {
                    String dataType = store.attr("data-type");
                    if (dataType.contains("430645")) {
                        Element quantitySpan = store.selectFirst("span.pickup-store-quantity");
                        if (quantitySpan != null) {
                            String stav = quantitySpan.text();
                            String freeAmountStr = quantitySpan.attr("data-freeamount");
                            String unpackedAmount = quantitySpan.attr("data-unpackedamount");

                            int free = 0;
                            int unpacked = 0;
                            try {
                                free = Integer.parseInt(freeAmountStr);
                            } catch (NumberFormatException e) {
                                System.out.println("freeAmount nie je číslo: " + freeAmountStr);
                            }
                            try {
                                unpacked = Integer.parseInt(unpackedAmount);
                            } catch (NumberFormatException e) {
                                System.out.println("unpackedAmount nie je číslo: " + unpackedAmount);
                            }

                            int available = free - unpacked;
                            String freeAmount = String.valueOf(available);

                            return new ProductInfo(
                                    productTitle,
                                    stav,
                                    freeAmount,
                                    unpackedAmount,
                                    imageUrl,
                                    price,
                                    dataEan,
                                    dataMatch,
                                    centralStock,
                                    productUrl
                            );
                        }
                    }
                }
            }catch (Exception e) {
                System.out.println("Chyba při AJAX požadavku: " + e.getMessage());
            }
            return new ProductInfo(
                    productTitle,
                    "Nedostupné",
                    "0",
                    "0",
                    imageUrl,
                    price,
                    dataEan,
                    dataMatch,
                    centralStock,
                    productUrl

            );

//            return new ProductInfo(productTitle, "Vypredané", "0", "0");
        } catch (Exception e) {
            e.printStackTrace();
            return new ProductInfo(
                    "Chyba",
                    "Chyba při načítání dat",
                    "0",
                    "0",
                    "",
                    "",
                    "",
                    "",
                    "",
                    ""
            );
        }
    }


    public List<ProductVariant> getProductVariants(String productUrl) {
        List<ProductVariant> variants = new ArrayList<>();
        try {
            Document productPage = Jsoup.connect(productUrl)
                    .userAgent("Mozilla/5.0")
                    .timeout(10000)
                    .get();

            Elements variantDivs = productPage.select("div.offer-box");
//            System.out.println("Nájdených " + variantDivs.size() + " variant divov");

            for (Element div : variantDivs) {
                String name = div.select(".offer-box-title").text();
                String imageUrl = div.select(".offer-box-image img").attr("src");
                String link = "https://www.nay.sk" + div.select("a").attr("href");
                String price = div.select(".offer-box-price-actual").text();

                // Načítanie počtov pre variant
                ProductInfo productInfo = getObjetAvailabilityForSkalica(link);

                variants.add(new ProductVariant(name, imageUrl, link, price, productInfo.getFreeAmount(), productInfo.getUnpackedAmount(), productInfo.getCentralStock()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return variants;
    }


    public String getLinkOfProductFromInput(String link) {
        String productUrl;
        if (link.matches("\\d+") || link.matches("[A-Za-z0-9]+")) { // Zkontroluje, zda je vstup číselný (EAN) nebo alfanumerický (matchId)
            productUrl = getLinkOfProductFromEanOrMatchId(link);
        } else if (link.startsWith("https://www.nay.sk/")) {
            productUrl = link; // Pokud je to již celá URL adresa, vrátí ji tak, jak je
        } else {
            System.out.println("Neplatný vstup");
            productUrl = "Neplatný vstup"; // Ošetření neplatného vstupu
        }
        return productUrl;
    }

    private String getLinkOfProductFromEanOrMatchId(String identifier) {
        try {
            // Načtení stránky pomocí Jsoup
            String searchUrl = "https://www.nay.sk/vyhladavanie?q=" + identifier;
            Document searchPage = Jsoup.connect(searchUrl)
                    .userAgent("Mozilla/5.0")
                    .timeout(5000)
                    .get();

            System.out.println( "getLinkOfProductFromEanOrMatchId  : " +  searchUrl);

            // Vyhledání prvního odkazu s atributem href
            Element linkElement = searchPage.selectFirst("a[data-gtm-event=click_item_category_listing][data-lb-name]");
            if (linkElement != null) {
                String href = linkElement.attr("href");
//                System.out.println("Odkaz nalezen: " + href);
                return "https://www.nay.sk" + href;
            } else {
//                System.out.println("Odkaz nebyl nalezen. : " + identifier );
                return "Odkaz nebyl nalezen; "+ identifier ;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Chyba při načítání stránky: " + e.getMessage();
        }
    }


}


