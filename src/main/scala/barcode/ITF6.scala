package barcode

import org.krysalis.barcode4j.cli.Main
import org.krysalis.barcode4j.impl.int2of5.ITF14Bean
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider
import java.awt.image.BufferedImage
import scalax.file.Path
import scalax.io.OpenOption
import scalax.io.StandardOpenOption

object ITF6 {
  def main(args: Array[String]) {
    val itf = new ITF14Bean
    for (out <- Path("foo.png").outputStream(StandardOpenOption.WriteTruncate: _*)) {
      val canvas = new BitmapCanvasProvider(out, "image/x-png", 150, BufferedImage.TYPE_BYTE_BINARY, true, 0)
      itf.generateBarcode(canvas, "12345678901231")
      canvas.finish()
    }
  }
}