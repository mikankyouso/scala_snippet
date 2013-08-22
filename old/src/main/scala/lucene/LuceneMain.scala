/*
package lucene

import java.io.Reader
import java.io.StringReader
import scala.collection.JavaConversions.asScalaIterator
import org.apache.lucene.analysis.ngram.NGramTokenizer
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute
import org.apache.lucene.analysis.Analyzer
import org.apache.lucene.analysis.TokenStream
import org.apache.lucene.document.Document
import org.apache.lucene.document.Field
import org.apache.lucene.index.IndexReader
import org.apache.lucene.index.IndexWriter
import org.apache.lucene.index.IndexWriterConfig
import org.apache.lucene.queryParser.QueryParser
import org.apache.lucene.search.IndexSearcher
import org.apache.lucene.store.RAMDirectory
import org.apache.lucene.util.Version
import org.apache.lucene.analysis.cjk.CJKAnalyzer
import org.apache.lucene.store.FSDirectory
import java.io.File
import org.apache.lucene.search.NGramPhraseQuery
import org.apache.lucene.search.PhraseQuery
import scala.util.Random
import org.apache.lucene.analysis.KeywordAnalyzer
import org.apache.lucene.search.highlight.Highlighter
import org.apache.lucene.search.highlight.QueryScorer
import org.apache.lucene.document.CompressionTools

class NGramAnalyzer(minGram: Int, maxGram: Int) extends Analyzer {
  def tokenStream(fieldName: String, reader: Reader): TokenStream = {
    new NGramTokenizer(reader, minGram, maxGram)
  }
}

object LuceneMain {
  val version = Version.LUCENE_35

  val file = new File("work/index")
  //val file = new File("work/kwindex")
  //val directory = new RAMDirectory()
  val directory = FSDirectory.open(file)
  println(directory)

  val minGram = 1
  val maxGram = 3

  //val analyzer = new CJKAnalyzer(version)

  def main(args: Array[String]) {
    //write

    val reader = IndexReader.open(directory)

    val searcher = new IndexSearcher(reader)

    val phrase = "8b8k"
    val gram = (minGram max phrase.size) min maxGram
    println(gram)

    val analyzer = new NGramAnalyzer(gram, gram)
    //    val analyzer = new KeywordAnalyzer

    val parser = new QueryParser(version, "name", analyzer) {
      override def newPhraseQuery() = new NGramPhraseQuery(gram)
    }
    parser.setAutoGeneratePhraseQueries(true)

    val query = parser.parse(phrase)
    println(query.getClass())
    println(query)
    query match {
      case q: PhraseQuery => println(q.getTerms().deep)
      case _              =>
    }
    println(searcher.rewrite(query))
    searcher.rewrite(query) match {
      case q: PhraseQuery => println(q.getTerms().deep)
      case _              =>
    }

    val topDocs = searcher.search(query, 10)
    println(topDocs.totalHits)
    val scorer = new QueryScorer(query)
    val hl = new Highlighter(scorer)
    for (scoreDoc <- topDocs.scoreDocs) {
      val doc = searcher.doc(scoreDoc.doc)
      println(scoreDoc, doc)
      println(hl.getBestFragment(analyzer, "name", doc.get("name")))
    }

    val tokenStream = analyzer.tokenStream("x", new StringReader("a"))
    //tokenStream.getAttributeClassesIterator().foreach(println)

    val charTermAttr = tokenStream.getAttribute(classOf[CharTermAttribute])
    val offsetAttr = tokenStream.getAttribute(classOf[OffsetAttribute])
    while (tokenStream.incrementToken()) {
      println(charTermAttr, offsetAttr)
    }

    reader.close()
  }

  def write {
    val analyzer = new NGramAnalyzer(minGram, maxGram)
    //    val analyzer = new KeywordAnalyzer

    val config = new IndexWriterConfig(version, analyzer)
    val writer = new IndexWriter(directory, config)
    writer.setInfoStream(System.err)

    def index(value: String) {
      val doc = new Document
      doc.add(new Field("name", value, Field.Store.YES, Field.Index.ANALYZED))
      //doc.add(new Field("name", value, Field.Store.NO, Field.Index.ANALYZED))
      //doc.add(new Field("name", CompressionTools.compressString(value)))
      writer.addDocument(doc)
    }
    Random.alphanumeric.toIterator.grouped(10).take(1000000).foreach(cs => index(cs.mkString))

    //writer.forceMergeDeletes()
    writer.close()
  }
}
*/ 