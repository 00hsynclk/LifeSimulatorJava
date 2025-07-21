package main.lifesimulator;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.text.DecimalFormat;
import java.util.Random;

public class LifeSimulator extends Application {

    // Karakter bilgileri
    private String characterName;
    private int age = 18;
    private double money = 1000.0;
    private int prestige = 0;
    private String job = "İşsiz";
    private int educationLevel = 0; // 0: Lise, 1: Üniversite, 2: Yüksek Lisans
    private boolean hasFamily = false;
    private boolean ownsHouse = false;
    private boolean ownsCar = false;
    private int day = 1; // Gün sayacı
    private int childrenCount = 0; // Çocuk sayısı

    // Maaşlar ve giderler
    private double jobSalary = 0.0;
    private double houseExpense = 200.0; // Ev masrafları varsayılan
    private double familyExpense = 0.0;

    // Yatırım araçları
    private double dollarInvestment = 0.0;
    private double goldInvestment = 0.0;
    private double stockInvestment = 0.0;
    private double dollarRate = 23.05;
    private double goldRate = 1200.55;
    private double stockRate = 200.75;

    // Prestij artırma maliyeti
    private double prestigeUpgradeCost = 100.0; // Prestij yükseltme için gereken para

    // Eğitim Maliyetleri
    private final int educationCostUniversity = 500;
    private final int educationCostMasters = 1000;

    // Formatlama için
    private DecimalFormat df = new DecimalFormat("0.00");

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Hayat Simülatörü");

        // Arayüz bileşenleri
        Label lblName = new Label("Karakter İsmi:");
        TextField txtName = new TextField();
        Button btnStart = new Button("Başla");

        Label lblInfo = new Label("Durum Bilgileri:");
        TextArea txtInfo = new TextArea();
        txtInfo.setEditable(false);
        txtInfo.setPrefHeight(400);  // Durum bilgisinin yüksekliğini arttırdık

        Button btnFindJob = new Button("İş Bul");
        Button btnUpgradeJob = new Button("İşi Yükselt");
        Button btnPrestigeUpgrade = new Button("Prestij Yükselt");
        Button btnStartFamily = new Button("Aile Kur");
        Button btnBuyHouse = new Button("Ev Satın Al");
        Button btnBuyCar = new Button("Araba Satın Al");
        Button btnInvestDollar = new Button("Dolar Yatırımı Yap");
        Button btnInvestGold = new Button("Altın Yatırımı Yap");
        Button btnInvestStock = new Button("Borsa Yatırımı Yap");
        Button btnNextDay = new Button("Bir Gün İlerle");
        Button btnHelp = new Button("Nasıl Oynanır?");
        Button btnEducation = new Button("Eğitim Al");

        // Yatırım tutarı için input alanları
        TextField txtDollarAmount = new TextField();
        txtDollarAmount.setPromptText("Dolar Yatırımı Miktarı");
        TextField txtGoldAmount = new TextField();
        txtGoldAmount.setPromptText("Altın Yatırımı Miktarı");
        TextField txtStockAmount = new TextField();
        txtStockAmount.setPromptText("Borsa Yatırımı Miktarı");

        // Yatırım bozdurma butonları
        Button btnSellDollar = new Button("Dolar Yatırımını Bozdur");
        Button btnSellGold = new Button("Altın Yatırımını Bozdur");
        Button btnSellStock = new Button("Borsa Yatırımını Bozdur");

        // Başlangıç butonuna tıklama işlemi
        btnStart.setOnAction(e -> {
            characterName = txtName.getText().trim();
            if (characterName.isEmpty()) {
                showAlert("Hata", "Lütfen karakter ismini giriniz.");
            } else {
                updateInfo(txtInfo);
            }
        });

        // Eğitim butonu işlemi
        btnEducation.setOnAction(e -> {
            if (educationLevel == 0 && money >= educationCostUniversity) {
                money -= educationCostUniversity;
                educationLevel = 1; // Üniversite seviyesi
                showAlert("Başarılı", "Üniversiteye başladınız! Eğitim seviyesi: Üniversite.");
            } else if (educationLevel == 1 && money >= educationCostMasters) {
                money -= educationCostMasters;
                educationLevel = 2; // Yüksek lisans seviyesi
                showAlert("Başarılı", "Yüksek lisansa başladınız! Eğitim seviyesi: Yüksek Lisans.");
            } else if (educationLevel == 2) {
                showAlert("Bilgi", "Zaten en yüksek eğitim seviyesindesiniz.");
            } else {
                showAlert("Hata", "Eğitim için yeterli paranız yok.");
            }
            updateInfo(txtInfo);
        });

