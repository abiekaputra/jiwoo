package dev.abiekaputra.jiwoo.data

interface JournalCipher {
    fun encrypt(plainText: String): String

    fun decrypt(cipherText: String): String
}
