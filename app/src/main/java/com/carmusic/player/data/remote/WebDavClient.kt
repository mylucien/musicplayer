package com.carmusic.player.data.remote

import android.util.Xml
import org.xmlpull.v1.XmlPullParser
import java.io.InputStream
import java.io.StringWriter
import java.net.HttpURLConnection
import java.net.URL
import java.util.Base64
import javax.xml.parsers.DocumentBuilderFactory

data class WebDavConfig(
    val name: String = "我的NAS",
    val url: String = "",
    val username: String = "",
    val password: String = ""
)

data class WebDavFile(
    val name: String,
    val path: String,
    val isDirectory: Boolean,
    val size: Long = 0
)

class WebDavClient(private val config: WebDavConfig) {

    private val supportedExtensions = setOf("mp3", "flac", "wav", "aac", "ogg", "m4a", "wma")

    private fun createConnection(urlStr: String, method: String = "GET"): HttpURLConnection {
        val url = URL(urlStr)
        val conn = url.openConnection() as HttpURLConnection
        conn.requestMethod = method
        conn.doInput = true
        conn.connectTimeout = 10000
        conn.readTimeout = 30000

        // Basic Auth
        val auth = "${config.username}:${config.password}"
        val encoded = Base64.getEncoder().encodeToString(auth.toByteArray())
        conn.setRequestProperty("Authorization", "Basic $encoded")

        if (method == "PROPFIND") {
            conn.setRequestProperty("Depth", "1")
            conn.setRequestProperty("Content-Type", "application/xml; charset=utf-8")
            // PROPFIND 需要 body
            conn.doOutput = true
            val body = """<?xml version="1.0" encoding="utf-8"?>
<d:propfind xmlns:d="DAV:">
    <d:prop>
        <d:displayname/>
        <d:resourcetype/>
        <d:getcontentlength/>
        <d:getcontenttype/>
    </d:prop>
</d:propfind>""".trimIndent()
            conn.outputStream.use { os ->
                os.write(body.toByteArray(Charsets.UTF_8))
            }
        }
        return conn
    }

    fun testConnection(): Result<Boolean> = runCatching {
        val conn = createConnection(config.url, "PROPFIND")
        val code = conn.responseCode
        conn.disconnect()
        code in 200..299
    }

    fun listFiles(directoryPath: String = config.url): Result<List<WebDavFile>> = runCatching {
        val conn = createConnection(directoryPath, "PROPFIND")
        val code = conn.responseCode
        if (code !in 200..299) {
            conn.disconnect()
            throw RuntimeException("WebDAV error: HTTP $code")
        }
        val files = parsePropFindResponse(conn.inputStream, directoryPath)
        conn.disconnect()
        files
    }

    fun listAudioFiles(directoryPath: String = config.url): Result<List<WebDavFile>> {
        return listFiles(directoryPath).map { files ->
            files.filter { file ->
                !file.isDirectory &&
                        file.name.substringAfterLast('.').lowercase() in supportedExtensions
            }
        }
    }

    fun getAudioStream(filePath: String): Result<InputStream> = runCatching {
        val conn = createConnection(filePath, "GET")
        val code = conn.responseCode
        if (code !in 200..299) {
            conn.disconnect()
            throw RuntimeException("WebDAV GET error: HTTP $code")
        }
        conn.inputStream
    }

    /**
     * 解析 WebDAV PROPFIND 返回的 XML
     */
    private fun parsePropFindResponse(inputStream: InputStream, baseUrl: String): List<WebDavFile> {
        val files = mutableListOf<WebDavFile>()
        val basePath = URL(baseUrl).path.trimEnd('/')

        val parser = Xml.newPullParser()
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true)
        parser.setInput(inputStream, "UTF-8")

        var currentHref: String? = null
        var currentName: String? = null
        var currentLength: Long = 0
        var isCollection = false
        var inProp = false
        var inResponse = false

        while (parser.eventType != XmlPullParser.END_DOCUMENT) {
            when (parser.eventType) {
                XmlPullParser.START_TAG -> {
                    val tagName = parser.name.lowercase()
                    when {
                        tagName == "response" -> {
                            inResponse = true
                            currentHref = null
                            currentName = null
                            currentLength = 0
                            isCollection = false
                        }
                        tagName == "prop" -> inProp = true
                        tagName == "href" && inResponse -> {
                            parser.next()
                            currentHref = parser.text?.trim()
                        }
                        tagName == "displayname" && inProp -> {
                            parser.next()
                            currentName = parser.text?.trim()
                        }
                        tagName == "getcontentlength" && inProp -> {
                            parser.next()
                            currentLength = parser.text?.trim()?.toLongOrNull() ?: 0
                        }
                        tagName == "collection" -> isCollection = true
                    }
                }
                XmlPullParser.END_TAG -> {
                    val tagName = parser.name.lowercase()
                    when {
                        tagName == "response" -> {
                            inResponse = false
                            val href = currentHref ?: continue
                            val name = currentName ?: href.trimEnd('/').substringAfterLast('/')
                            // 跳过根目录自身
                            if (href.trimEnd('/') != basePath) {
                                files.add(
                                    WebDavFile(
                                        name = name,
                                        path = href,
                                        isDirectory = isCollection,
                                        size = currentLength
                                    )
                                )
                            }
                        }
                        tagName == "prop" -> inProp = false
                    }
                }
            }
            parser.next()
        }
        return files
    }

    companion object {
        @Volatile
        private var instances = mutableMapOf<String, WebDavClient>()

        fun getInstance(config: WebDavConfig): WebDavClient {
            return instances.getOrPut(config.url) { WebDavClient(config) }
        }

        fun clearInstances() {
            instances.clear()
        }
    }
}