        // İş bulma işlemi
        btnFindJob.setOnAction(e -> {
            if (educationLevel == 0) {
                showAlert("Hata", "İş bulmak için en azından üniversite seviyesinde eğitiminiz olmalıdır.");
            } else if (job.equals("İşsiz")) {
                if (educationLevel == 1) {
                    job = "Orta Düzey Çalışan";
                    jobSalary = 300.0;
                    prestige += 20;
                } else if (educationLevel == 2) {
                    job = "Kıdemli Yönetici";
                    jobSalary = 500.0;
                    prestige += 50;
                }
                showAlert("Başarılı", job + " olarak işe başladınız. Maaşınız: " + jobSalary + " TL");
                updateInfo(txtInfo);
            } else {
                showAlert("Bilgi", "Zaten bir işiniz var.");
            }
        });

        // Dolar yatırım işlemi
        btnInvestDollar.setOnAction(e -> {
            try {
                double investAmount = Double.parseDouble(txtDollarAmount.getText().trim());

                if (investAmount < 100) {
                    showAlert("Hata", "Yatırım tutarı 100 TL'den az olamaz.");
                } else if (investAmount > 10000) {
                    showAlert("Hata", "Yatırım tutarı 10.000 TL'den fazla olamaz.");
                } else if (investAmount > money) {
                    showAlert("Hata", "Mevcut bakiyeniz yetersiz. Yatırım yapabilmek için " + (investAmount - money) + " TL'ye ihtiyacınız var.");
                } else {
                    dollarInvestment += investAmount;
                    money -= investAmount;
                    showAlert("Başarılı", "Dolar yatırımı başarılı. Yeni bakiye: " + df.format(money) + " TL");
                    updateInfo(txtInfo);
                }
            } catch (NumberFormatException ex) {
                showAlert("Hata", "Geçerli bir rakam giriniz.");
            }
        });

        // Altın yatırım işlemi
        btnInvestGold.setOnAction(e -> {
            try {
                double investAmount = Double.parseDouble(txtGoldAmount.getText().trim());

                if (investAmount < 100) {
                    showAlert("Hata", "Yatırım tutarı 100 TL'den az olamaz.");
                } else if (investAmount > 10000) {
                    showAlert("Hata", "Yatırım tutarı 10.000 TL'den fazla olamaz.");
                } else if (investAmount > money) {
                    showAlert("Hata", "Mevcut bakiyeniz yetersiz. Yatırım yapabilmek için " + (investAmount - money) + " TL'ye ihtiyacınız var.");
                } else {
                    goldInvestment += investAmount;
                    money -= investAmount;
                    showAlert("Başarılı", "Altın yatırımı başarılı. Yeni bakiye: " + df.format(money) + " TL");
                    updateInfo(txtInfo);
                }
            } catch (NumberFormatException ex) {
                showAlert("Hata", "Geçerli bir rakam giriniz.");
            }
        });

        // Borsa yatırım işlemi
        btnInvestStock.setOnAction(e -> {
            try {
                double investAmount = Double.parseDouble(txtStockAmount.getText().trim());

                if (investAmount < 100) {
                    showAlert("Hata", "Yatırım tutarı 100 TL'den az olamaz.");
                } else if (investAmount > 10000) {
                    showAlert("Hata", "Yatırım tutarı 10.000 TL'den fazla olamaz.");
                } else if (investAmount > money) {
                    showAlert("Hata", "Mevcut bakiyeniz yetersiz. Yatırım yapabilmek için " + (investAmount - money) + " TL'ye ihtiyacınız var.");
                } else {
                    stockInvestment += investAmount;
                    money -= investAmount;
                    showAlert("Başarılı", "Borsa yatırımı başarılı. Yeni bakiye: " + df.format(money) + " TL");
                    updateInfo(txtInfo);
                }
            } catch (NumberFormatException ex) {
                showAlert("Hata", "Geçerli bir rakam giriniz.");
            }
        });

