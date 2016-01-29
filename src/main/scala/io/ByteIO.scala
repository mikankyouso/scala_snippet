package io

import java.io.File
import java.io.Closeable
import java.nio.file.Path
import java.nio.file.Files
import java.nio.file.{ OpenOption => JOpenOption }
import java.nio.file.StandardOpenOption
import java.nio.ByteBuffer
import java.io.EOFException
import java.nio.file.OpenOption
import scala.annotation.tailrec
import scala.util.Try
import scala.collection.mutable.ArrayBuffer
import java.nio.channels.FileChannel
import java.nio.channels.SeekableByteChannel
import java.nio.charset.Charset
import java.io.InputStreamReader
import scala.io.Source
import java.util.concurrent.locks.ReentrantReadWriteLock
import java.util.concurrent.locks.Lock
import scala.collection.mutable.Buffer
import java.io.InputStream
import java.io.BufferedReader
import scala.collection.generic.CanBuildFrom
import java.io.OutputStream
import java.nio.channels.Channels
import java.nio.channels.WritableByteChannel

trait ByteIO[A] extends Closeable {
  def source: A
  def newInput(): ByteInput
  def newOutput(): ByteOutput
  def close(): Unit
}

protected abstract class AbstractByteIO[A] extends ByteIO[A] {
  protected var closed = false
  protected val rwLock = new ReentrantReadWriteLock(true)

  def closeImpl(): Unit
  def close(): Unit = {
    rwLock.writeLock().lock()
    try {
      if (!closed) {
        closed = true
        closeImpl()
      }
    } finally {
      rwLock.writeLock().unlock()
    }
  }
}

abstract class ByteInput(lock: Lock) {
  protected var position: Long = 0
  protected lazy val buffer = ByteBuffer.allocate(4096)

  protected def readImpl(buffer: ByteBuffer): Int

  protected def lockWith[A](f: => A): A = {
    lock.lock()
    try f finally lock.unlock()
  }

  def read(buffer: ByteBuffer): Int = lockWith {
    val len = readImpl(buffer)
    if (len > 0) position += len
    len
  }

  def readFully(buffer: ByteBuffer, failOnEOF: Boolean = true): Unit = lockWith {
    @tailrec
    def f(): Unit = {
      read(buffer) match {
        case -1 if failOnEOF && buffer.hasRemaining() => throw new EOFException()
        case n if n >= 0 && buffer.hasRemaining()     => f()
        case _                                        => ()
      }
    }
    f()
  }

  def read(array: Array[Byte]): Int = read(array, 0, array.length)
  def read(array: Array[Byte], offset: Int, length: Int): Int = read(ByteBuffer.wrap(array, offset, length))
  def readFully(array: Array[Byte]): Unit = readFully(array, 0, array.length)
  def readFully(array: Array[Byte], offset: Int, length: Int): Unit = readFully(ByteBuffer.wrap(array, offset, length))

  def readByteArray(length: Int, failOnEOF: Boolean = true): Array[Byte] = {
    val bs = new Array[Byte](length)
    val buf = ByteBuffer.wrap(bs)
    readFully(buf, failOnEOF)
    if (buf.hasRemaining()) {
      val ret = new Array[Byte](buf.position())
      compat.Platform.arraycopy(bs, 0, ret, 0, ret.length)
      ret
    } else {
      bs
    }
  }

  protected def readPrimitive(length: Int): ByteBuffer = {
    buffer.clear().limit(length)
    readFully(buffer)
    buffer.flip()
    buffer
  }

  def readByte(): Byte = lockWith(readPrimitive(1).get())
  def readShort(): Short = lockWith(readPrimitive(2).getShort())
  def readChar(): Char = lockWith(readPrimitive(2).getChar())
  def readInt(): Int = lockWith(readPrimitive(4).getInt())
  def readLong(): Long = lockWith(readPrimitive(8).getLong())
  def readFloat(): Float = lockWith(readPrimitive(4).getFloat())
  def readDouble(): Double = lockWith(readPrimitive(8).getDouble())

  def readString(byteLength: Int, charset: Charset = ByteIO.UTF8, failOnEOF: Boolean = true): String = {
    require(!(byteLength < 0 && failOnEOF))
    if (byteLength < 0) {
      val in = new InputStream() {
        def read(): Int = try readByte() & 0xFF catch { case t: EOFException => -1 }
        override def read(b: Array[Byte], off: Int, len: Int): Int = ByteInput.this.read(b, off, len)
      }
      val r = new BufferedReader(new InputStreamReader(in, charset))
      lockWith {
        Iterator.continually(r.read()).takeWhile(_ >= 0).map(_.toChar).mkString
      }
    } else {
      new String(readByteArray(byteLength, failOnEOF), charset)
    }
  }

  def copyStream(out: OutputStream, length: Long = -1, failOnEOF: Boolean = true): Long =
    copyChannel(Channels.newChannel(out), length, failOnEOF)

  def copyChannel(ch: WritableByteChannel, length: Long = -1, failOnEOF: Boolean = true): Long = lockWith {
    buffer.clear()
    @tailrec
    def f(total: Long): Long = {
      if (total == length)
        total
      else {
        if (length > 0) buffer.limit((length - total).min(buffer.remaining()).toInt + buffer.position())
        read(buffer) match {
          case -1 if failOnEOF => throw new EOFException()
          case -1              => total
          case n => {
            buffer.flip()
            ch.write(buffer)
            buffer.compact()
            f(total + n)
          }
        }
      }
    }
    f(0)
  }
}

protected trait SeekableByteInput { self: ByteInput =>
  protected def seekImpl(position: Long): Unit
  def seek(position: Long): this.type = { seekImpl(position); this.position = position; this }
}

abstract class ByteOutput(lock: Lock) {
  protected var position: Long = 0

  def writeImpl(buffer: ByteBuffer): Int

  protected def lockWith[A](f: => A): A = {
    lock.lock()
    try f finally lock.unlock()
  }

  def write(buffer: ByteBuffer): Int = lockWith {
    val len = writeImpl(buffer)
    position += len
    len
  }

}

protected trait SeekableByteOutput { self: ByteOutput =>
  protected def seekImpl(position: Long): Unit
  def seek(position: Long): this.type = { seekImpl(position); this.position = position; this }
}

object ByteIO {
  val UTF8 = Charset.forName("UTF-8")
  def from(path: Path, options: OpenOption*): ByteIO[Path] =
    new SeekableByteChannelByteIO(Files.newByteChannel(path, options: _*), path)
}

protected class SeekableByteChannelByteIO[A](channel: SeekableByteChannel, val source: A) extends AbstractByteIO[A] {

  def newInput(): ByteInput = new ByteInput(rwLock.writeLock()) with SeekableByteInput { //チャネルの位置は1つなのでライトロック
    protected def readImpl(buffer: ByteBuffer): Int = {
      seekImpl(position)
      channel.read(buffer)
    }
    protected def seekImpl(position: Long): Unit = channel.position(position)
  }

  def newOutput(): ByteOutput = new ByteOutput(rwLock.writeLock()) with SeekableByteOutput {
    def writeImpl(buffer: ByteBuffer): Int = {
      seekImpl(position)
      channel.write(buffer)
    }
    protected def seekImpl(position: Long): Unit = channel.position(position)
  }

  def closeImpl(): Unit = channel.close()
}
