package pdf

import com.itextpdf.text.Document
import com.itextpdf.text.pdf.PdfWriter
import scalax.file.Path
import scalax.io.Resource
import scalax.io.OpenOption
import com.itextpdf.text.Paragraph
import com.itextpdf.text.Font
import com.itextpdf.text.pdf.BaseFont

object Pdf {
  def main(args: Array[String]) {
    val doc = new Document()
    Path("target", "foo.pdf").outputStream().foreach { out =>
      val writer = PdfWriter.getInstance(doc, out)
      val font = new Font(BaseFont.createFont("HeiseiKakuGo-W5", "UniJIS-UCS2-H", BaseFont.NOT_EMBEDDED), 11);
      doc.open()
      doc.add(new Paragraph("aaa"))
      doc.add(new Paragraph("あいうえ", font))
      doc.add(new Paragraph("zzz!\"#%$'}"))
      doc.close()
    }
  }
}