        // İş yükseltme işlemi
        btnUpgradeJob.setOnAction(e -> {
            if (!job.equals("İşsiz")) {
                // İş seviyeleri ve gerekli prestij puanı
                int requiredPrestige = 0;
                if (job.equals("Orta Düzey Çalışan")) {
                    requiredPrestige = 100;  // İlk iş yükseltme için gereken prestij
                } else if (job.equals("Kıdemli Yönetici")) {
                    requiredPrestige = 200;  // İkinci iş yükseltme için gereken prestij
                } else if (job.equals("Yönetici")) {
                    requiredPrestige = 350;  // Üçüncü seviye iş için gereken prestij
                } else {
                    showAlert("Hata", "Artık işinizi yükseltemezsiniz.");
                    return;
                }

                if (prestige >= requiredPrestige) {
                    // İş yükseltme işlemi
                    if (job.equals("Orta Düzey Çalışan")) {
                        job = "Kıdemli Yönetici";
                        jobSalary += 200;
                    } else if (job.equals("Kıdemli Yönetici")) {
                        job = "Yönetici";
                        jobSalary += 300;
                    }

                    prestige -= requiredPrestige;  // Yükseltme için gereken prestij düşürülür
                    showAlert("Başarılı", "İşiniz yükseltildi! Yeni maaşınız: " + jobSalary + " TL");
                    updateInfo(txtInfo);
                } else {
                    showAlert("Hata", "İşi yükseltmek için " + (requiredPrestige - prestige) + " prestije daha ihtiyacınız var.");
                }
            } else {
                showAlert("Hata", "Öncelikle bir işiniz olmalı.");
            }
        });



        // Prestij artırma işlemi (para ile)
        btnPrestigeUpgrade.setOnAction(e -> {
            if (money >= prestigeUpgradeCost) {
                money -= prestigeUpgradeCost; // Para harcanır
                prestige += 10; // Prestij artışı
                prestigeUpgradeCost += 50; // Sonraki prestij artışı maliyeti yükselir
                showAlert("Başarılı", "Prestijiniz arttı! Şu anki prestij: " + prestige);
                updateInfo(txtInfo);
            } else {
                showAlert("Hata", "Prestij artırmak için " + df.format(prestigeUpgradeCost - money) + " TL'ye ihtiyacınız var.");
            }
        });

        // Yatırım bozdurma işlemi
        btnSellDollar.setOnAction(e -> {
            if (dollarInvestment > 0) {
                money += dollarInvestment;
                dollarInvestment = 0;
                showAlert("Başarılı", "Dolar yatırımı bozduruldu. Yeni bakiye: " + df.format(money));
                updateInfo(txtInfo);
            } else {
                showAlert("Hata", "Dolar yatırımınız yok.");
            }
        });

        btnSellGold.setOnAction(e -> {
            if (goldInvestment > 0) {
                money += goldInvestment;
                goldInvestment = 0;
                showAlert("Başarılı", "Altın yatırımı bozduruldu. Yeni bakiye: " + df.format(money));
                updateInfo(txtInfo);
            } else {
                showAlert("Hata", "Altın yatırımınız yok.");
            }
        });

        btnSellStock.setOnAction(e -> {
            if (stockInvestment > 0) {
                money += stockInvestment;
                stockInvestment = 0;
                showAlert("Başarılı", "Borsa yatırımı bozduruldu. Yeni bakiye: " + df.format(money));
                updateInfo(txtInfo);
            } else {
                showAlert("Hata", "Borsa yatırımınız yok.");
            }
        });

        // Aile kurma işlemi
        btnStartFamily.setOnAction(e -> {
            if (!job.equals("İşsiz")) {
                if (!hasFamily) {
                    hasFamily = true;
                    familyExpense = 350.0; // Aile masrafı eklenir
                    showAlert("Başarılı", "Aile kurdunuz! Aylık masraf: " + familyExpense + " TL");
                    updateInfo(txtInfo);
                } else {
                    showAlert("Bilgi", "Zaten bir aileniz var.");
                }
            } else {
                showAlert("Hata", "Öncelikle bir işiniz olmalı.");
            }
        });

        // Ev satın alma işlemi
        btnBuyHouse.setOnAction(e -> {
            if (!ownsHouse && money >= 30000.0) {
                money -= 30000.0;
                ownsHouse = true;
                houseExpense = 0; // Ev masrafı sıfırlanır
                showAlert("Başarılı", "Ev satın aldınız!");
                updateInfo(txtInfo);
            } else if (ownsHouse) {
                showAlert("Bilgi", "Zaten bir eviniz var.");
            } else {
                showAlert("Hata", "Ev almak için 30.000 TL'ye ihtiyacınız var.");
            }
        });

        // Araba satın alma işlemi
        btnBuyCar.setOnAction(e -> {
            if (!ownsCar && money >= 10000.0) {
                money -= 10000.0;
                ownsCar = true;
                showAlert("Başarılı", "Araba satın aldınız!");
                updateInfo(txtInfo);
            } else if (ownsCar) {
                showAlert("Bilgi", "Zaten bir arabanız var.");
            } else {
                showAlert("Hata", "Araba almak için 10.000 TL'ye ihtiyacınız var.");
            }
        });

