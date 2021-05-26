package com.example.shoptest1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

public class EditProduct extends AppCompatActivity {

    EditText productNameChange, descriptionChange, priceChange, dimension_widthChange,
            dimension_heightChange, dimension_lengthChange,
            weightChange, skuChange;
    String accessToken = new String(), ownerId = new String();
    Button doneChange;
    Button choosePhoto;
    String category = new String();
    String productId = new String();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        accessToken = getIntent().getStringExtra("access_token");
        ownerId = getIntent().getStringExtra("group_id");
        Spinner spinner = findViewById(R.id.spinnerCategoryChange);
        doneChange = findViewById(R.id.doneChange);
        productNameChange = findViewById(R.id.editProductNameChange);
        descriptionChange = findViewById(R.id.editDescriptionChange);
        priceChange = findViewById(R.id.editPriceChange);
        dimension_widthChange = findViewById(R.id.edit_dimension_widthChange);
        dimension_heightChange = findViewById(R.id.edit_dimension_heightChange);
        dimension_lengthChange = findViewById(R.id.edit_dimension_lengthChange);
        weightChange = findViewById(R.id.editWeightChange);
        skuChange = findViewById(R.id.editSkuChange);
        productId = getIntent().getStringExtra("item_id");

        productNameChange.setText(getIntent().getStringExtra("productName"));
        descriptionChange.setText(getIntent().getStringExtra("description"));
        priceChange.setText(getIntent().getStringExtra("price"));
        dimension_widthChange.setText(getIntent().getStringExtra("dimension_width"));
        dimension_heightChange.setText(getIntent().getStringExtra("dimension_height"));
        dimension_lengthChange.setText(getIntent().getStringExtra("dimension_length"));
        weightChange.setText(getIntent().getStringExtra("weight"));
        skuChange.setText(getIntent().getStringExtra("sku"));

        doneChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean canIMakeUrl = true;
                if (productNameChange.getText().length() < 4) {
                    Toast.makeText(getApplicationContext(), "Слишком короткое название товара", Toast.LENGTH_LONG).show();
                    canIMakeUrl = false;
                }
                if (descriptionChange.getText().length() < 10) {
                    Toast.makeText(getApplicationContext(), "Слишком короткое описание товара", Toast.LENGTH_LONG).show();
                    canIMakeUrl = false;
                }
                if (canIMakeUrl == true) {
                    getCategory(spinner);
                    String url = "https://api.vk.com/method/market.edit?v=5.131&access_token=" +
                            accessToken + "&owner_id=-" + ownerId + "&item_id=" + productId +
                            "&name=" + productNameChange.getText() + "&description=" +
                            descriptionChange.getText() + "&category_id=" + category +
                            "&price=" + priceChange.getText() + "&old_price=" + priceChange.getText() +
                            "&dimension_width=" +
                            dimension_widthChange.getText() + "&dimension_height=" +
                            dimension_heightChange.getText() + "&dimension_length=" +
                            dimension_lengthChange.getText() + "&weight=" + weightChange.getText();   // sku only from 5.131
                    //Log.d("URRRRRRRRRRRRRRL", url);
                    GetCall getCall = new GetCall();
                    try {
                        String response = getCall.execute(url).get();           // добавить проверку на ошибку запроса (тч на количество символов в названии/описании)
                        //Log.d("RESP875Onse", response);
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Intent intent = new Intent(EditProduct.this, GroupMarket.class); // toast короткий
                startActivity(intent);
            }
        });
    }

    private void getCategory(Spinner spinner) {
        switch (spinner.getSelectedItem().toString()) {
            case ("Женская одежда"):
                category = "1";
                break;
            case ("Мужская одежда"):
                category = "2";
                break;
            case ("Детская одежда"):
                category = "3";
                break;
            case ("Обувь и сумки"):
                category = "4";
                break;
            case ("Аксессуары и украшения"):
                category = "5";
                break;
            case ("Автокресла"):
                category = "100";
                break;
            case ("Детские коляски"):
                category = "101";
                break;
            case ("Детская комната"):
                category = "102";
                break;
            case ("Игрушки"):
                category = "103";
                break;
            case ("Мамам и малышам"):
                category = "104";
                break;
            case ("Обучение и творчество"):
                category = "105";
                break;
            case ("Школьникам"):
                category = "106";
                break;
            case ("Телефоны и аксессуары"):
                category = "200";
                break;
            case ("Фото- и видеокамеры"):
                category = "201";
                break;
            case ("Аудио- и видеотехника"):
                category = "202";
                break;
            case ("Портативная техника"):
                category = "203";
                break;
            case ("Игровые приставки и игры"):
                category = "204";
                break;
            case ("Техника для автомобилей"):
                category = "205";
                break;
            case ("Оптические приборы"):
                category = "206";
                break;
            case ("Компьютеры"):
                category = "300";
                break;
            case ("Ноутбуки, нетбуки"):
                category = "301";
                break;
            case ("Комплектующие и аксессуары"):
                category = "302";
                break;
            case ("Периферийные устройства"):
                category = "303";
                break;
            case ("Сетевое оборудование"):
                category = "304";
                break;
            case ("Оргтехника и расходники"):
                category = "305";
                break;
            case  ("Фильмы, музыка, программы"):
                category = "306";
                break;
            case ("Автомобили"):
                category = "400";
                break;
            case ("Мотоциклы и мототехника"):
                category = "401";
                break;
            case ("Грузовики и спецтехника"):
                category = "402";
                break;
            case ("Водный транспорт"):
                category = "403";
                break;
            case ("Запчасти и аксессуары"):
                category = "404";
                break;
            case ("Квартиры"):
                category = "500";
                break;
            case ("Комнаты"):
                category = "501";
                break;
            case ("Дома, дачи, коттеджи"):
                category = "502";
                break;
            case ("Земельные участки"):
                category = "503";
                break;
            case ("Гаражи и машиноместа"):
                category = "504";
                break;
            case ("Коммерческая недвижимость"):
                category = "505";
                break;
            case ("Недвижимость за рубежом"):
                category = "506";
                break;
            case ("Бытовая техника"):
                category = "600";
                break;
            case ("Мебель и интерьер"):
                category = "601";
                break;
            case ("Кухонные принадлежности"):
                category = "602";
                break;
            case ("Текстиль"):
                category = "603";
                break;
            case ("Хозяйственные товары"):
                category = "604";
                break;
            case ("Ремонт и строительство"):
                category = "605";
                break;
            case ("Дача, сад и огород"):
                category = "606";
                break;
            case ("Декоративная косметика"):
                category = "700";
                break;
            case ("Парфюмерия"):
                category = "701";
                break;
            case ("Уход за лицом и телом"):
                category = "702";
                break;
            case ("Приборы и аксессуары"):
                category = "703";
                break;
            case ("Оптика"):
                category = "704";
                break;
            case ("Активный отдых"):
                category = "800";
                break;
            case ("Туризм"):
                category = "801";
                break;
            case ("Охота и рыбалка"):
                category = "802";
                break;
            case ("Тренажеры и фитнес"):
                category = "803";
                break;
            case ("Игры"):
                category = "804";
                break;
            case ("Билеты и путешествия"):
                category = "900";
                break;
            case ("Книги и журналы"):
                category = "901";
                break;
            case ("Коллекционирование"):
                category = "902";
                break;
            case ("Музыкальные инструменты"):
                category = "903";
                break;
            case ("Настольные игры"):
                category = "904";
                break;
            case ("Подарочные наборы и сертификаты"):
                category = "905";
                break;
            case ("Сувениры и цветы"):
                category = "906";
                break;
            case ("Рукоделие"):
                category = "907";
                break;
            case ("Собаки"):
                category = "1000";
                break;
            case ("Кошки"):
                category = "1001";
                break;
            case ("Грызуны"):
                category = "1002";
                break;
            case ("Птицы"):
                category = "1003";
                break;
            case ("Рыбы"):
                category = "1004";
                break;
            case ("Другие животные"):
                category = "1005";
                break;
            case ("Корма и аксессуары"):
                category = "1006";
                break;
            case ("Бакалея"):
                category = "1100";
                break;
            case ("Биопродукты"):
                category = "1101";
                break;
            case ("Детское питание"):
                category = "1102";
                break;
            case ("Еда на заказ"):
                category = "1103";
                break;
            case ("Напитки"):
                category = "1104";
                break;
            case ("Фото- и видеосъёмка"):
                category = "1200";
                break;
            case ("Удалённая работа"):
                category = "1201";
                break;
            case ("Организация мероприятий"):
                category = "1202";
                break;
            case ("Красота и здоровье"):
                category = "1203";
                break;
            case ("Установка и ремонт техники"):
                category = "1204";
                break;
            case ("Уборка и помощь по хозяйству"):
                category = "1205";
                break;
            case ("Курьеры и грузоперевозки"):
                category = "1206";
                break;
            case ("Обучение и развитие"):
                category = "1207";
                break;
        }
    }

}