package org.example;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.io.*;
import java.io.FileReader;
import java.util.ArrayList;
  class Main {
    public static void main(String[] args) {
        System.out.println("Lütfen yapma istediğiniz işlemi seçiniz.");
        System.out.println("1-Elit üye Ekleme\n2-Genel Üye Ekleme\n3-Mail Gönderme");
        Scanner girdi=new Scanner(System.in);
        int secim=girdi.nextInt();
        UyeKayit bilgiaktar=new UyeKayit();//uye islemleri icin Uyekayit sinifindan nesne olusturuluyor
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        String isim = "", soyisim = "", email = "";
        if(secim==1){//elit üye bilgileri alinir
            try {
                System.out.print("Elit Üye İsim: ");
                isim = input.readLine();
                System.out.print("Elit Üye Soyisim: ");
                soyisim = input.readLine();
                System.out.print("Elit Üye E-posta: ");
                email = input.readLine();
            } catch (IOException e) {
                System.out.println("Girdi işleminde hata oluştu: " + e.getMessage());
            }
            UyeKayit.elitUye(isim,soyisim,email);
        }
        else if(secim==2){//genel uye bilgileri alinir
            try {
                System.out.print("Genel Uye İsim: ");
                isim = input.readLine();
                System.out.print("Genel Uye Soyisim: ");
                soyisim = input.readLine();
                System.out.print("Genel UyeE-posta: ");
                email = input.readLine();
            } catch (IOException e) {
                System.out.println("Girdi işleminde hata oluştu: " + e.getMessage());
            }
            UyeKayit.genelUye(isim,soyisim,email);
        }
        else if(secim==3){
            System.out.println("Mail göndermek istediğiniz kitleyi seçiniz");
            System.out.println("1-Elit Üyelere Mail\n2-Genel Üyelere Mail\n3-Tüm Üyelere Mail");
            int mailsecim=girdi.nextInt();
            switch (mailsecim) {
                case 1 -> { //elit üyeler listesindeki bütün mail adreslerine mail at
                    UyeKayit girilen = new UyeKayit();
                    List<String> elitMailler = UyeKayit.mailAyristirma(mailsecim);
                    sendMail mailGonderici_elit = new sendMail();
                    mailGonderici_elit.elitMaillereGonder(elitMailler);
                }
                case 2 -> {//genel üyeler listesindeki bütün mail adreslerine mail at
                    UyeKayit girilen1 = new UyeKayit();
                    List<String> genelMailler = UyeKayit.mailAyristirma(mailsecim);
                    sendMail mailGonderici_genel = new sendMail();
                    mailGonderici_genel.genelMaillereGonder(genelMailler);
                }
                case 3 -> {//tüm üyeler listesindeki bütün mail adreslerine mail at
                    UyeKayit girilen2 = new UyeKayit();
                    List<String> tumMailler = UyeKayit.mailAyristirma(mailsecim);
                    sendMail mailGonderici_tum = new sendMail();
                    mailGonderici_tum.tumMaillereGonder(tumMailler);
                }
                default -> System.out.println("Yanlış giris yaptiniz");
            }
        }
        else{
            System.out.println("Yanlış giriş yaptınız");
        }
    }
}
class sendMail extends Main {//Main sinifindan sendMail sinifi kalitildi
    public void tumMaillereGonder(List<String> tumMailler) {//butun mail adreslerine mail gonderen fonksiyon
        System.out.print("Mailin içeriğini giriniz:");
        Scanner metin=new Scanner(System.in);
        String icerik=metin.nextLine();
        Properties props = new Properties();//outlook sunucusuna baglanilir
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.office365.com");
        props.put("mail.smtp.port", "587");
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                //asagiya mail gonderecek hesabin mail adresini ve sifresini giriniz.
                //outlook sunucusu kullanildiğindan uzantisi outlook olan hesap kullanmalisiniz.
                return new PasswordAuthentication("MAIL GONDERECEK HESAP", "HESAP SIFRESI");
            }
        });
        try {
            for (String mail : tumMailler) {
                Message message = new MimeMessage(session);
                //tekrardan mail gonderecek olan mail adresini yazin.
                message.setFrom(new InternetAddress("MAIL GONDERECEK HESAP")); // gönderici
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mail)); // alici
                message.setSubject("Konu Basligi");
                message.setText(String.valueOf(icerik));
                Transport.send(message);
            }
            System.out.println("İslem tamamlanmistir.");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
    public void elitMaillereGonder(List<String> elitMailler) {//elit mail adreslerine mail gonderen fonksiyon
        System.out.print("Mailin içeriğini giriniz:");
        Scanner metin=new Scanner(System.in);
        String icerik=metin.nextLine();
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.office365.com");
        props.put("mail.smtp.port", "587");
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                //asagiya mail gonderecek hesabin mail adresini ve sifresini giriniz.
                //outlook sunucusu kullanildiğindan uzantisi outlook olan hesap kullanmalisiniz.
                return new PasswordAuthentication("MAIL GONDERECEK HESAP", "HESAP SIFRESI");
            }
        });
        try {
            for (String mail : elitMailler) {//listedeki mail adreslerine sirayla mail atilir
                Message message = new MimeMessage(session);
                //tekrardan mail gonderecek olan mail adresini yazin.
                message.setFrom(new InternetAddress("MAIL GONDERECEK HESAP")); // gönderici
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mail)); // alici
                message.setSubject("Konu Basligi");
                message.setText(icerik);
                Transport.send(message);
            }
            System.out.println("İslem tamamlanmistir");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
    public void genelMaillereGonder(List<String> genelMailler) {
        System.out.print("Mailin içeriğini giriniz:");
        Scanner metin=new Scanner(System.in);
        String icerik=metin.nextLine();
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.office365.com");
        props.put("mail.smtp.port", "587");
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                //asagiya mail gonderecek hesabin mail adresini ve sifresini giriniz.
                //outlook sunucusu kullanildiğindan uzantisi outlook olan hesap kullanmalisiniz.
                return new PasswordAuthentication("MAIL GONDERECEK HESAP", "HESAP SIFRESI");
            }
        });
        try {
            for (String mail : genelMailler) {
                Message message = new MimeMessage(session);
                //tekrardan mail gonderecek olan mail adresini yazin.
                message.setFrom(new InternetAddress("MAIL GONDERECEK HESAP")); // gönderici
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mail)); // alici
                message.setSubject("Konu Basligi");
                message.setText(icerik);
                Transport.send(message);
            }
            System.out.println("İslem tamamlanmistir");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