        // Gün ilerleme işlemi
        btnNextDay.setOnAction(e -> {
            // Yaş arttırma her 180 günde bir
            if (day % 30 == 0) {
                age++;
            }

            // Günlük gelir gider hesaplama
            money += jobSalary - houseExpense - familyExpense;

            if (money < 0) {
                showAlert("Oyun Bitti", "Paranız bitti. Oyun sona erdi.");
                System.exit(0); // Oyunu kapat
            }



            // Yatırım araçlarının değerlerinin değişmesi
            double dollarChange = 1 + (new Random().nextDouble() * 0.04 - 0.01);  // Dolar değerinde % -1 ile %2 arasında değişim
            double goldChange = 1 + (new Random().nextDouble() * 0.06 - 0.02);   // Altın değerinde % -2 ile %3 arasında değişim
            double stockChange = 1 + (new Random().nextDouble() * 0.1 - 0.04);   // Borsa değerinde % -4 ile %5 arasında değişim



            // Yatırım tutarlarının güncellenmesi
            dollarInvestment *= dollarChange;
            goldInvestment *= goldChange;
            stockInvestment *= stockChange;

            // Günün ilerlemesi
            day++;

            // Durumu güncelleme
            updateInfo(txtInfo);
        });

        // Yardım mesajı
        btnHelp.setOnAction(e -> {
            String helpMessage = "1. Oyun başladığında, karakterinize bir isim verin.\n" +
                    "2. İş bulmak için eğitim alın ve iş fırsatlarını keşfedin.\n" +
                    "3. İşinizi yükseltmek ve prestij kazanın.\n" +
                    "4. Yatırım yaparak paranızı değerlendirebilirsiniz.\n" +
                    "5. Ev (30.000 TL ) ve araba (10.000 TL) satın alarak hayatınızı daha lüks ve ev masrafından kaçınır hale getirebilirsiniz.\n" +
                    "6. Aile kurarak sorumluluk alın. (Masrafınız Artar).\n" +
                    "7. Prestij artırma ile işinizi yükseltebilirsiniz. (Her prestij harcaması : 350 TL. Maaşınız artar) .";
            showAlert("Nasıl Oynanır?", helpMessage);
        });

        // Arayüz düzenleme
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(btnFindJob, btnUpgradeJob, btnPrestigeUpgrade, btnStartFamily, btnBuyHouse, btnBuyCar, btnEducation);

        HBox investmentBox = new HBox(10);
        investmentBox.setAlignment(Pos.CENTER);
        investmentBox.getChildren().addAll(txtDollarAmount, btnInvestDollar, txtGoldAmount, btnInvestGold, txtStockAmount, btnInvestStock, btnSellDollar, btnSellGold, btnSellStock);

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(20));
        vbox.getChildren().addAll(lblName, txtName, btnStart, lblInfo, txtInfo, buttonBox, investmentBox, btnNextDay, btnHelp);

        // Günün ilerleme butonunu en alta ortalamak
        VBox.setMargin(btnNextDay, new Insets(50, 0, 0, 600));
        VBox.setMargin(btnHelp,new Insets(0,0,0,595) );

        Scene scene = new Scene(vbox, 1391, 904); // Ekran boyutu 1391x904
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Durumu güncelleyen metot
    private void updateInfo(TextArea txtInfo) {
        StringBuilder info = new StringBuilder();
        info.append("Adı: ").append(characterName).append("\n")
                .append("Yaş: ").append(age).append("\n")
                .append("Para: ").append(df.format(money)).append(" TL\n")
                .append("Prestij: ").append(prestige).append("\n")
                .append("İş: ").append(job).append("\n")
                .append("Eğitim: ").append(educationLevel == 0 ? "Lise" : (educationLevel == 1 ? "Üniversite" : "Yüksek Lisans")).append("\n")
                .append("Ev: ").append(ownsHouse ? "Var" : "Yok").append("\n")
                .append("Araba: ").append(ownsCar ? "Var" : "Yok").append("\n")
                .append("Aile Durumu:").append(hasFamily ? " Evli" : " Bekar").append("\n")
                .append("Çocuk Sayısı: ").append(childrenCount).append("\n")
                .append("Gün: ").append(day).append("\n")
                .append("Günlük Kazanç: ").append(jobSalary).append(" TL\n")
                .append("Masraflar:\n")
                .append("Ev Masrafı: ").append(df.format(houseExpense)).append(" TL\n")
                .append("Aile Masrafı: ").append(df.format(familyExpense)).append(" TL\n")
                .append("Yatırımlar:\n")
                .append("Dolar: ").append(df.format(dollarInvestment)).append("\n")
                .append("Altın: ").append(df.format(goldInvestment)).append("\n")
                .append("Borsa: ").append(df.format(stockInvestment)).append("\n");


        txtInfo.setText(info.toString());
    }

    // Yardım mesajı gösteren fonksiyon
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
