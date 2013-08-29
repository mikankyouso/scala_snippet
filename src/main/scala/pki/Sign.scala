package pki

import java.security.Signature
import java.security.KeyStore
import java.io.FileInputStream
import java.security.PrivateKey

object Sign {
  def main(args: Array[String]) {
    val alias = ""
    val pass = "".toCharArray()
    val ksFile = """"""

    val ks = KeyStore.getInstance("JKS")
    ks.load(new FileInputStream(ksFile), pass)

    val s = Signature.getInstance("SHA1withRSA")

    //val data = Array[Byte](1, 2, 3)
    val data = Array.ofDim[Byte](1024 * 1024 * 1024)
    println("署名開始")
    s.initSign(ks.getKey(alias, pass).asInstanceOf[PrivateKey])
    s.update(data)
    val sign = s.sign()
    println("署名終了")

    println(sign.length)
    println(sign.deep)

    //data(2) = 4
    //sign(10) = 1
    println("検証開始")
    s.initVerify(ks.getCertificate(alias))
    s.update(data)
    println(s.verify(sign))
    println("検証終了")
  }
}