class UyeKayit {
    public static void elitUye(String isim, String soyad, String email) {
        try {
            File dosya = new File("kullanicilar.txt");
            FileReader fileReader = new FileReader(dosya);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String satir;
            int elitIndis = -1;
            int i = 0;
            String[] satirlar = new String[1000];//kullanicilar.txt dosyasindaki her satir diziye atanir
            while ((satir = bufferedReader.readLine()) != null) {
                satirlar[i] = satir;//basligi iceren satir bulunur
                i++;
                if (satir.contains("ELİT UYELER")) {
                    elitIndis = i - 1;
                }
            }
            bufferedReader.close();
            if (elitIndis == -1) {
                elitIndis = i;
                satirlar[elitIndis] = "ELİT UYELER";
                i++;
            }
            int bosSatirIndis = -1;//kullanici bilgilerinin yazilacagi baslik altindaki ilk bos satir bulunur
            for (int j = elitIndis + 1; j < i; j++) {
                if (satirlar[j].trim().equals("")) {
                    bosSatirIndis = j;
                    break;
                }
            }
            if (bosSatirIndis == -1) {
                bosSatirIndis = i;
                i++;
            }
            satirlar[bosSatirIndis] = isim + "\t" + soyad + "    " + email;//kullanici bilgileri yazilir
            FileWriter fileWriter = new FileWriter(dosya);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            for (int j = 0; j < i; j++) {
                bufferedWriter.write(satirlar[j]);
                bufferedWriter.newLine();
            }
            bufferedWriter.close();//islem tamamlaninca dosya kapatilir
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void genelUye(String isim, String soyad, String email) {
        try {
            File dosya = new File("kullanicilar.txt");
            FileReader fileReader = new FileReader(dosya);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String satir;
            int elitIndis = -1;
            int i = 0;
            String[] satirlar = new String[1000];
            while ((satir = bufferedReader.readLine()) != null) {
                satirlar[i] = satir;//basligi iceren satir bulunur
                i++;
                if (satir.contains("GENEL UYELER")) {
                    elitIndis = i - 1;
                }
            }
            bufferedReader.close();
            if (elitIndis == -1) {
                elitIndis = i;
                satirlar[elitIndis] = "GENEL UYELER";
                i++;
            }
            int bosSatirIndis = -1;
            for (int j = elitIndis + 1; j < i; j++) {
                if (satirlar[j].trim().equals("")) {
                    bosSatirIndis = j;//kullanici bilgilerinin yazilacagi baslik altindaki ilk bos satir bulunur
                    break;
                }
            }
            if (bosSatirIndis == -1) {
                bosSatirIndis = i;
                i++;
            }
            satirlar[bosSatirIndis] = isim + "\t" + soyad + "    " + email;//kullanici bilgileri yazilir
            FileWriter fileWriter = new FileWriter(dosya);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            for (int j = 0; j < i; j++) {
                bufferedWriter.write(satirlar[j]);
                bufferedWriter.newLine();
            }
            bufferedWriter.close();//islem tamamlaninca dosya kapatilir
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<String> mailAyristirma(int mailsecim) {
        List<String> genelMailler = new ArrayList<>();
        List<String> elitMailler = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("kullanicilar.txt"))) {
            String line;
            boolean isGenel = false;
            while ((line = br.readLine()) != null) {
                if (line.contains("GENEL UYELER")) {//basligi iceren satir bulunur
                    isGenel = true;
                    continue;
                } else if (line.contains("ELİT UYELER")) {//basligi iceren satir bulunur
                    isGenel = false;
                    continue;
                }
                if (isGenel && line.contains("@")) {
                    String[] tokens = line.split("\\s+");
                    //Genel basligindan altta bulunan satirlarda "@" iceren kelimeler genelMailler listesine eklenir
                    for (String token : tokens) {
                        if (token.contains("@")) {
                            genelMailler.add(token);
                        }
                    }
                } else if (!isGenel && line.contains("@")) {
                    String[] tokens = line.split("\\s+");
                    //Elit basligindan altta bulunan satirlarda "@" iceren kelimeler elitMailler listesine eklenir
                    for (String token : tokens) {
                        if (token.contains("@")) {
                            elitMailler.add(token);
                        }
                    }
                }
            }
        } catch (IOException e) {//Hata ayiklamasi icin kullanilir
            System.err.format("IOException: %s%n", e);
        }
        List<String> tumMailler = new ArrayList<>();
        tumMailler.addAll(genelMailler);
        tumMailler.addAll(elitMailler);
        if (mailsecim == 1)//mail gonderilecek kesime gore deger donduruluyor
        {
            return elitMailler;
        } else if (mailsecim == 2) {
            return genelMailler;
        } else {
            return tumMailler;
        }
    }
}