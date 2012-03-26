package lucene

import org.apache.lucene.index.IndexWriter
import org.apache.lucene.store.RAMDirectory
import org.apache.lucene.index.IndexWriterConfig
import org.apache.lucene.util.Version
import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.analysis.Analyzer
import java.io.Reader
import org.apache.lucene.analysis.TokenStream
import org.apache.lucene.analysis.ngram.NGramTokenizer
import org.apache.lucene.document.Document
import org.apache.lucene.document.Field
import org.apache.lucene.search.IndexSearcher
import org.apache.lucene.index.IndexReader
import org.apache.lucene.queryParser.QueryParser

object LuceneMain {
  def main(args: Array[String]) {
    val version = Version.LUCENE_35

    val directory = new RAMDirectory
    val analyzer = new Analyzer() {
      def tokenStream(fieldName: String, reader: Reader): TokenStream = {
        new NGramTokenizer(reader)
      }
    }
    val config = new IndexWriterConfig(version, analyzer)
    val writer = new IndexWriter(directory, config)

    val doc = new Document
    doc.add(new Field("name", "value", Field.Store.YES, Field.Index.ANALYZED))
    writer.addDocument(doc)

    val reader = IndexReader.open(writer, true)

    val searcher = new IndexSearcher(reader)
    val parser = new QueryParser(version, "name", analyzer)
    val query = parser.parse("va")

    val topDocs = searcher.search(query, 10)
    println(topDocs.totalHits)
    for (scoreDoc <- topDocs.scoreDocs) {
      println(scoreDoc)
      println(searcher.doc(scoreDoc.doc))
    }

    writer.close()
  }
}