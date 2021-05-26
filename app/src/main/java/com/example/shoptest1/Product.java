package com.example.shoptest1;

import java.net.URL;

public class Product {
    int availability;              // количество товара в наличие
    int categoryId;                // id категории товаров
    String categoryName;           // название категории товаров
    int categorySectionId;         // id раздела категорий товаров
    String categorySectionName;    // имя раздела категорий товаров
    String description;            // описание товара
    int id;                        // id товара
    int ownerId;                   // id группы-продавца
    String priceAmount;               // цена на товар
    int priceCurrencyId;           // id валюты
    String priceCurrencyName;      // название валюты
    String priceCurrencyTitle;     // название валюты (не международное)
    String priceText;              // стоимость, в виде текста
    String title;                  // название товара
    String date;                   // дата
    String thumbPhoto;             // ссылка на фотографию товара
    int cartQuantity;
    String dimensions_width;
    String dimensions_height;
    String dimensions_length;
    String weight;
    String sku;


    public Product(int availability, int categoryId, String categoryName, int categorySectionId,
                    String categorySectionName, String description, int id, int ownerId,
                   String priceAmount, int priceCurrencyId, String priceCurrencyName, String priceCurrencyTitle,
                    String priceText, String title, String date, String thumbPhoto, int cartQuantity, String dimensions_width,
                   String dimensions_height, String dimensions_length, String weight, String sku) {
        this.availability = availability;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.categorySectionId = categorySectionId;
        this.categorySectionName = categorySectionName;
        this.description = description;
        this.id = id;
        this.ownerId = ownerId;
        this.priceAmount = priceAmount;
        this.priceCurrencyId = priceCurrencyId;
        this.priceCurrencyName = priceCurrencyName;
        this.priceCurrencyTitle = priceCurrencyTitle;
        this.priceText = priceText;
        this.title = title;
        this.date = date;
        this.thumbPhoto = thumbPhoto;
        this.cartQuantity = cartQuantity;
        this.dimensions_width = dimensions_width;
        this.dimensions_height = dimensions_height;
        this.dimensions_length = dimensions_length;
        this.weight = weight;
        this.sku = sku;
    }

    public Product() {
        this.availability = -1;
        this.categoryId = -1;
        this.categoryName = "";
        this.categorySectionId = -1;
        this.categorySectionName = "";
        this.description = "";
        this.id = -1;
        this.ownerId = 0;
        this.priceAmount = "-1";
        this.priceCurrencyId = -1;
        this.priceCurrencyName = "";
        this.priceCurrencyTitle = "";
        this.priceText = "";
        this.title = "";
        this.date = "";
        this.thumbPhoto = "";
        this.cartQuantity = -1;
        this.dimensions_width = "";
        this.dimensions_height = "";
        this.dimensions_length = "";
        this.weight = "";
        this.sku = "";
    }

    public static void toProducts(Product product, String item) {
        product.availability = Integer.parseInt(new StringBuilder(item).substring(item.indexOf("availability") + 14,
                item.indexOf(",", item.indexOf("availability"))));
        product.categoryId = Integer.parseInt(new StringBuilder(item).substring(item.indexOf("id", item.indexOf("category")) + 4,
                item.indexOf(",", item.indexOf("id", item.indexOf("category")))));
        product.categoryName = new StringBuilder(item).substring(item.indexOf("name", item.indexOf("category")) + 7,
                item.indexOf("\",", item.indexOf("name", item.indexOf("category"))));
        product.categorySectionId = Integer.parseInt(new StringBuilder(item).substring(item.indexOf("id", item.indexOf("section")) + 4,
                item.indexOf(",", item.indexOf("id", item.indexOf("section")))));
        product.categorySectionName = new StringBuilder(item).substring(item.indexOf("name", item.indexOf("section")) + 7,
                item.indexOf("\"}", item.indexOf("name", item.indexOf("section"))));
        product.description = new StringBuilder(item).substring(item.indexOf("description") + 14,
                item.indexOf("\",", item.indexOf("description")));
        product.id = Integer.parseInt(new StringBuilder(item).substring(item.indexOf("id", item.indexOf("description")) + 4,
                item.indexOf(",", item.indexOf("id", item.indexOf("description")))));
        product.ownerId = Integer.parseInt(new StringBuilder(item).substring(item.indexOf("owner_id") + 11,
                item.indexOf(",", item.indexOf("owner_id"))));
        product.priceAmount = new StringBuilder(item).substring(item.indexOf("amount", item.indexOf("price")) + 9,
                item.indexOf("\",", item.indexOf("amount", item.indexOf("price"))));
        product.priceCurrencyId = Integer.parseInt(new StringBuilder(item).substring(item.indexOf("id", item.indexOf("currency")) + 4,
                item.indexOf(",", item.indexOf("id", item.indexOf("currency")))));
        product.priceCurrencyName = new StringBuilder(item).substring(item.indexOf("name", item.indexOf("currency")) + 7,
                item.indexOf("\",", item.indexOf("name", item.indexOf("currency"))));
        product.priceCurrencyTitle = new StringBuilder(item).substring(item.indexOf("title", item.indexOf("currency")) + 8,
                item.indexOf("\"}", item.indexOf("title", item.indexOf("currency"))));
        product.priceText = new StringBuilder(item).substring(item.indexOf("text") + 7,
                item.indexOf("\"", item.indexOf("text") + 7));
        product.title = new StringBuilder(item).substring(item.indexOf("title", item.indexOf("text")) + 8,
                item.indexOf("\",", item.indexOf("title", item.indexOf("text"))));
        product.date = new StringBuilder(item).substring(item.indexOf("date") + 6,
                item.indexOf(",", item.indexOf("date")));
        product.thumbPhoto = new StringBuilder(item).substring(item.indexOf("thumb_photo") + 14,
                item.indexOf("\",", item.indexOf("thumb_photo")));
        product.cartQuantity = Integer.parseInt(new StringBuilder(item).substring(item.indexOf("cart_quantity") + 15,
                item.indexOf("cart_quantity") + 16));
    }
}
