/*
package lucene

import java.io.StringReader
import scala.collection.JavaConversions._
import org.apache.lucene.analysis.ja.JapaneseAnalyzer
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute
import org.apache.lucene.analysis.Analyzer
import org.apache.lucene.util.Version
import org.apache.lucene.analysis.ja.JapaneseTokenizer
import org.apache.lucene.analysis.ngram.NGramTokenizer
import org.apache.lucene.analysis.Tokenizer
import java.io.Reader
import org.apache.lucene.analysis.ja.JapaneseTokenizer.Mode

object AnalyzeMain {

  implicit def str2reader(s: String): Reader = new StringReader(s)

  def main(args: Array[String]) {
    tokenize(new NGramTokenizer("貴社の", 1, 2))
    tokenize(new JapaneseTokenizer("貴社の記者が汽車で帰社した", null, true, Mode.NORMAL))
    tokenize(new JapaneseTokenizer("貴社の記者が汽車で帰社した", null, true, Mode.EXTENDED))
    tokenize(new JapaneseTokenizer("東欧を覆う鳳凰", null, true, Mode.SEARCH))
  }

  private def analyze(analyzer: Analyzer, str: java.lang.String): Unit = {
    println("*** %s: %s ***".format(analyzer, str))
    val tokenStream = analyzer.tokenStream("x", new StringReader(str))

    val charTermAttr = tokenStream.getAttribute(classOf[CharTermAttribute])
    val offsetAttr = tokenStream.getAttribute(classOf[OffsetAttribute])
    while (tokenStream.incrementToken()) {
      tokenStream.getAttributeClassesIterator().foreach(
        c => println("%26s: %s".format(c.getSimpleName, tokenStream.getAttribute(c))))
      println
    }
  }

  private def tokenize(tokenizer: Tokenizer): Unit = {
    println("*** %s ***".format(tokenizer))

    while (tokenizer.incrementToken()) {
      tokenizer.getAttributeClassesIterator().foreach(
        c => println("%26s: %s".format(c.getSimpleName, tokenizer.getAttribute(c))))
      println
    }
    println
  }
}
*/ 