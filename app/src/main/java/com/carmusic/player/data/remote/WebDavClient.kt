package com.carmusic.player.data.remote

import com.github.lookfirst.dav4j.DavMethod
import com.github.lookfirst.dav4j.DavResource
import com.github.lookfirst.dav4j.PropFindMethod
import java.io.InputStream
import java.net.URL

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

    fun testConnection(): Result<Boolean> = runCatching {
        val url = URL(config.url)
        val propFind = PropFindMethod(url)
        propFind.setUsernamePassword(config.username, config.password)
        propFind.execute()
        true
    }

    fun listFiles(directoryPath: String = config.url): Result<List<WebDavFile>> = runCatching {
        val url = URL(directoryPath)
        val propFind = PropFindMethod(url)
        propFind.setUsernamePassword(config.username, config.password)
        propFind.execute()

        propFind.resources
            .filter { it.name != null && it.name != directoryPath.trimEnd('/').substringAfterLast('/') }
            .map { resource ->
                WebDavFile(
                    name = resource.name ?: "",
                    path = resource.href ?: "",
                    isDirectory = resource.isCollection,
                    size = resource.contentLength
                )
            }
    }

    fun listAudioFiles(directoryPath: String = config.url): Result<List<WebDavFile>> {
        val result = listFiles(directoryPath)
        return result.map { files ->
            files.filter { file ->
                !file.isDirectory && file.name.substringAfterLast('.').lowercase() in supportedExtensions
            }
        }
    }

    fun getAudioStream(filePath: String): Result<InputStream> = runCatching {
        val url = URL(filePath)
        val get = DavMethod(url)
        get.setUsernamePassword(config.username, config.password)
        get.execute()
        get.responseBodyAsStream
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
