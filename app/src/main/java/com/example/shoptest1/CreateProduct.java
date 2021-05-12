package com.example.shoptest1;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CreateProduct extends AppCompatActivity {

    static final int GALLERY_REQUEST = 1;
    public static String mainPhotoStr;

    EditText productName, description, price, dimension_width, dimension_height, dimension_length,
            weight, sku;
    String accessToken, ownerId, category;
    Button done, choosePhoto;
    String uploadUrl;
    Uri mainPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_product);

        accessToken = getIntent().getStringExtra("access_token");
        ownerId = getIntent().getStringExtra("group_id");
        Log.d("GROUPID1", ownerId);
        Spinner spinner = findViewById(R.id.spinnerCategory);
        done = findViewById(R.id.done);
        choosePhoto = findViewById(R.id.choosePhoto);
        productName = findViewById(R.id.editProductName);
        description = findViewById(R.id.editDescription);
        price = findViewById(R.id.editPrice);
        dimension_width = findViewById(R.id.edit_dimension_width);
        dimension_height = findViewById(R.id.edit_dimension_height);
        dimension_length = findViewById(R.id.edit_dimension_length);
        weight = findViewById(R.id.editWeight);
        sku = findViewById(R.id.editSku);
        String selected = spinner.getSelectedItem().toString();
        Log.d("SPINNER", selected);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProduct(spinner);
            }
        });
        choosePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case GALLERY_REQUEST:
                if (resultCode == RESULT_OK) {
                    mainPhoto = data.getData();
                    mainPhotoStr = mainPhoto.toString();
                }
                break;
        }
    }

    private void getUrlPhoto() {
        String url = "https://api.vk.com/method/photos.getMarketUploadServer?v=5.52&access_token=" +
                accessToken + "&group_id=" + ownerId + "&main_photo=1";
        GetCall getCall = new GetCall();
        try {
            uploadUrl = getCall.execute(url).get();
            PutPhoto putPhoto = new PutPhoto();
            String photo = putPhoto.execute().get();
            Log.d("RESPONSE", photo);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public class PutPhoto extends AsyncTask<String, Integer, String> {
        OkHttpClient client = new OkHttpClient();
        @Override
        protected String doInBackground(String... urls) {
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("dfahb", "sbdg", RequestBody.create(
                            MediaType.parse("image/jpeg"), new File(mainPhotoStr)))
                    .build();

            Request request = new Request.Builder()
                    .url(uploadUrl)
                    .post(requestBody)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                String resp = response.body().string();


                return resp;

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private void getCategory(Spinner spinner) {
        switch (spinner.getSelectedItem().toString()) {
            case ("Женская одежда"):
                category = "1";
            case ("Мужская одежда"):
                category = "2";
            case ("Детская одежда"):
                category = "3";
            case ("Обувь и сумки"):
                category = "4";
            case ("Аксессуары и украшения"):
                category = "5";
            case ("Автокресла"):
                category = "100";
            case ("Детские коляски"):
                category = "101";
            case ("Детская комната"):
                category = "102";
            case ("Игрушки"):
                category = "103";
            case ("Мамам и малышам"):
                category = "104";
            case ("Обучение и творчество"):
                category = "105";
            case ("Школьникам"):
                category = "106";
            case ("Телефоны и аксессуары"):
                category = "200";
            case ("Фото- и видеокамеры"):
                category = "201";
            case ("Аудио- и видеотехника"):
                category = "202";
            case ("Портативная техника"):
                category = "203";
            case ("Игровые приставки и игры"):
                category = "204";
            case ("Техника для автомобилей"):
                category = "205";
            case ("Оптические приборы"):
                category = "206";
            case ("Компьютеры"):
                category = "300";
            case ("Ноутбуки, нетбуки"):
                category = "301";
            case ("Комплектующие и аксессуары"):
                category = "302";
            case ("Периферийные устройства"):
                category = "303";
            case ("Сетевое оборудование"):
                category = "304";
            case ("Оргтехника и расходники"):
                category = "305";
            case ("Фильмы, музыка, программы"):
                category = "306";
            case ("Автомобили"):
                category = "400";
            case ("Мотоциклы и мототехника"):
                category = "401";
            case ("Грузовики и спецтехника"):
                category = "402";
            case ("Водный транспорт"):
                category = "403";
            case ("Запчасти и аксессуары"):
                category = "404";
            case ("Квартиры"):
                category = "500";
            case ("Комнаты"):
                category = "501";
            case ("Дома, дачи, коттеджи"):
                category = "502";
            case ("Земельные участки"):
                category = "503";
            case ("Гаражи и машиноместа"):
                category = "504";
            case ("Коммерческая недвижимость"):
                category = "505";
            case ("Недвижимость за рубежом"):
                category = "506";
            case ("Бытовая техника"):
                category = "600";
            case ("Мебель и интерьер"):
                category = "601";
            case ("Кухонные принадлежности"):
                category = "602";
            case ("Текстиль"):
                category = "603";
            case ("Хозяйственные товары"):
                category = "604";
            case ("Ремонт и строительство"):
                category = "605";
            case ("Дача, сад и огород"):
                category = "606";
            case ("Декоративная косметика"):
                category = "700";
            case ("Парфюмерия"):
                category = "701";
            case ("Уход за лицом и телом"):
                category = "702";
            case ("Приборы и аксессуары"):
                category = "703";
            case ("Оптика"):
                category = "704";
            case ("Активный отдых"):
                category = "800";
            case ("Туризм"):
                category = "801";
            case ("Охота и рыбалка"):
                category = "802";
            case ("Тренажеры и фитнес"):
                category = "803";
            case ("Игры"):
                category = "804";
            case ("Билеты и путешествия"):
                category = "900";
            case ("Книги и журналы"):
                category = "901";
            case ("Коллекционирование"):
                category = "902";
            case ("Музыкальные инструменты"):
                category = "903";
            case ("Настольные игры"):
                category = "904";
            case ("Подарочные наборы и сертификаты"):
                category = "905";
            case ("Сувениры и цветы"):
                category = "906";
            case ("Рукоделие"):
                category = "907";
            case ("Собаки"):
                category = "1000";
            case ("Кошки"):
                category = "1001";
            case ("Грызуны"):
                category = "1002";
            case ("Птицы"):
                category = "1003";
            case ("Рыбы"):
                category = "1004";
            case ("Другие животные"):
                category = "1005";
            case ("Корма и аксессуары"):
                category = "1006";
            case ("Бакалея"):
                category = "1100";
            case ("Биопродукты"):
                category = "1101";
            case ("Детское питание"):
                category = "1102";
            case ("Еда на заказ"):
                category = "1103";
            case ("Напитки"):
                category = "1104";
            case ("Фото- и видеосъёмка"):
                category = "1200";
            case ("Удалённая работа"):
                category = "1201";
            case ("Организация мероприятий"):
                category = "1202";
            case ("Красота и здоровье"):
                category = "1203";
            case ("Установка и ремонт техники"):
                category = "1204";
            case ("Уборка и помощь по хозяйству"):
                category = "1205";
            case ("Курьеры и грузоперевозки"):
                category = "1206";
            case ("Обучение и развитие"):
                category = "1207";
        }
    }

    private void addProduct(Spinner spinner) {
        getCategory(spinner);
        String url = "https://api.vk.com/method/market.add?v=5.52&access_token=" + accessToken +
                "&owner_id=-" + ownerId + "&name=" + productName.getText().toString() + "&description=" +
                description.getText().toString() + "&category_id=" + category.toString() + "&price=" + price.getText().toString();
        //String url = "https://api.vk.com/method/market.add?v=5.52&access_token=" + accessToken +
        //        "&ownerId=-" + ownerId + "&name=" + "aaaaaaaa" + "&description=" +
        //        "bbbbbbbbbbbbbbbb" + "&category_id=" + "100" + "&price=" + "1000";
        GetCall getCall = new GetCall();
        String newProductId = new String();
        try {
            newProductId = getCall.execute(url).get();
            Log.d("URL", url);
            Log.d("NEWPRODUCTID", newProductId);
            Intent i = new Intent(CreateProduct.this, GroupMarket.class);
            setResult(1, i);
            finish();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}