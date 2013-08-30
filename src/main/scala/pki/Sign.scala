package pki

import java.security.Signature
import java.security.KeyStore
import java.io.FileInputStream
import java.security.PrivateKey

object Sign {
  def main(args: Array[String]) {
    val ksFile = args(0)
    val pass = args(1).toCharArray()
    val alias = args(2)

    val keyStore = KeyStore.getInstance("JKS")
    keyStore.load(new FileInputStream(ksFile), pass)

    val signature = Signature.getInstance("SHA256withRSA")

    //val data = Array[Byte](1, 2, 3)
    val data = Array.ofDim[Byte](1024 * 1024)
    val privateKey = keyStore.getKey(alias, pass).asInstanceOf[PrivateKey]
    println(privateKey)
    println(privateKey.getAlgorithm())
    println(privateKey.getFormat())

    println("署名開始")
    signature.initSign(privateKey)
    signature.update(data)
    val sign = signature.sign()
    println(sign.length)
    println(sign.deep)
    println("署名終了")

    //data(2) = 4
    //sign(10) = 1
    val certificate = keyStore.getCertificate(alias)
    println(certificate)
    val publicKey = certificate.getPublicKey()
    println(publicKey)
    println(publicKey.getAlgorithm())
    println(publicKey.getFormat())

    println("検証開始")
    signature.initVerify(certificate)
    signature.update(data)
    println(signature.verify(sign))
    println("検証終了")
  }
}