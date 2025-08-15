package com.ruoyi.common.utils.file;

import com.aspose.slides.Presentation;
import com.aspose.slides.Slide;
import com.aspose.words.Document;
import com.aspose.words.FontSettings;
import com.aspose.words.License;
import com.aspose.words.SaveFormat;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class AsposeUtils {

    //获取license
    private static void getLicense() {
        try (InputStream is = AsposeUtils.class.getClassLoader().getResourceAsStream("License.xml" )) {
            License license = new License();
            license.setLicense(is);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //word转pdf
    public static void wordToPdf(String wordPath, String pdfPath) {
        getLicense();
        File file = new File(pdfPath);
        FontSettings defaultInstance = FontSettings.getDefaultInstance();
        defaultInstance.setFontsFolder("/usr/share/fonts/msttcore", true);
        try (FileOutputStream os = new FileOutputStream(file)) {
            Document doc = new Document(wordPath);
            FontSettings fontSettings = new FontSettings();
            fontSettings.getSubstitutionSettings().getDefaultFontSubstitution().setDefaultFontName("SimSun");
            doc.setFontSettings(fontSettings);
            doc.save(os, SaveFormat.PDF);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void pptToPdf(String pptPath, String pdfPath) {
        getLicensePPT();
        try {
            FontSettings defaultInstance = FontSettings.getDefaultInstance();
            if (!System.getProperty("os.name").toLowerCase().contains("windows")) {
                defaultInstance.setFontsFolder("/usr/share/fonts/msttcore", true);
            }

            Presentation pres = new Presentation(pptPath);
            int slidesCount = pres.getSlides().size();

            for (int i = 0; i < slidesCount; i++) {
                Slide slide = (Slide) pres.getSlides().get_Item(i);

                // 创建一个只包含当前幻灯片的新Presentation对象
                Presentation singleSlidePres = new Presentation();
                singleSlidePres.getSlides().addClone(slide);

                // 设置输出PDF路径，每个幻灯片保存为单独的PDF文件
                String slidePdfPath = pdfPath + "_" + (i + 1) + ".pdf";
                FileOutputStream fileOS = new FileOutputStream(slidePdfPath);

                // 保存当前幻灯片为PDF
                singleSlidePres.save(fileOS, com.aspose.slides.SaveFormat.Pdf);

                fileOS.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean getLicensePPT() {
        boolean result = false;
        try {
            String license =
                    "<License>\n" +
                            "  <Data>\n" +
                            "    <Products>\n" +
                            "      <Product>Aspose.Total for Java</Product>\n" +
                            "      <Product>Aspose.Words for Java</Product>\n" +
                            "    </Products>\n" +
                            "    <EditionType>Enterprise</EditionType>\n" +
                            "    <SubscriptionExpiry>20991231</SubscriptionExpiry>\n" +
                            "    <LicenseExpiry>20991231</LicenseExpiry>\n" +
                            "    <SerialNumber>8bfe198c-7f0c-4ef8-8ff0-acc3237bf0d7</SerialNumber>\n" +
                            "  </Data>\n" +
                            "  <Signature>sNLLKGMUdF0r8O1kKilWAGdgfs2BvJb/2Xp8p5iuDVfZXmhppo+d0Ran1P9TKdjV4ABwAgKXxJ3jcQTqE/2IRfqwnPf8itN8aFZlV3TJPYeD3yWE7IT55Gz6EijUpC7aKeoohTb4w2fpox58wWoF3SNp6sK6jDfiAUGEHYJ9pjU=</Signature>\n" +
                            "</License>";
            InputStream is = new ByteArrayInputStream(license.getBytes("UTF-8" ));
            com.aspose.slides.License aposeLic = new com.aspose.slides.License();
            aposeLic.setLicense(is);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
