package HandlePDF;

/**
  * @ProjectName:    Escape
  * @ClassName:      CreatePDF
  * @Description:    用于生成 PDF 证书
  * @Author:         Han Chubo
  */


import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class CreatePDF {

    public CreatePDF(double score, String name) throws DocumentException {
        File file = new File("Cert.pdf");
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String path = "src/WendyOne-Regular.ttf";//自己的字体资源路径
        Font font = FontFactory.getFont(path, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED,30f, Font.NORMAL, BaseColor.BLACK);

        Rectangle rect = new Rectangle(PageSize.A4.rotate());
        // rect.setBackgroundColor(new BaseColor(249, 205, 173));

        Document certificate = new Document(rect);
        try {
            PdfWriter writer  = PdfWriter.getInstance(certificate, new FileOutputStream(file));
            writer.setPageEvent(new Watermark("Escape!"));
        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        }
        certificate.open();

        Paragraph title = new Paragraph("Certificate of Escape!", font);
        title.setAlignment(1); //设置文字居中 0靠左   1，居中     2，靠右
        title.setLeading(40f); //行间距
        certificate.add(title);

        String pathBody = "src/SourceHanSans-Bold.otf";//自己的字体资源路径
        Font fontBody = FontFactory.getFont(pathBody, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED,20f, Font.NORMAL, BaseColor.BLACK);
        Paragraph body = new Paragraph(name + "," , fontBody);
        body.setSpacingBefore(10f);
        body.setSpacingBefore(10f);
        body.setIndentationRight(12);
        certificate.add(body);

        Paragraph body2 = new Paragraph("祝贺你成功在 Escape! 游戏中获得超过 50 分，远超大家的平均水平！也谢谢你在第一时间尝试并体验了本款制作仍然不够精良的游戏，作者在本游戏的开发中学到了（速成）了简单的 Java 基础，并自主解决了一些问题。希望你继续支持本游戏的开发。\n你本次的分数为：", fontBody);
        body2.setFirstLineIndent(43);
        body2.setIndentationLeft(12);
        body2.setIndentationRight(12);
        certificate.add(body2);

        String pathScore = "src/WendyOne-Regular.ttf";//自己的字体资源路径
        Font fontScore = FontFactory.getFont(pathScore, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED,100f, Font.NORMAL, BaseColor.BLACK);
        Paragraph scorePara = new Paragraph(Double.toString(score),fontScore);
        scorePara.setAlignment(1);
        certificate.add(scorePara);

        certificate.close();

    }


}
