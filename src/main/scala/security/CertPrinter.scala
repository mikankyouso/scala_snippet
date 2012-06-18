package security
import java.security.cert.Certificate
import java.security.cert.CertificateFactory
import java.io.ByteArrayInputStream

object CertPrinter {
  def main(args: Array[String]) {
    val factory = CertificateFactory.getInstance("X509")
    val pem =
      """-----BEGIN CERTIFICATE-----

-----END CERTIFICATE-----

      """
    val cert = factory.generateCertificate(new ByteArrayInputStream(pem.getBytes))
    println(cert)
  }
}
